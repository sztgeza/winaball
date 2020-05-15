package intland.assignment.winaball.service.rule.validation;

import intland.assignment.winaball.exception.ValidationException;

public interface ValidationRule<T> {
  void validate(T data) throws ValidationException;
}
