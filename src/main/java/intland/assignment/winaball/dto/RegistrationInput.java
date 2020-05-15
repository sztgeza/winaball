package intland.assignment.winaball.dto;

import intland.assignment.winaball.entity.Territory;
import java.time.LocalDate;

public class RegistrationInput {
  private String email;
  private LocalDate dob;
  private Territory territory;

  public RegistrationInput(String email, LocalDate dob,
      Territory territory) {
    this.email = email;
    this.dob = dob;
    this.territory = territory;
  }

  public String getEmail() {
    return email;
  }

  public LocalDate getDob() {
    return dob;
  }

  public Territory getTerritory() {
    return territory;
  }
}
