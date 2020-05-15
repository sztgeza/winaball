package intland.assignment.winaball.repository;

import intland.assignment.winaball.entity.Country;
import intland.assignment.winaball.entity.Coupon;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface CouponRepository extends PagingAndSortingRepository<Coupon, Integer> {
  boolean existsCouponByNumber(String number);

  @Query(value = "SELECT count(1) FROM Coupon c where c.redeemer.territory.country = :country")
  Integer countRedeemsByCountry(@Param("country") Country country);

  @Query(value = "SELECT count(1) FROM Coupon c where c.redeemer.territory.country = :country and c.redeemDate = :day and c.winner = true")
  Integer countWinnersByCountryAndDay(@Param("country") Country country, @Param("day") LocalDate day);

  @Query(value = "SELECT count(1) FROM Coupon c where c.redeemer.territory.country = :country and c.winner = true")
  Integer countWinnersByCountry(@Param("country") Country country);

  @Query("select c from Coupon c " +
      "where c.winner = true " +
      "and c.redeemer.territory.country = :country  " +
      "and c.redeemDate >= (CASE WHEN :from IS NULL THEN '1900-01-01' ELSE :from END) " +
      "and c.redeemDate <= (CASE WHEN :to IS NULL THEN '3000-12-31' ELSE :to END)")
  List<CouponView> findWinnerCouponsByCountry(@Param("country")Country country,
      @Param("from") LocalDate from,
      @Param("to") LocalDate to,
      Pageable pageable);

  interface CouponView {
    String getNumber();
    @Value("#{target.redeemer.email}")
    String getEmail();
  }

}
