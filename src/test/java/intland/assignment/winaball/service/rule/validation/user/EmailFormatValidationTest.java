package intland.assignment.winaball.service.rule.validation.user;

import static org.junit.Assert.*;

import intland.assignment.winaball.dto.RegistrationInput;
import intland.assignment.winaball.exception.ValidationException;
import org.junit.Test;

public class EmailFormatValidationTest {

  private EmailFormatValidation emailFormatValidation = new EmailFormatValidation();

  @Test(expected = ValidationException.class)
  public void shouldNotBeValidWhenEmailIsNull() throws ValidationException {
    RegistrationInput input = new RegistrationInput(null, null, null);
    emailFormatValidation.validate(input);
  }

  @Test(expected = ValidationException.class)
  public void shouldNotBeValidWhenEmailIsEmpty() throws ValidationException {
    RegistrationInput input = new RegistrationInput("", null, null);
    emailFormatValidation.validate(input);
  }

  @Test(expected = ValidationException.class)
  public void shouldNotBeValidWhenDomainIsEmpty() throws ValidationException {
    RegistrationInput input = new RegistrationInput("email@", null, null);
    emailFormatValidation.validate(input);
  }

  @Test(expected = ValidationException.class)
  public void shouldNotBeValidWhenNoAt() throws ValidationException {
    RegistrationInput input = new RegistrationInput("email", null, null);
    emailFormatValidation.validate(input);
  }

  @Test(expected = ValidationException.class)
  public void shouldNotBeValidWhenUsernameIsEmpty() throws ValidationException {
    RegistrationInput input = new RegistrationInput("@domain", null, null);
    emailFormatValidation.validate(input);
  }

  @Test
  public void shouldBeValidWhenEmailIsValid() throws ValidationException {
    RegistrationInput input = new RegistrationInput("username@domain", null, null);
    emailFormatValidation.validate(input);
  }




}