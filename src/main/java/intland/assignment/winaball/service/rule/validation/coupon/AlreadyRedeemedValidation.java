package intland.assignment.winaball.service.rule.validation.coupon;

import intland.assignment.winaball.dto.RedeemContext;
import intland.assignment.winaball.exception.InvalidAttemptException;
import intland.assignment.winaball.exception.ValidationException;
import intland.assignment.winaball.repository.CouponRepository;
import intland.assignment.winaball.service.rule.validation.ValidationRule;

public class AlreadyRedeemedValidation implements ValidationRule<RedeemContext> {

  private final CouponRepository couponRepository;

  public AlreadyRedeemedValidation(CouponRepository couponRepository) {
    this.couponRepository = couponRepository;
  }

  @Override
  public void validate(RedeemContext data) throws ValidationException {
     if (couponRepository.existsCouponByNumber(data.getCouponNumber())) {
       throw new InvalidAttemptException("Coupon already redeemed");
     }
  }
}
