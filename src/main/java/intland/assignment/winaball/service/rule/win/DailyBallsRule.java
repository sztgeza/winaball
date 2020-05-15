package intland.assignment.winaball.service.rule.win;

import intland.assignment.winaball.dto.RedeemContext;
import intland.assignment.winaball.repository.CouponRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DailyBallsRule implements WinningRule<RedeemContext> {
  private final static Logger LOG = LoggerFactory.getLogger(DailyBallsRule.class);

  private final CouponRepository couponRepository;

  public DailyBallsRule(CouponRepository couponRepository) {
    this.couponRepository = couponRepository;
  }

  @Override
  public boolean isWinning(RedeemContext context) {
    Integer winnersByCountryAndDay = couponRepository.countWinnersByCountryAndDay(
        context.getUser().getTerritory().getCountry(),
        context.getDate());
    boolean retVal = winnersByCountryAndDay < context.getUser().getTerritory().getDailyBallsToWin();
    LOG.debug("Returning value {}", retVal);
    return retVal;
  }
}
