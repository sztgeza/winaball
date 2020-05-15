package intland.assignment.winaball.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class ValidationException extends Exception {
  private List<String> messages = new ArrayList<>();

  public ValidationException(String message) {
    super(message);
    this.messages.add(message);
  }

  public ValidationException(ValidationException other) {
    addException(other);
  }

  public void addException(ValidationException other) {
    this.messages.addAll(other.getMessages());
  }

  public List<String> getMessages() {
    return messages;
  }

  @Override
  public String getMessage() {
    StringJoiner joiner = new StringJoiner(",", "[", "]");
    this.messages.stream().forEach(m -> joiner.add(m));
    return joiner.toString();
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", ValidationException.class.getSimpleName() + "[", "]")
        .add("messages=" + messages)
        .toString();
  }
}
