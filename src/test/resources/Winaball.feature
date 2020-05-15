Feature: Win a ball.

  Scenario: Attempt to register a user with a not supported country
    Given a territory GERMANY
    When a user with email email_al@gmail.com and dob 1900-01-01 and country ALBANIA attempts to registrate
    Then the attempt should be invalid

  Scenario: Attempt to register a user with an invalid email
    Given a territory GERMANY
    When a user with email invalidemail and dob 1900-01-01 and country GERMANY attempts to registrate
    Then the attempt should be invalid

  Scenario: Attempt to register a user with an invalid age
    Given a territory GERMANY
    When a user with email valid_email@email.hu and dob 2020-01-01 and country GERMANY attempts to registrate
    Then the attempt should be invalid

  Scenario: Attempt to register a user with a valid email, age, territory
    Given a territory GERMANY
    When a user with email valid_email@gmail.com and dob 1900-01-01 and country GERMANY attempts to registrate
    Then the registration attempt should be valid

  Scenario: Attempt to redeem a coupon when the user is not registered.
    Given a territory GERMANY
    When a user with email email_de@gmail.com attempts to redeem a coupon with number 1234567890
    Then the attempt should be invalid

  Scenario: Attempt to redeem a coupon when the user is registered and the coupon number is invalid.
    Given a territory GERMANY
    Given a user registered with email email_de@gmail.com and territory GERMANY
    When a user with email email_de@gmail.com attempts to redeem a coupon with number 1
    Then the attempt should be invalid

  Scenario: Attempt to redeem a coupon when the user is registered and the coupon is already redeemed.
    Given a territory GERMANY
    Given a user registered with email email_de@gmail.com and territory GERMANY
    Given a coupon already redeemed with number 1234567890
    When a user with email email_de@gmail.com attempts to redeem a coupon with number 1234567890
    Then the attempt should be invalid

  Scenario: Attempt to redeem a coupon when the user is registered and the coupon number is valid and no previous redeems.
    Given a territory GERMANY
    Given a user registered with email email_de@gmail.com and territory GERMANY
    When a user with email email_de@gmail.com attempts to redeem a coupon with number 1234567890
    Then the attempt should be valid with result false

  Scenario: Attempt to redeem a coupon when the user is registered and the coupon number is valid and one is missing to win.
    Given a territory GERMANY
    Given a user registered with email email_de@gmail.com and territory GERMANY
    Given 39 coupons already redeemed today in GERMANY
    When a user with email email_de@gmail.com attempts to redeem a coupon with number 1234567890
    Then the attempt should be valid with result true

  Scenario: Attempt to redeem a coupon when the user is registered and the coupon number is valid and should win but no more balls today
    Given a territory GERMANY
    Given a user registered with email email_de@gmail.com and territory GERMANY
    Given 10039 coupons already redeemed today in GERMANY
    When a user with email email_de@gmail.com attempts to redeem a coupon with number 1234567890
    Then the attempt should be valid with result false

  Scenario: Attempt to redeem a coupon when the user is registered and the coupon number is valid and should win today but no more balls overall
    Given a territory GERMANY
    Given a user registered with email email_de@gmail.com and territory GERMANY
    Given 400000 coupons already redeemed since the beginning in GERMANY
    When a user with email email_de@gmail.com attempts to redeem a coupon with number 1234567890
    Then the attempt should be valid with result false
