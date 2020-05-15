package intland.assignment.winaball.testutil;

import java.time.LocalDate;
import org.springframework.jdbc.core.JdbcTemplate;

public class TestUtils {
  private static final String INSERT_COUPON = "INSERT INTO coupon(number, redeem_date, winner, redeemer_id) VALUES(?,?,?,?)";

  public static void insertCouponsUpToToday(JdbcTemplate jdbcTemplate, int numberOfCoupons, Territory territory) {
    //assume that every day dailyBallsWin balls won and no more redeems on that day
    LocalDate startDate = LocalDate.now().minusDays(numberOfCoupons / territory.getDailyBalls());
    //int maxId = jdbcTemplate.queryForObject("select coalesce(max(id),0) from coupon", Integer.class);
    //for (int i=maxId+1; i< numberOfCoupons; i++) {
    for (int i=0; i< numberOfCoupons; i++) {
      boolean win = (i> 0 && i % territory.getWhichRedeemWins()  == 0);
      String number = String.format("%010d", i);
      LocalDate date = startDate.plusDays(i / territory.getDailyBalls());

      jdbcTemplate.update(
          INSERT_COUPON,
          number, date, win, territory.getId());
    }
  }

  public static void insertCouponsToday(JdbcTemplate jdbcTemplate, int numberOfCoupons, Territory territory) {
    //assume that every day dailyBallsWin balls won and no more redeems on that day
    for (int i=0; i< numberOfCoupons; i++) {
      boolean win = (i> 0 && i % territory.getWhichRedeemWins()  == 0);
      String number = String.format("%010d", i);
      LocalDate date = LocalDate.now();

      jdbcTemplate.update(
          INSERT_COUPON,
          number, date, win, territory.getId());
    }
  }


}
