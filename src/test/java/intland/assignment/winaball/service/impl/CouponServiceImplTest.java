package intland.assignment.winaball.service.impl;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import intland.assignment.winaball.dto.RedeemContext;
import intland.assignment.winaball.entity.Coupon;
import intland.assignment.winaball.entity.User;
import intland.assignment.winaball.exception.ValidationException;
import intland.assignment.winaball.repository.CouponRepository;
import intland.assignment.winaball.repository.UserRepository;
import intland.assignment.winaball.service.rule.validation.ValidationRuleProcessor;
import intland.assignment.winaball.service.rule.win.WinningRuleProcessor;
import java.time.LocalDate;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CouponServiceImplTest {
  private static final String EMAIL = "email";
  private static final String COUPON_NUMBER = "123456768790";

  @Mock
  private UserRepository userRepository;
  @Mock
  private CouponRepository couponRepository;
  @Mock
  private ValidationRuleProcessor<RedeemContext> couponValidator;
  @Mock
  private WinningRuleProcessor<RedeemContext> winningRules;
  @InjectMocks
  private CouponServiceImpl couponService;

  @Mock
  private User user;
  @Captor
  private ArgumentCaptor<RedeemContext> redeemContextCaptor;
  @Captor
  private ArgumentCaptor<Coupon> couponCaptor;


  @Test
  public void shouldThrowExceptionWhenValidationFails() throws ValidationException {
    //given
    when(userRepository.findByEmail(EMAIL)).thenReturn(user);

    ValidationException expectedException = new ValidationException("message");
    doThrow(expectedException).when(couponValidator).process(any(RedeemContext.class));

    //when
    try {
      boolean actual = couponService.redeemCoupon(EMAIL, COUPON_NUMBER);
    } catch (ValidationException actualException) {
      //then
      assertThat(actualException, is(expectedException));
    }
    //then
    verify(couponValidator, times(1)).process(redeemContextCaptor.capture());
    RedeemContext actualRedeemContext = redeemContextCaptor.getValue();
    assertThat(actualRedeemContext, hasSameAttributes(user, COUPON_NUMBER, LocalDate.now()));
    }

  @Test
  public void shouldReturnWinningRulesResultWhenValidationPasses() throws ValidationException {
    //given
    boolean expectedResult = true;
    when(userRepository.findByEmail(EMAIL)).thenReturn(user);
    doNothing().when(couponValidator).process(any(RedeemContext.class));
    when(winningRules.process(any(RedeemContext.class))).thenReturn(expectedResult);
    when(couponRepository.save(any(Coupon.class))).thenReturn(new Coupon());

    //when
    boolean actualResult = couponService.redeemCoupon(EMAIL, COUPON_NUMBER);

    //then
    assertThat(actualResult, is(expectedResult));

    verify(couponValidator, times(1)).process(redeemContextCaptor.capture());
    RedeemContext actualRedeemContext = redeemContextCaptor.getValue();
    assertThat(actualRedeemContext, hasSameAttributes(user, COUPON_NUMBER, LocalDate.now()));

    verify(winningRules, times(1)).process(redeemContextCaptor.capture());
    actualRedeemContext = redeemContextCaptor.getValue();
    assertThat(actualRedeemContext, hasSameAttributes(user, COUPON_NUMBER, LocalDate.now()));

    verify(couponRepository, times(1)).save(couponCaptor.capture());
    Coupon couponToSave = couponCaptor.getValue();
    assertThat(couponToSave.getNumber(), is(COUPON_NUMBER));
    assertThat(couponToSave.getRedeemDate(), is(LocalDate.now()));
    assertThat(couponToSave.getRedeemer(), is(user));
  }

  private static Matcher<RedeemContext> hasSameAttributes(final User user, final String couponNumber, final LocalDate date) {
    return new BaseMatcher<RedeemContext>() {
      @Override
      public boolean matches(final Object item) {
        final RedeemContext right = (RedeemContext) item;
        return StringUtils.equals(right.getCouponNumber(), couponNumber)
          && Objects.equals(right.getUser(), user)
          && Objects.equals(right.getDate(), date);
      }
      @Override
      public void describeTo(final Description description) {
      }
    };
  }


}