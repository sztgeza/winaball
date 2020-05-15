package intland.assignment.winaball.service;

import intland.assignment.winaball.entity.Country;
import intland.assignment.winaball.entity.User;
import intland.assignment.winaball.exception.ValidationException;
import java.time.LocalDate;

public interface UserService {
  User registerUser(String email, LocalDate dob,
      Country country) throws ValidationException;
}