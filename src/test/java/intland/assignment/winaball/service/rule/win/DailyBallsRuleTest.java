package intland.assignment.winaball.service.rule.win;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static  org.hamcrest.Matchers.is;

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
public class DailyBallsRuleTest {

  private static final int DAILY_BALLS_TO_WIN = 100;

  @Mock (answer = Answers.RETURNS_DEEP_STUBS)
  private RedeemContext context;
  @Mock
  private CouponRepository couponRepository;

  @InjectMocks
  private DailyBallsRule rule;

  @Captor
  private ArgumentCaptor<Country> countryArgCaptor;
  @Captor
  private ArgumentCaptor<LocalDate> dateArgCaptor;
  /*
  public boolean isWinning(RedeemContext context) {
    Long winnersByCountryAndDay = couponRepository.countWinnersByCountryAndDay(
        context.getUser().getTerritory().getCountry(),
        LocalDate.now());
    return winnersByCountryAndDay < context.getUser().getTerritory().getDailyBallsToWin();
  }*/
  @Test
  public void shouldWinWhenLessThanDailyBallsWon() {
    //given
    when(context.getUser().getTerritory().getCountry()).thenReturn(Country.HUNGARY);
    when(context.getUser().getTerritory().getDailyBallsToWin()).thenReturn(DAILY_BALLS_TO_WIN);
    when(context.getDate()).thenReturn(LocalDate.now());
    when(couponRepository.countWinnersByCountryAndDay(context.getUser().getTerritory().getCountry(),
        context.getDate())).thenReturn(DAILY_BALLS_TO_WIN - 1);
    //when
    boolean actualResult = rule.isWinning(context);

    //then
    assertThat(actualResult, is(true));

    verify(couponRepository, times(1)).countWinnersByCountryAndDay(countryArgCaptor.capture(), dateArgCaptor.capture());
    assertThat(countryArgCaptor.getValue(), is(Country.HUNGARY));
    assertThat(dateArgCaptor.getValue(), is(LocalDate.now()));
  }

  @Test
  public void shouldNotWinWhenTotalBallsWon() {
    //given
    when(context.getUser().getTerritory().getCountry()).thenReturn(Country.HUNGARY);
    when(context.getUser().getTerritory().getDailyBallsToWin()).thenReturn(DAILY_BALLS_TO_WIN);
    when(context.getDate()).thenReturn(LocalDate.now());
    when(couponRepository.countWinnersByCountryAndDay(context.getUser().getTerritory().getCountry(),
        context.getDate())).thenReturn(DAILY_BALLS_TO_WIN);

    //when
    boolean actualResult = rule.isWinning(context);

    //then
    assertThat(actualResult, is(false));

    verify(couponRepository, times(1)).countWinnersByCountryAndDay(countryArgCaptor.capture(), dateArgCaptor.capture());
    assertThat(countryArgCaptor.getValue(), is(Country.HUNGARY));
    assertThat(dateArgCaptor.getValue(), is(LocalDate.now()));
  }

}