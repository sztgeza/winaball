package intland.assignment.winaball.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
public class Territory {
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Integer id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Country country;

  @Column(nullable = false)
  private int totalBallsToWin;

  @Column(nullable = false)
  private int dailyBallsToWin;

  @Column(nullable = false)
  private int whichRedeemWins;

  public Territory() {}

  public Territory(Country country, int totalBallsToWin, int dailyBallsToWin, int whichRedeemWins) {
    this.country = country;
    this.totalBallsToWin = totalBallsToWin;
    this.dailyBallsToWin = dailyBallsToWin;
    this.whichRedeemWins = whichRedeemWins;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Country getCountry() {
    return country;
  }

  public void setCountry(Country country) {
    this.country = country;
  }

  public int getTotalBallsToWin() {
    return totalBallsToWin;
  }

  public void setTotalBallsToWin(int totalBallsToWin) {
    this.totalBallsToWin = totalBallsToWin;
  }

  public int getDailyBallsToWin() {
    return dailyBallsToWin;
  }

  public void setDailyBallsToWin(int dailyBallsToWin) {
    this.dailyBallsToWin = dailyBallsToWin;
  }

  public int getWhichRedeemWins() {
    return whichRedeemWins;
  }

  public void setWhichRedeemWins(int whichRedeemWins) {
    this.whichRedeemWins = whichRedeemWins;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Territory territory = (Territory) o;

    return new EqualsBuilder()
        .append(country, territory.country)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(country)
        .toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("country", country)
        .toString();
  }
}
