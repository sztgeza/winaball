package intland.assignment.winaball.service.impl;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import intland.assignment.winaball.dto.RegistrationInput;
import intland.assignment.winaball.entity.Country;
import intland.assignment.winaball.entity.Territory;
import intland.assignment.winaball.entity.User;
import intland.assignment.winaball.exception.ValidationException;
import intland.assignment.winaball.repository.TerritoryRepository;
import intland.assignment.winaball.repository.UserRepository;
import intland.assignment.winaball.service.rule.validation.ValidationRuleProcessor;
import java.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UserServiceImplTest {
  private static final String EMAIL = "email";
  private static final LocalDate DOB = LocalDate.now();
  private static final Country COUNTRY = Country.HUNGARY;

  @Mock
  private UserRepository userRepository;
  @Mock
  private TerritoryRepository territoryRepository;
  @Mock
  private ValidationRuleProcessor<RegistrationInput> validation;
  @Mock
  private RegistrationInput registrationInput;

  @InjectMocks
  private UserServiceImpl userService;

  @Mock
  private User expectedUser;
  @Mock
  private Territory expectedTerritory;

  @Captor
  private ArgumentCaptor<RegistrationInput> registrationInputCaptor;
  @Captor
  private ArgumentCaptor<User> userCaptor;

  @Test
  public void shouldThrowExceptionWhenValidationFails() throws ValidationException {
    //given
    ValidationException expectedException = new ValidationException("exception");
    doThrow(expectedException).when(validation).process(any(RegistrationInput.class));

    //when
    try {
      User actual = userService.registerUser(EMAIL, DOB, COUNTRY);
    } catch (ValidationException actualException) {
      //then
      assertThat(actualException, is(expectedException));
    }
    //then
    verify(validation, times(1)).process(registrationInputCaptor.capture());
    RegistrationInput actualRegistrationInput = registrationInputCaptor.getValue();
    assertThat(actualRegistrationInput.getEmail(), is(EMAIL));
    assertThat(actualRegistrationInput.getDob(), is(DOB));
  }

  @Test
  public void shouldReturnRegisteredUserWhenValidationPasses() throws ValidationException {
    //given
    doNothing().when(validation).process(registrationInput);
    when(territoryRepository.findByCountry(COUNTRY)).thenReturn(expectedTerritory);
    when(userRepository.save(any(User.class))).thenReturn(expectedUser);

    //when
    User actual = userService.registerUser(EMAIL, DOB, COUNTRY);

    //then
    assertThat(actual, is(expectedUser));

    verify(territoryRepository, times(1)).findByCountry(COUNTRY);
    verify(validation, times(1)).process(registrationInputCaptor.capture());
    RegistrationInput actualRegistrationInput = registrationInputCaptor.getValue();
    assertThat(actualRegistrationInput.getEmail(), is(EMAIL));
    assertThat(actualRegistrationInput.getDob(), is(DOB));

    verify(userRepository, times(1)).save(userCaptor.capture());
    User userArgument = userCaptor.getValue();
    assertThat(userArgument.getEmail(), is(EMAIL));
    assertThat(userArgument.getDob(), is(DOB));
    assertThat(userArgument.getTerritory(), is(expectedTerritory));
  }
}