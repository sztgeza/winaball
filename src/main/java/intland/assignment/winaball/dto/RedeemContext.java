package intland.assignment.winaball.dto;

import intland.assignment.winaball.entity.User;
import java.time.LocalDate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class RedeemContext {
  private User user;
  private String couponNumber;
  private LocalDate date;

  public RedeemContext(User user, String couponNumber, LocalDate date) {
    this.user = user;
    this.couponNumber = couponNumber;
    this.date = date;
  }

  public User getUser() {
    return user;
  }

  public String getCouponNumber() {
    return couponNumber;
  }

  public LocalDate getDate() {
    return date;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RedeemContext that = (RedeemContext) o;

    return new EqualsBuilder()
        .append(user, that.user)
        .append(couponNumber, that.couponNumber)
        .append(date, that.date)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(user)
        .append(couponNumber)
        .append(date)
        .toHashCode();
  }
}
