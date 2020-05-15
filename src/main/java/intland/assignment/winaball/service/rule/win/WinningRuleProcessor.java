package intland.assignment.winaball.service.rule.win;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class WinningRuleProcessor<T> {
  private List<WinningRule<T>> rules = new ArrayList<>();

  public WinningRuleProcessor(WinningRule<T>... rules) {
    this(Arrays.asList(rules));
  }

  public WinningRuleProcessor(List<WinningRule<T>> rules) {
    this.rules.addAll(rules);
  }

  public boolean process(T data) {
    boolean win = true;
    for (WinningRule rule : rules) {
      win = win && rule.isWinning(data);
    }
    return win;
  }

}
