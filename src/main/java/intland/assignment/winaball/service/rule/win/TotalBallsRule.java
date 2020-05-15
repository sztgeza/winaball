package intland.assignment.winaball.service.rule.win;

import intland.assignment.winaball.dto.RedeemContext;
import intland.assignment.winaball.repository.CouponRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TotalBallsRule implements WinningRule<RedeemContext> {
  private final static Logger LOG = LoggerFactory.getLogger(TotalBallsRule.class);


  private final CouponRepository couponRepository;

  public TotalBallsRule(CouponRepository couponRepository) {
    this.couponRepository = couponRepository;
  }

  @Override
  public boolean isWinning(RedeemContext context) {
    boolean retVal = couponRepository.countWinnersByCountry(context.getUser().getTerritory().getCountry()) <
        context.getUser().getTerritory().getTotalBallsToWin();
    LOG.debug("Returning value {}", retVal);
    return retVal;
  }
}
