package intland.assignment.winaball.service.impl;

import intland.assignment.winaball.dto.RegistrationInput;
import intland.assignment.winaball.entity.Country;
import intland.assignment.winaball.entity.Territory;
import intland.assignment.winaball.entity.User;
import intland.assignment.winaball.exception.ValidationException;
import intland.assignment.winaball.repository.TerritoryRepository;
import intland.assignment.winaball.repository.UserRepository;
import intland.assignment.winaball.service.UserService;
import intland.assignment.winaball.service.rule.validation.ValidationRuleProcessor;
import java.time.LocalDate;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final TerritoryRepository territoryRepository;
  private final ValidationRuleProcessor<RegistrationInput> registrationValidator;

  public UserServiceImpl(UserRepository userRepository, TerritoryRepository territoryRepository,
      ValidationRuleProcessor<RegistrationInput> registrationValidator) {
    this.userRepository = userRepository;
    this.territoryRepository = territoryRepository;
    this.registrationValidator = registrationValidator;
  }

  @Override
  @Transactional
  public User registerUser(String email, LocalDate dob,
      Country country) throws ValidationException {
    Territory territory = territoryRepository.findByCountry(country);
    RegistrationInput input = new RegistrationInput(email, dob, territory);
    registrationValidator.process(input);
    return userRepository.save(new User(input.getEmail(), input.getDob(), territory));
  }

}
