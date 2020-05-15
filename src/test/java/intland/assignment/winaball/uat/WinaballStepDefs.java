package intland.assignment.winaball.uat;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.is;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import intland.assignment.winaball.Application;
import intland.assignment.winaball.entity.Country;
import intland.assignment.winaball.entity.User;
import intland.assignment.winaball.service.UserService;
import intland.assignment.winaball.testutil.Territory;
import intland.assignment.winaball.service.CouponService;
import intland.assignment.winaball.testutil.TestUtils;
import java.time.LocalDate;
import org.hamcrest.Matchers;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("integration")
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Application.class })
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class WinaballStepDefs {

  private static final int GERMANY_ID = 1; //for both territory and user
  private static final int HUNGARY_ID = 2; //for both territory and user

  private static final String INSERT_TERRITORY = "INSERT INTO territory(id,country,daily_balls_to_win,total_balls_to_win,which_redeem_wins) values (?,?,?,?,?)";
  private static final String INSERT_USER = "INSERT INTO user(id, email, dob, territory_id) VALUES(?,?,?,?)";
  private static final String INSERT_COUPON = "INSERT INTO coupon(number, redeem_date, winner, redeemer_id) VALUES(?,?,?,?)";

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private CouponService couponService;

  @Autowired
  private UserService userService;

  private Exception resultException;

  private Boolean win;

  private User resultUser;

  @Before
  public void setup() {
    jdbcTemplate.update("delete from coupon");
    jdbcTemplate.update("delete from user");
    jdbcTemplate.update("delete from territory");
    resultException = null;
    win = null;
    resultUser = null;
  }

  @Given("^a territory (GERMANY|HUNGARY)$")
  public void givenATerritory(String country) throws Throwable {
    Territory territory = Territory.getByName(country);
    jdbcTemplate.update(INSERT_TERRITORY,
        territory.getId(),
        territory.getName(),
        territory.getDailyBalls(),
        territory.getTotalBalls(),
        territory.getWhichRedeemWins());
  }

  @Given("^a user registered with email (.*) and territory (GERMANY|HUNGARY)$")
  public void givenAUserRegisteredWithEmailAndTerritory(String email, String aTerritory) {
    Territory territory = Territory.getByName(aTerritory);
    jdbcTemplate.update(INSERT_USER,
        territory.getId(), email, LocalDate.of(1900,1, 1), territory.getId());
  }

  @Given("^a coupon already redeemed with number (.*)$")
  public void givenACouponAlreeadyRedeemedWithNumber(String number) {
    jdbcTemplate.update(INSERT_COUPON, number, LocalDate.now(), false, 1);
  }

  @Given("^(\\d+) coupons already redeemed today in (GERMANY|HUNGARY)$")
  public void givenANumberOfCouponsRedeemedToday(int numberOfCoupons, String aTerritory) {
    Territory territory = Territory.getByName(aTerritory);
    TestUtils.insertCouponsToday(jdbcTemplate, numberOfCoupons, territory);
  }

  @Given("^(\\d+) coupons already redeemed since the beginning in (GERMANY|HUNGARY)$")
  public void givenANumberOfCouponsRedeemedSinceTheBeginning(int numberOfCoupons, String aTerritory) {
    Territory territory = Territory.getByName(aTerritory);
    TestUtils.insertCouponsUpToToday(jdbcTemplate, numberOfCoupons, territory);
  }

  @Given("^a user with email (.*), dob (.*), country (.*) attempts to registrate$")
  public void whenUserAttemptsToRegistrate(String email, String dob, String country) throws Throwable {
    try {
      resultUser = userService.registerUser(email, LocalDate.parse(dob), null);
    } catch (Exception e) {
      resultException = e;
    }
  }

  @When("^a user with email (.*) and dob (.*) and country (.*) attempts to registrate$")
  public void whenUsetAttemptsToRegistrate(String email, String dob, String country) {
    try {
      resultUser = userService.registerUser(email, LocalDate.parse(dob), Country.byName(country));
    } catch (Exception e) {
      resultException = e;
    }
  }

  @When("^a user with email (.*) attempts to redeem a coupon with number (.*)$")
  public void whenUserAttemptsToRedeemCoupon(String email, String couponNumber) {
    try {
      win = couponService.redeemCoupon(email, couponNumber);
    } catch (Exception e) {
      resultException = e;
    }
  }

  @Then("^the attempt should be invalid$")
  public void shouldTheResultBeAnException() {
    assertThat(resultException, notNullValue());
  }

  @Then("^the attempt should be valid with result (true|false)$")
  public void shouldTheAttemptBeValidWithResult(String result) {
    assertThat(resultException, Matchers.nullValue());
    assertThat(win, notNullValue());
    assertThat(win, is(Boolean.valueOf(result)));
  }

  @Then("^the registration attempt should be valid$")
  public void shouldTeRegistrationBeValid() {
    assertThat(resultUser, Matchers.notNullValue());
  }

}
