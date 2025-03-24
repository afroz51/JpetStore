Feature: User Registration

    @invalid1
    Scenario: Register with invalid data table data details
        Given User navigates to the registration page
        When User enters invalid data table data registration details
        | username | password | repeatedPassword | firstName | lastName | email          | phone    | address1 | address2 | city | state | zip   | country | language | favoriteCategory | enableMyList | enableMyBanner |
        | john1    | 1234     | 1244             | john      | doe      | john@net.com   | main     |    kp    | KP       |  123 |   123 |  111  | 123     |  English | cats             | false         | true           |
        Then Error message should appear for missing details
    
    @valid1
    Scenario: Register with valid data table data details
        Given User navigates to the registration page
        When User enters valid data table data registration details
        | username | password    | repeatedPassword | firstName | lastName | email             | phone    | address1  | address2 | city      | state | zip   | country | language | favoriteCategory | enableMyList | enableMyBanner |
        | afroz233   | Afroz@8143   | Afroz@8143        | Afroz      | Shaik      | Afroz@example.com | 9876543120  | Main St | Apt 4B  | Banglore  | Karnataka    | 12345 | india     | english  | Dogs            | true         | true           |
        Then User should be registered successfully

    @invalid2
    Scenario: Register with invalid excel details
        Given User navigates to the registration page
        When User enters invalid excel registration details
        Then Error message should appear for missing details
    
    @valid2
    Scenario: Register with valid properties details
        Given User navigates to the registration page
        When User enters valid properties registration details
        Then User should be registered successfully