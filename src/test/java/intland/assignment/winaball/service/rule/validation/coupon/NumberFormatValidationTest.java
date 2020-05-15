package intland.assignment.winaball.service.rule.validation.coupon;

import intland.assignment.winaball.dto.RedeemContext;
import intland.assignment.winaball.exception.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class NumberFormatValidationTest {
  private NumberFormatValidation numberFormatValidation = new NumberFormatValidation();

  @Test(expected = ValidationException.class)
  public void shouldNotBeValidWhenCouponNumberIsEmpty() throws ValidationException {
    String couponNumber = StringUtils.EMPTY;
    numberFormatValidation.validate(new RedeemContext(null, couponNumber,null));
  }

  @Test(expected = ValidationException.class)
  public void shouldNotBeValidWhenCouponNumberIsNull() throws ValidationException {
    String couponNumber = null;
    numberFormatValidation.validate(new RedeemContext(null, couponNumber,null));
  }

  @Test(expected = ValidationException.class)
  public void shouldNotBeValidWhenCouponNumberIslongerThan10() throws ValidationException {
    String couponNumber = "12345678901";
    numberFormatValidation.validate(new RedeemContext(null, couponNumber,null));
  }

  @Test
  public void shouldBeValidWhenCouponNumberIs10() throws ValidationException {
    String couponNumber = "1234567890";
    numberFormatValidation.validate(new RedeemContext(null, couponNumber,null));
  }
}