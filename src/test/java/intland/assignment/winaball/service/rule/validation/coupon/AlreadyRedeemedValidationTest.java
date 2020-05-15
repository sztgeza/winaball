package intland.assignment.winaball.service.rule.validation.coupon;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import intland.assignment.winaball.dto.RedeemContext;
import intland.assignment.winaball.exception.ValidationException;
import intland.assignment.winaball.repository.CouponRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AlreadyRedeemedValidationTest {

  @Mock
  private CouponRepository couponRepository;

  @InjectMocks
  private AlreadyRedeemedValidation alreadyRedeemedValidation;

  @Test(expected = ValidationException.class)
  public void shouldNotBeValidWhenCouponAlreadyRedeemed() throws ValidationException {
    //given
    String couponNumber = "1234567890";
    when(couponRepository.existsCouponByNumber(couponNumber)).thenReturn(true);
    RedeemContext context = new RedeemContext(null, couponNumber, null);
    //when
    alreadyRedeemedValidation.validate(context);
    //then
    //exception is thrown
  }

  @Test
  public void shouldBeValidWhenCouponNotRedeemedYet() throws ValidationException {
    //given
    String couponNumber = "1234567890";
    when(couponRepository.existsCouponByNumber(couponNumber)).thenReturn(false);
    RedeemContext context = new RedeemContext(null, couponNumber, null);
    //when
    alreadyRedeemedValidation.validate(context);
    //then
    //nothing happens
  }




}