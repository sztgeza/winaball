package intland.assignment.winaball.service.rule.validation.coupon;

import intland.assignment.winaball.dto.RedeemContext;
import intland.assignment.winaball.exception.ValidationException;
import intland.assignment.winaball.service.rule.validation.ValidationRule;
import org.apache.commons.lang3.StringUtils;

public class NumberFormatValidation implements ValidationRule<RedeemContext> {
  private static final int NUMBER_LENGTH = 10;

  @Override
  public void validate(RedeemContext data) throws ValidationException {
    if (StringUtils.length(data.getCouponNumber()) != NUMBER_LENGTH) {
      throw new ValidationException("Invalid number format");
    }
  }
}
