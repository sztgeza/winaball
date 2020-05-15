package intland.assignment.winaball.service.rule.validation.user;

import intland.assignment.winaball.dto.RegistrationInput;
import intland.assignment.winaball.exception.ValidationException;
import intland.assignment.winaball.service.rule.validation.ValidationRule;

public class TerritoryValidation implements ValidationRule<RegistrationInput> {

  @Override
  public void validate(RegistrationInput input) throws ValidationException {
    if (input.getTerritory() == null) {
      throw new ValidationException("Territory is not supported");
    }
  }
}
