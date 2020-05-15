package intland.assignment.winaball.web.dto;

import intland.assignment.winaball.entity.Country;
import java.time.LocalDate;

public class RegistrationDTO {
  private String email;
  private LocalDate dob;
  private Country country;

  public RegistrationDTO() {}

  public RegistrationDTO(String email, LocalDate dob,
      Country country) {
    this.email = email;
    this.dob = dob;
    this.country = country;
  }

  public String getEmail() {
    return email;
  }

  public LocalDate getDob() {
    return dob;
  }

  public Country getCountry() {
    return country;
  }
}
