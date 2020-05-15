package intland.assignment.winaball.service.impl;

import intland.assignment.winaball.Application;
import intland.assignment.winaball.dto.RegistrationInput;
import intland.assignment.winaball.entity.Country;
import intland.assignment.winaball.entity.User;
import intland.assignment.winaball.exception.ValidationException;
import intland.assignment.winaball.repository.CouponRepository;
import intland.assignment.winaball.service.UserService;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
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
public class UserServiceImplIntegrationTest {

  @Autowired
  private UserService userService;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private CouponRepository couponRepository;

  @Autowired
  private DataSource dataSource;

  @Before
  public void init() {
    String INSERT_TERRITORY = "INSERT INTO territory(id,country,daily_balls_to_win,total_balls_to_win,which_redeem_wins) values (?,?,?,?,?)";
    String INSERT_USER = "INSERT INTO user(id, email, dob, territory_id) values(?,?,?,?)";
    jdbcTemplate.update(INSERT_TERRITORY, 1, "HUNGARY", 100, 5000, 80);
    jdbcTemplate.update(INSERT_TERRITORY, 2, "GERMANY", 250, 10000, 40);
  }
  /*
  @Test
  public void shouldRegisterUser() throws ValidationException {
    try {
      User user1 = userService.registerUser(new RegistrationInput("sztgeza@gmail.com",
          LocalDate.of(1973, 6, 1), Country.HUNGARY));
      System.out.println(user1.getId());

      User user2 = userService.registerUser(new RegistrationInput("jurgenschmidt@gmail.com",
          LocalDate.of(1900, 1, 1), Country.GERMANY));
      System.out.println(user2.getId());

    } catch (ValidationException e) {
      System.out.println(e.getMessages());
    }

  }*/

  @Test
  public void shouldInsertUserWithSimpleJdbcInsert() {
    Map<String, Object> params = new HashMap<>();
    params.put("dob", LocalDate.of(1901,1,1));
    params.put("email", "email_01@gmail.com");
    params.put("territory_id", 1);

    Number userId = new SimpleJdbcInsert(dataSource).withTableName("user").usingGeneratedKeyColumns("id").executeAndReturnKey(params);
    System.out.println(userId);
  }

}