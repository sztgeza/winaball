package intland.assignment.winaball.service.rule.validation.user;

import intland.assignment.winaball.dto.RegistrationInput;
import intland.assignment.winaball.exception.ValidationException;
import intland.assignment.winaball.service.rule.validation.ValidationRule;
import java.util.regex.Pattern;
import org.springframework.util.StringUtils;

public class EmailFormatValidation implements ValidationRule<RegistrationInput> {
  private static final String PATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";

  @Override
  public void validate(RegistrationInput input) throws ValidationException {

    if (StringUtils.isEmpty(input.getEmail()) ||
        !Pattern.compile(PATTERN).matcher(input.getEmail()).matches()) {
      throw new ValidationException("Empty or invalid email");
    };
  }
}
