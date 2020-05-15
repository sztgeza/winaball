package intland.assignment.winaball.service.rule.validation.coupon;

import static org.junit.Assert.*;

import intland.assignment.winaball.dto.RedeemContext;
import intland.assignment.winaball.entity.User;
import intland.assignment.winaball.exception.ValidationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserExistsValidationTest {



  private UserExistsValidation userExistsValidation = new UserExistsValidation();

  @Test(expected = ValidationException.class)
  public void shouldNotBeValidWhenUserIsNull() throws ValidationException {
    RedeemContext context = new RedeemContext(null, null, null);
    userExistsValidation.validate(context);
  }

  @Test
  public void shouldBeValidWhenUserIsNotNull() throws ValidationException {
    RedeemContext context = new RedeemContext(new User(), null, null);
    userExistsValidation.validate(context);
  }

}