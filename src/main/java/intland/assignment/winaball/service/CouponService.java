package intland.assignment.winaball.service;

import intland.assignment.winaball.entity.Country;
import intland.assignment.winaball.exception.ValidationException;
import intland.assignment.winaball.repository.CouponRepository.CouponView;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface CouponService {
  boolean redeemCoupon(String email, String number) throws ValidationException;

  List<CouponView> findWinnerCouponsByCountry(Country country,
      Optional<LocalDate> from,
      Optional<LocalDate> to,
      Pageable pageable);
}
