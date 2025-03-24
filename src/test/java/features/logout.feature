@Regression @Logout
Feature: Logging Out and Verifying Session End

  Scenario: Logout and verify session end
    Given User is logged in with valid credentials
      | username | password |
      | afroz51     | Afroz@8143 |
    When User clicks on "Sign Out" from "My Account"
    Then User should be logged out successfully
    And User should be redirected to the homepage
