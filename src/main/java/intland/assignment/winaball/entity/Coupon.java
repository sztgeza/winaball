package intland.assignment.winaball.entity;

import java.time.LocalDate;
import java.util.StringJoiner;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
public class Coupon {
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Integer id;

  @Column(unique = true, nullable = false)
  private String number;

  @ManyToOne(optional = false)
  private User redeemer;

  @Column(nullable = false)
  private LocalDate redeemDate;

  @Column(nullable = false)
  private boolean winner;

  public Coupon() {}

  public Coupon(String number, User redeemer, LocalDate redeemDate, boolean winner) {
    this.number = number;
    this.redeemer = redeemer;
    this.redeemDate = redeemDate;
    this.winner = winner;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public User getRedeemer() {
    return redeemer;
  }

  public void setRedeemer(User redeemer) {
    this.redeemer = redeemer;
  }

  public LocalDate getRedeemDate() {
    return redeemDate;
  }

  public void setRedeemDate(LocalDate redeemDate) {
    this.redeemDate = redeemDate;
  }

  public boolean isWinner() {
    return winner;
  }

  public void setWinner(boolean winner) {
    this.winner = winner;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Coupon coupon = (Coupon) o;

    return new EqualsBuilder()
        .append(number, coupon.number)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(number)
        .toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("number", number)
        .append("redeemer", redeemer)
        .append("redeemDate", redeemDate)
        .append("winner", winner)
        .toString();
  }
}
