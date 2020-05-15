package intland.assignment.winaball.service.rule.validation.user;


import intland.assignment.winaball.dto.RegistrationInput;
import intland.assignment.winaball.exception.ValidationException;
import java.time.LocalDate;
import org.junit.Test;

public class AgeValidationTest {

  private AgeValidation ageValidation = new AgeValidation();

  @Test(expected = ValidationException.class)
  public void shouldNotBeValidWhenDobIsNull() throws ValidationException {
    RegistrationInput input = new RegistrationInput(null, null, null);
    ageValidation.validate(input);
  }

  @Test(expected = ValidationException.class)
  public void shouldNotBeValidWhenTooYoung() throws ValidationException {
    RegistrationInput input = new RegistrationInput(null, LocalDate.parse("2020-01-01"), null);
    ageValidation.validate(input);
  }

  @Test(expected = ValidationException.class)
  public void shouldNotBeValidWhenAgeIs13() throws ValidationException {
    RegistrationInput input = new RegistrationInput(null, LocalDate.now().minusYears(13), null);
    ageValidation.validate(input);
  }

  @Test(expected = ValidationException.class)
  public void shouldNotBeValidWhenInFuture() throws ValidationException {
    RegistrationInput input = new RegistrationInput(null, LocalDate.now().plusDays(1), null);
    ageValidation.validate(input);
  }

  @Test
  public void shouldBeValidWhenOlderThan13() throws ValidationException {
    RegistrationInput input = new RegistrationInput(null, LocalDate.now().minusYears(13).minusDays(1), null);
    ageValidation.validate(input);
  }


}