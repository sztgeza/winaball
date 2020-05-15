package intland.assignment.winaball.testutil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum Territory {

  GERMANY("GERMANY", 1, 250, 10000, 40),
  HUNGARY("HUNGARY", 2, 100, 5000, 80);

  String name = "";
  int id = 0;
  int dailyBalls = 0;
  int totalBalls = 0;
  int whichRedeemWins = 0;

  private static final Map<String, Territory> NAME_MAP;

  static {
    Map<String, Territory> nameMap = new HashMap<>();
    for (Territory value: values()) {
      nameMap.put(value.name, value);
    }
    NAME_MAP = Collections.unmodifiableMap(nameMap);
  }

  Territory(String name, int id, int dailyBalls, int totalBalls, int whichRedeemWins){
    this.name = name;
    this.id = id;
    this.dailyBalls = dailyBalls;
    this.totalBalls = totalBalls;
    this.whichRedeemWins = whichRedeemWins;
  }

  public static Territory getByName(String name) { return NAME_MAP.get(name); }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getDailyBalls() {
    return dailyBalls;
  }

  public int getTotalBalls() {
    return totalBalls;
  }

  public int getWhichRedeemWins() {
    return whichRedeemWins;
  }
}

