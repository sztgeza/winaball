package intland.assignment.winaball.service.rule.validation.user;

import intland.assignment.winaball.dto.RegistrationInput;
import intland.assignment.winaball.exception.ValidationException;
import intland.assignment.winaball.repository.UserRepository;
import intland.assignment.winaball.service.rule.validation.ValidationRule;

public class AlreadyRegisteredValidation implements ValidationRule<RegistrationInput> {

  private final UserRepository userRepository;

  public AlreadyRegisteredValidation(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void validate(RegistrationInput input) throws ValidationException {
    if (userRepository.existsUserByEmail(input.getEmail())) {
      throw new ValidationException("User does not exist with given email");
    }
  }
}
