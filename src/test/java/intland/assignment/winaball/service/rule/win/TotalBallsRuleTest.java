package intland.assignment.winaball.service.rule.win;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import intland.assignment.winaball.dto.RedeemContext;
import intland.assignment.winaball.entity.Country;
import intland.assignment.winaball.repository.CouponRepository;
import java.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TotalBallsRuleTest {

  private static final int TOTAL_BALLS_TO_WIN = 5000;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private RedeemContext context;
  @Mock
  private CouponRepository couponRepository;

  @InjectMocks
  private TotalBallsRule rule;

  @Captor
  private ArgumentCaptor<Country> countryArgCaptor;

  @Test
  public void shouldWinWhenLessThanTotalBallsWon() {
    //given
    when(context.getUser().getTerritory().getCountry()).thenReturn(Country.HUNGARY);
    when(context.getUser().getTerritory().getTotalBallsToWin()).thenReturn(TOTAL_BALLS_TO_WIN);
    when(couponRepository.countWinnersByCountry(context.getUser().getTerritory().getCountry()))
        .thenReturn(TOTAL_BALLS_TO_WIN - 1);
    //when
    boolean actualResult = rule.isWinning(context);

    //then
    assertThat(actualResult, is(true));

    verify(couponRepository, times(1))
        .countWinnersByCountry(countryArgCaptor.capture());
    assertThat(countryArgCaptor.getValue(), is(Country.HUNGARY));
  }

  @Test
  public void shouldNotWinWhenTotalBallsWon() {
    //given
    when(context.getUser().getTerritory().getCountry()).thenReturn(Country.HUNGARY);
    when(context.getUser().getTerritory().getTotalBallsToWin()).thenReturn(TOTAL_BALLS_TO_WIN);
    when(couponRepository.countWinnersByCountry(context.getUser().getTerritory().getCountry()))
        .thenReturn(TOTAL_BALLS_TO_WIN);
    //when
    boolean actualResult = rule.isWinning(context);

    //then
    assertThat(actualResult, is(false));

    verify(couponRepository, times(1))
        .countWinnersByCountry(countryArgCaptor.capture());
    assertThat(countryArgCaptor.getValue(), is(Country.HUNGARY));  }

}