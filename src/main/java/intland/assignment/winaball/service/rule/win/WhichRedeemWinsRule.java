package intland.assignment.winaball.service.rule.win;

import intland.assignment.winaball.dto.RedeemContext;
import intland.assignment.winaball.repository.CouponRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhichRedeemWinsRule implements WinningRule<RedeemContext> {
  private final static Logger LOG = LoggerFactory.getLogger(TotalBallsRule.class);

  private final CouponRepository couponRepository;

  public WhichRedeemWinsRule(CouponRepository couponRepository) {
    this.couponRepository = couponRepository;
  }

  @Override
  public boolean isWinning(RedeemContext context) {
    int countRedeemsByCountry = couponRepository.countRedeemsByCountry(context.getUser().getTerritory().getCountry());
    boolean retVal = (countRedeemsByCountry + 1) % context.getUser().getTerritory().getWhichRedeemWins() == 0;
    LOG.debug("returning value {}", retVal);
    return retVal;
  }
}
