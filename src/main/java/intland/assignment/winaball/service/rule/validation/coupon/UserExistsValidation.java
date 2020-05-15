package intland.assignment.winaball.service.rule.validation.coupon;

import intland.assignment.winaball.dto.RedeemContext;
import intland.assignment.winaball.exception.ValidationException;
import intland.assignment.winaball.service.rule.validation.ValidationRule;

public class UserExistsValidation implements ValidationRule<RedeemContext> {

  @Override
  public void validate(RedeemContext data) throws ValidationException {
    if (data.getUser() == null) {
      throw new ValidationException("User not registered");
    }
  }

}