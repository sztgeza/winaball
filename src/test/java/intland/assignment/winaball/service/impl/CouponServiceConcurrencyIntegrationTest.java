package intland.assignment.winaball.service.impl;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import intland.assignment.winaball.Application;
import intland.assignment.winaball.exception.ValidationException;
import intland.assignment.winaball.repository.CouponRepository;
import intland.assignment.winaball.service.CouponService;
import intland.assignment.winaball.testutil.Territory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("integration")
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Application.class })
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class CouponServiceConcurrencyIntegrationTest {

  private final static Logger LOG = LoggerFactory.getLogger(CouponServiceConcurrencyIntegrationTest.class);

  private static final String EMAIL_HUNGARY = "email_hu@email.com";
  private static final String EMAIL_GERMANY = "email_de@email.com";

  private static final String INSERT_TERRITORY = "INSERT INTO territory(id,country,daily_balls_to_win,total_balls_to_win,which_redeem_wins) values (?,?,?,?,?)";
  private static final String INSERT_USER = "INSERT INTO user(id, email, dob, territory_id) values(?,?,?,?)";
  private static final String INSERT_COUPON = "INSERT INTO coupon(number, redeem_date, winner, redeemer_id) VALUES(?,?,?,?)";

  @Autowired
  private CouponService couponService;

  @Autowired
  private CouponRepository couponRepository;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Before
  public void setup() {
    jdbcTemplate.update(INSERT_TERRITORY,
        Territory.GERMANY.getId(),
        Territory.GERMANY.getName(),
        Territory.GERMANY.getDailyBalls(),
        Territory.GERMANY.getTotalBalls(),
        Territory.GERMANY.getWhichRedeemWins());

    jdbcTemplate.update(INSERT_TERRITORY,
        Territory.HUNGARY.getId(),
        Territory.HUNGARY.getName(),
        Territory.HUNGARY.getDailyBalls(),
        Territory.HUNGARY.getTotalBalls(),
        Territory.HUNGARY.getWhichRedeemWins());

    jdbcTemplate.update(INSERT_USER, 1, EMAIL_GERMANY, LocalDate.of(1900,1, 1), 1);
    jdbcTemplate.update(INSERT_USER, 2, EMAIL_HUNGARY, LocalDate.of(1900,1, 1), 2);
  }


  @Test
  public void shouldHandleParallelRedeem() {
    int numberOfThreads = 200;
    CountDownLatch latch = new CountDownLatch(1);
    ExecutorService service =
        Executors.newFixedThreadPool(numberOfThreads);

    AtomicBoolean running = new AtomicBoolean();
    AtomicInteger overlaps = new AtomicInteger();
    Collection<Future<Outcome>> futures =
        new ArrayList<>(numberOfThreads);

    for (int t = 0; t < numberOfThreads; ++t) {
      futures.add(
          service.submit(
              () -> {
                latch.await();
                if (running.get()) {
                  overlaps.incrementAndGet();
                }
                running.set(true);
                Outcome outCome = tryToRedeemCoupon();
                running.set(false);
                return outCome;
              }
          )
      );
    }


    latch.countDown();
    List<Outcome> outComes = new ArrayList<>();
    for (Future<Outcome> f : futures) {
      try {
        outComes.add(f.get());
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    }

    LOG.debug("overlaps: {}", overlaps.get());
    assertThat(overlaps.get(), greaterThan(0));

    assertThat(outComes.size(), equalTo(numberOfThreads));

    Map<Outcome, Long> outComesCount = outComes.stream().collect(
        groupingBy(x -> x, counting()));

    assertThat(outComesCount.get(Outcome.INSERTED), equalTo(1L));
    assertThat("There is a DataIntegrityViolationException, but should not be any!", outComesCount.get(Outcome.DATAINTEGRITY_VIOLATION_EXCEPTION), Matchers
        .nullValue() );
    assertThat(outComesCount.get(Outcome.VALIDATION_EXCEPTION), equalTo(numberOfThreads - 1L));
  }

  private Outcome tryToRedeemCoupon() {
    try {
      boolean won = couponService.redeemCoupon(EMAIL_HUNGARY, "9876543210");
      return Outcome.INSERTED;
    } catch (ValidationException e) {
      return Outcome.VALIDATION_EXCEPTION;
    } catch (DataIntegrityViolationException de) {
      return Outcome.DATAINTEGRITY_VIOLATION_EXCEPTION;
    }
  }

  private enum Outcome {
    INSERTED,
    VALIDATION_EXCEPTION,
    DATAINTEGRITY_VIOLATION_EXCEPTION
  }
}
