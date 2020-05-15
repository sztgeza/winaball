package intland.assignment.winaball.service.rule.validation.user;

import intland.assignment.winaball.dto.RegistrationInput;
import intland.assignment.winaball.exception.ValidationException;
import intland.assignment.winaball.service.rule.validation.ValidationRule;
import java.time.LocalDate;
import java.time.Period;

public class AgeValidation implements ValidationRule<RegistrationInput> {
  private static final int MIN_AGE = 13;

  @Override
  public void validate(RegistrationInput input) throws ValidationException {
    if (input.getDob() == null) {
      throw new ValidationException("User cannot be younger than 13");
    }
    Period age = Period.between(input.getDob(), LocalDate.now());

    if (age.getYears() < MIN_AGE || (age.getYears() == MIN_AGE && age.getDays() + age.getMonths() == 0)) {
      throw new ValidationException("User cannot be younger than 13");
    }
  }
}
