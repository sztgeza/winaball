package intland.assignment.winaball.service.rule.win;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static  org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class WinningRuleProcessorTest {

  @Mock
  private WinningRule rule_1;
  @Mock
  private WinningRule rule_2;

  private WinningRuleProcessor ruleProcessor;

  @Before
  public void init() {
    ruleProcessor = new WinningRuleProcessor(rule_1, rule_2);
  }

  @Test
  public void shouldReturnFalseWhenBothReturnFalse() {
    //given
    Object context = new Object();
    when(rule_1.isWinning(context)).thenReturn(false);
    when(rule_2.isWinning(context)).thenReturn(false);

    //when
    boolean actual = ruleProcessor.process(context);

    //then
    assertThat(actual, is(false));
    verify(rule_1, times(1)).isWinning(context);
    verify(rule_2, times(0)).isWinning(context);
  }

  @Test
  public void shouldReturnFalseWhenFirstReturnFalseSecondReturnTrue() {
    //given
    Object context = new Object();
    when(rule_1.isWinning(context)).thenReturn(false);
    when(rule_2.isWinning(context)).thenReturn(true);

    //when
    boolean actual = ruleProcessor.process(context);

    //then
    assertThat(actual, is(false));
    verify(rule_1, times(1)).isWinning(context);
    verify(rule_2, times(0)).isWinning(context);
  }

  @Test
  public void shouldReturnFalseWhenFirstReturnTrueSecondReturnFalse() {
    //given
    Object context = new Object();
    when(rule_1.isWinning(context)).thenReturn(true);
    when(rule_2.isWinning(context)).thenReturn(false);

    //when
    boolean actual = ruleProcessor.process(context);

    //then
    assertThat(actual, is(false));
    verify(rule_1, times(1)).isWinning(context);
    verify(rule_2, times(1)).isWinning(context);
  }

  @Test
  public void shouldReturnTrueWhenFirstReturnTrueSecondReturnTrue() {
    //given
    Object context = new Object();
    when(rule_1.isWinning(context)).thenReturn(true);
    when(rule_2.isWinning(context)).thenReturn(true);

    //when
    boolean actual = ruleProcessor.process(context);

    //then
    assertThat(actual, is(true));
    verify(rule_1, times(1)).isWinning(context);
    verify(rule_2, times(1)).isWinning(context);
  }

}