package intland.assignment.winaball.web.dto;

import intland.assignment.winaball.entity.Country;

public class TerritoryDTO {
  private Country country;
  private int totalBallsToWin;
  private int dailyBallsToWin;
  private int whichRedeemWins;

  public TerritoryDTO() {}

  public TerritoryDTO(Country country, int totalBallsToWin, int dailyBallsToWin,
      int whichRedeemWins) {
    this.country = country;
    this.totalBallsToWin = totalBallsToWin;
    this.dailyBallsToWin = dailyBallsToWin;
    this.whichRedeemWins = whichRedeemWins;
  }

  public Country getCountry() {
    return country;
  }

  public int getTotalBallsToWin() {
    return totalBallsToWin;
  }

  public int getDailyBallsToWin() {
    return dailyBallsToWin;
  }

  public int getWhichRedeemWins() {
    return whichRedeemWins;
  }
}
