package intland.assignment.winaball.web;

import intland.assignment.winaball.entity.Country;
import intland.assignment.winaball.entity.Territory;
import intland.assignment.winaball.entity.User;
import intland.assignment.winaball.exception.ValidationException;
import intland.assignment.winaball.repository.CouponRepository.CouponView;
import intland.assignment.winaball.repository.TerritoryRepository;
import intland.assignment.winaball.service.CouponService;
import intland.assignment.winaball.service.UserService;
import intland.assignment.winaball.web.dto.CouponDTO;
import intland.assignment.winaball.web.dto.RegistrationDTO;
import intland.assignment.winaball.web.dto.TerritoryDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/winaball")
public class MainController {

	@Autowired
	private UserService userService;
	@Autowired
	private TerritoryRepository territoryRepository;
	@Autowired
	private CouponService couponService;

	@PostMapping(path="/territory")
	public @ResponseBody Territory registerTerritory(@RequestBody TerritoryDTO dto) {
		return territoryRepository.save(new Territory(dto.getCountry(), dto.getDailyBallsToWin(), dto.getTotalBallsToWin(), dto.getWhichRedeemWins()));
	}

	@PostMapping(path="/user")
	public @ResponseBody User registerUser (@RequestBody RegistrationDTO registration) throws ValidationException {
		return userService.registerUser(registration.getEmail(),
				registration.getDob(), registration.getCountry());
	}

	@PostMapping(path="/coupon")
	public @ResponseBody  boolean redeemCoupon(@RequestBody CouponDTO dto) throws ValidationException {
		return couponService.redeemCoupon(dto.getEmail(), dto.getNumber());
	}

	@GetMapping(path="/coupon/winners")
	public @ResponseBody List<CouponView> findWinnersByCountryAndDates(
			@RequestParam Country country,
			@RequestParam Optional<LocalDate> from,
			@RequestParam Optional<LocalDate> to,
			@RequestParam int pageNumber,
			@RequestParam int pageSize) {
		return couponService.findWinnerCouponsByCountry(country, from, to, PageRequest.of(pageNumber, pageSize));
	}

}
