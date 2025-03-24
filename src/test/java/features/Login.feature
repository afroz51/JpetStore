Feature: User Login

    @invalid1
    Scenario: Login with invalid data table details
        Given User navigates to the login page
        When User enters invalid data table login details
        | username | password    |
        | afroz 	 | wrongPass   |
        | afroz 	 | wrongPass   |
        Then Error message should be displayed

    @valid1
    Scenario: Login with valid data table details
        Given User navigates to the login page
        When User enters valid data table login details
        | username | password    |
        | afroz23  | Afroz@8143  |
        Then User should be logged in successfully

    @invalid2
    Scenario: Login with invalid excel details
        Given User navigates to the login page
        When User enters invalid excel login details
        Then Error message should be displayed

    @valid2
    Scenario: Login with valid properties details
        Given User navigates to the login page
        When User enters valid properties login details
        Then User should be logged in successfully
