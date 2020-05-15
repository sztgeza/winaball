package intland.assignment.winaball.service.rule;

import intland.assignment.winaball.dto.RedeemContext;
import intland.assignment.winaball.dto.RegistrationInput;
import intland.assignment.winaball.repository.CouponRepository;
import intland.assignment.winaball.repository.UserRepository;
import intland.assignment.winaball.service.rule.validation.ValidationRuleProcessor;
import intland.assignment.winaball.service.rule.validation.coupon.AlreadyRedeemedValidation;
import intland.assignment.winaball.service.rule.validation.coupon.NumberFormatValidation;
import intland.assignment.winaball.service.rule.validation.coupon.UserExistsValidation;
import intland.assignment.winaball.service.rule.validation.user.AgeValidation;
import intland.assignment.winaball.service.rule.validation.user.EmailFormatValidation;
import intland.assignment.winaball.service.rule.validation.user.AlreadyRegisteredValidation;
import intland.assignment.winaball.service.rule.validation.user.TerritoryValidation;
import intland.assignment.winaball.service.rule.win.DailyBallsRule;
import intland.assignment.winaball.service.rule.win.WhichRedeemWinsRule;
import intland.assignment.winaball.service.rule.win.TotalBallsRule;
import intland.assignment.winaball.service.rule.win.WinningRuleProcessor;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RuleConfiguration {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CouponRepository couponRepository;

  @Bean
  public ValidationRuleProcessor<RegistrationInput> registrationValidation() {
    return new ValidationRuleProcessor<RegistrationInput>(Arrays.asList(
        new EmailFormatValidation(),
        new TerritoryValidation(),
        new AgeValidation(),
        new AlreadyRegisteredValidation(userRepository)
    ));
  }

  @Bean
  public ValidationRuleProcessor<RedeemContext> couponValidation() {
    return new ValidationRuleProcessor<RedeemContext>(Arrays.asList(
        new NumberFormatValidation(),
        new UserExistsValidation(),
        new AlreadyRedeemedValidation(couponRepository)
    ));
  }

  @Bean
  public WinningRuleProcessor<RedeemContext> winningRules() {
    return new WinningRuleProcessor<>(Arrays.asList(
        new DailyBallsRule(couponRepository),
        new TotalBallsRule(couponRepository),
        new WhichRedeemWinsRule(couponRepository)
    ));
  }
}
