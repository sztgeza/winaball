package intland.assignment.winaball.service.rule.validation;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import intland.assignment.winaball.exception.ValidationException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ValidationRuleProcessorTest {

  @Mock
  private ValidationRule rule_1;

  @Mock
  private ValidationRule rule_2;

  private ValidationRuleProcessor ruleProcessor;

  @Before
  public void init() {
    ruleProcessor = new ValidationRuleProcessor(rule_1, rule_2);
  }

  @Test
  public void shouldNotThrowExceptionWhenBothPass() throws ValidationException {
    Object data = new Object();
    doNothing().when(rule_1).validate(data);
    doNothing().when(rule_2).validate(data);
    ruleProcessor.process(data);
    verify(rule_1, times(1)).validate(data);
    verify(rule_2, times(1)).validate(data);
  }

  @Test
  public void shouldThrowExceptionWhenFirstFailsSecondPasses() throws ValidationException {
    Object data = new Object();
    String message_1 = "exception_1";
    doThrow(new ValidationException(message_1)).when(rule_1).validate(data);
    doNothing().when(rule_2).validate(data);

    try {
      ruleProcessor.process(data);
    } catch (ValidationException aggregatedException) {
      List<String> messages = aggregatedException.getMessages();
      assertThat(messages, containsInAnyOrder(message_1));
    }
    verify(rule_1, times(1)).validate(data);
    verify(rule_2, times(1)).validate(data);
  }

  @Test
  public void shouldThrowExceptionWhenFirstPassesSecondFails() throws ValidationException {
    Object data = new Object();
    String message_2 = "exception_2";
    doNothing().when(rule_1).validate(data);
    doThrow(new ValidationException(message_2)).when(rule_2).validate(data);

    try {
      ruleProcessor.process(data);
    } catch (ValidationException aggregatedException) {
      List<String> messages = aggregatedException.getMessages();
      assertThat(messages, containsInAnyOrder(message_2));
    }

    verify(rule_1, times(1)).validate(data);
    verify(rule_2, times(1)).validate(data);
  }

  @Test
  public void shouldThrowExceptionWhenFirstFailsSecondFails() throws ValidationException {
    Object data = new Object();
    String message_1 = "exception_1";
    String message_2 = "exception_2";
    doThrow(new ValidationException(message_1)).when(rule_1).validate(data);
    doThrow(new ValidationException(message_2)).when(rule_2).validate(data);

    try {
      ruleProcessor.process(data);
    } catch (ValidationException aggregatedException) {
      List<String> messages = aggregatedException.getMessages();
      assertThat(messages, containsInAnyOrder(message_1, message_2));
    }
    verify(rule_1, times(1)).validate(data);
    verify(rule_2, times(1)).validate(data);
  }
}