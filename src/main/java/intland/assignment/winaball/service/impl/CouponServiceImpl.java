package intland.assignment.winaball.service.impl;

import intland.assignment.winaball.dto.RedeemContext;
import intland.assignment.winaball.entity.Country;
import intland.assignment.winaball.entity.Coupon;
import intland.assignment.winaball.entity.User;
import intland.assignment.winaball.exception.ValidationException;
import intland.assignment.winaball.repository.CouponRepository;
import intland.assignment.winaball.repository.CouponRepository.CouponView;
import intland.assignment.winaball.repository.UserRepository;
import intland.assignment.winaball.service.CouponService;
import intland.assignment.winaball.service.rule.validation.ValidationRuleProcessor;
import intland.assignment.winaball.service.rule.win.WinningRuleProcessor;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CouponServiceImpl implements CouponService {

  private final UserRepository userRepository;
  private final CouponRepository couponRepository;
  private final ValidationRuleProcessor<RedeemContext> couponValidator;
  private final WinningRuleProcessor<RedeemContext> winningRules;

  private final ReentrantLock lock = new ReentrantLock();

  public CouponServiceImpl(UserRepository userRepository,
      CouponRepository couponRepository,
      @Qualifier("couponValidation") ValidationRuleProcessor couponValidator,
      WinningRuleProcessor<RedeemContext> winningRules) {
    this.userRepository = userRepository;
    this.couponRepository = couponRepository;
    this.couponValidator = couponValidator;
    this.winningRules = winningRules;
  }

  @Override
  @Transactional
  public boolean redeemCoupon(String email, String number) throws ValidationException {
    lock.lock();
    //synchronized (this) {
    try {
      User user = userRepository.findByEmail(email);
      RedeemContext context = new RedeemContext(user, number, LocalDate.now());
      couponValidator.process(context);
      boolean win = winningRules.process(context);
      couponRepository
          .save(new Coupon(context.getCouponNumber(), context.getUser(), context.getDate(), win));
      return win;
    } catch (Exception e) {
      throw e;
    } finally {
      lock.unlock();
    }
    //}
  }

  public List<CouponView> findWinnerCouponsByCountry(Country country,
      Optional<LocalDate> from,
      Optional<LocalDate> to,
      Pageable pageable) {
    return couponRepository.findWinnerCouponsByCountry(country,
        from.orElse(null),
        to.orElse(null),
        pageable);
  }

}
