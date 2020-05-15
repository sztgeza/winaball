package intland.assignment.winaball.web.dto;

public class CouponDTO {
  private String number;
  private String email;

  public CouponDTO() {
  }

  public CouponDTO(String number, String email) {
    this.number = number;
    this.email = email;
  }

  public String getNumber() {
    return number;
  }

  public String getEmail() {
    return email;
  }
}
