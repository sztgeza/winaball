package intland.assignment.winaball.service.rule.validation;

import intland.assignment.winaball.exception.ValidationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ValidationRuleProcessor<T> {
  private final List<ValidationRule<T>> rules = new ArrayList<>();

  public ValidationRuleProcessor(ValidationRule<T>... rules) {
    this(Arrays.asList(rules));
  }
  public ValidationRuleProcessor(List<ValidationRule<T>> rules) {
    this.rules.addAll(rules);
  }

  public void process(T data) throws ValidationException {
    Optional<ValidationException> aggregatedException = Optional.ofNullable(null);
    for (ValidationRule rule : rules) {
      try {
        rule.validate(data);
      } catch (ValidationException e) {
        if (!aggregatedException.isPresent()) {
          aggregatedException = Optional.ofNullable(new ValidationException(e));
        } else {
          aggregatedException.get().addException(e);
        }
      }
    }
    if (aggregatedException.isPresent()) {
      throw aggregatedException.get();
    }
  }

}
