package intland.assignment.winaball.service.impl;


import static intland.assignment.winaball.service.impl.SameCoupon.sameCouponAs;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import intland.assignment.winaball.Application;
import intland.assignment.winaball.entity.Country;
import intland.assignment.winaball.entity.Coupon;
import intland.assignment.winaball.repository.CouponRepository;
import intland.assignment.winaball.repository.CouponRepository.CouponView;
import intland.assignment.winaball.service.CouponService;
import intland.assignment.winaball.testutil.Territory;
import intland.assignment.winaball.testutil.TestUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

//TODO: this should be improved with more testcases and more fine-grained dataset.
@ActiveProfiles("integration")
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Application.class })
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class CouponServiceImplIntegrationTest {
  private final static Logger LOG = LoggerFactory.getLogger(CouponServiceImplIntegrationTest.class);

  private static final String EMAIL_HUNGARY = "email_hu@email.com";
  private static final String EMAIL_GERMANY = "email_de@email.com";

  private static final String INSERT_TERRITORY = "INSERT INTO territory(id,country,daily_balls_to_win,total_balls_to_win,which_redeem_wins) values (?,?,?,?,?)";
  private static final String INSERT_USER = "INSERT INTO user(id, email, dob, territory_id) values(?,?,?,?)";

  @Autowired
  private CouponService couponService;

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
    TestUtils.insertCouponsUpToToday(jdbcTemplate, 2000, Territory.HUNGARY);
  }

  @Test
  public void shouldFindWinnersFirstPageWithoutTimeLimit() {

    List<CouponView> coupons = couponService.findWinnerCouponsByCountry(
        Country.HUNGARY,
        Optional.empty(),
        Optional.empty(),
        PageRequest.of(0, 5));

    assertThat(coupons, containsInAnyOrder(
        sameCouponAs("0000000080", EMAIL_HUNGARY),
        sameCouponAs("0000000160", EMAIL_HUNGARY),
        sameCouponAs("0000000240", EMAIL_HUNGARY),
        sameCouponAs("0000000320", EMAIL_HUNGARY),
        sameCouponAs("0000000400", EMAIL_HUNGARY))
    );
  }

  @Test
  public void shouldFindWinnersLastPageWithoutTimeLimit() {

    List<CouponView> coupons = couponService.findWinnerCouponsByCountry(
        Country.HUNGARY,
        Optional.empty(),
        Optional.empty(),
        PageRequest.of(4, 5));

    assertThat(coupons, containsInAnyOrder(
        sameCouponAs("0000001680", EMAIL_HUNGARY),
        sameCouponAs("0000001760", EMAIL_HUNGARY),
        sameCouponAs("0000001840", EMAIL_HUNGARY),
        sameCouponAs("0000001920", EMAIL_HUNGARY))
    );
  }

  @Test
  public void shouldFindWinnersFirstPageWithTimeFrom() {

    List<CouponView> coupons = couponService.findWinnerCouponsByCountry(
        Country.HUNGARY,
        Optional.of(LocalDate.now().minusDays(3)),
        Optional.empty(),
        PageRequest.of(0, 5));

    assertThat(coupons, containsInAnyOrder(
        sameCouponAs("0000001760", EMAIL_HUNGARY),
        sameCouponAs("0000001840", EMAIL_HUNGARY),
        sameCouponAs("0000001920", EMAIL_HUNGARY) )
    );
  }


}

class SameCoupon extends BaseMatcher<CouponView> {
  private String referenceNumber;
  private String referenceEmail;


  public SameCoupon(String referenceNumber, String referenceEmail) {
    this.referenceNumber = referenceNumber;
    this.referenceEmail = referenceEmail;
  }
  @Override
  public boolean matches(Object actual) {
    CouponView act = (CouponView) actual;
    return Objects.equals(act.getEmail(), this.referenceEmail)
        && Objects.equals(act.getNumber(), this.referenceNumber);
  }

  public void describeTo(Description description) {
    description.appendText("Not the same coupon");
  }

  public static SameCoupon sameCouponAs(String referenceNumber, String referenceEmail) {
    return new SameCoupon(referenceNumber, referenceEmail);
  }
}
