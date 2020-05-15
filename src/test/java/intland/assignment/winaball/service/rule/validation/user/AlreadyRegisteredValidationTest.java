package intland.assignment.winaball.service.rule.validation.user;

import static org.mockito.Mockito.when;

import intland.assignment.winaball.dto.RegistrationInput;
import intland.assignment.winaball.exception.ValidationException;
import intland.assignment.winaball.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AlreadyRegisteredValidationTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private AlreadyRegisteredValidation alreadyRegisteredValidation;

  @Test(expected = ValidationException.class)
  public void shouldNotBeValidWhenUserAlreadyExists() throws ValidationException {
    String email = "abc";
    when(userRepository.existsUserByEmail(email)).thenReturn(true);

    RegistrationInput input = new RegistrationInput(email, null, null);
    alreadyRegisteredValidation.validate(input);
  }

  @Test
  public void shouldBeValidWhenUserNotExistsYet() throws ValidationException {
    String email = "abc";
    when(userRepository.existsUserByEmail(email)).thenReturn(false);

    RegistrationInput input = new RegistrationInput(email, null, null);
    alreadyRegisteredValidation.validate(input);
  }

}