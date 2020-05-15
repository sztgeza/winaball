package intland.assignment.winaball.service.rule.win;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import intland.assignment.winaball.dto.RedeemContext;
import intland.assignment.winaball.entity.Country;
import intland.assignment.winaball.repository.CouponRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WhichRedeemWinsRuleTest {
  private static final int WHICH_REDEEMS_WIN = 80;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private RedeemContext context;
  @Mock
  private CouponRepository couponRepository;

  @InjectMocks
  private WhichRedeemWinsRule rule;

  @Captor
  private ArgumentCaptor<Country> countryArgCaptor;

  @Test
  public void shouldWinWhenModIsZero() {
    //given
    when(context.getUser().getTerritory().getCountry()).thenReturn(Country.HUNGARY);
    when(context.getUser().getTerritory().getWhichRedeemWins()).thenReturn(WHICH_REDEEMS_WIN);
    when(couponRepository.countRedeemsByCountry(context.getUser().getTerritory().getCountry())).thenReturn(20 * WHICH_REDEEMS_WIN - 1);
    //when
    boolean actualResult = rule.isWinning(context);

    //then
    assertThat(actualResult, is(true));

    verify(couponRepository, times(1)).countRedeemsByCountry(countryArgCaptor.capture());
    assertThat(countryArgCaptor.getValue(), is(Country.HUNGARY));
  }


  @Test
  public void shouldNotWinWhenModIsNotZero() {
    //given
    when(context.getUser().getTerritory().getCountry()).thenReturn(Country.HUNGARY);
    when(context.getUser().getTerritory().getWhichRedeemWins()).thenReturn(WHICH_REDEEMS_WIN);
    when(couponRepository.countRedeemsByCountry(context.getUser().getTerritory().getCountry()))
        .thenReturn(20 * WHICH_REDEEMS_WIN);
    //when
    boolean actualResult = rule.isWinning(context);

    //then
    assertThat(actualResult, is(false));

    verify(couponRepository, times(1)).countRedeemsByCountry(countryArgCaptor.capture());
    assertThat(countryArgCaptor.getValue(), is(Country.HUNGARY));
  }




  /*
   Integer countRedeemsByCountry = couponRepository.countRedeemsByCountry(context.getUser().getTerritory().getCountry());
    return countRedeemsByCountry > 0
        && countRedeemsByCountry % context.getUser().getTerritory().getWhichRedeemWins() == 0;
   */
}