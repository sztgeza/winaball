package intland.assignment.winaball.service.rule.win;

public interface WinningRule<T> {
  boolean isWinning(T data);
}
