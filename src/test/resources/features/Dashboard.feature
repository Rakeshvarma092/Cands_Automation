Feature: Dashboard Functionality

  Background:
    Given User navigates to Url
#    When User enters email "yeshwanth.bupathi@toucanus.com"
#    And User enters password "Password@123"
#    And User click on Login button

  @Browser @Positive
  Scenario: Verify Dashboard elements after successful login

    And Verify profile name "Superadmin" is visible
#    And Verify side navigation icons are displayed
#      | Icon         |
#      | Home         |
#      | Organization |
#      | Search       |
#      | Statistics   |
#      | Institution  |
#      | Settings     |

#  @Browser @Positive
#  Scenario: Dismiss login success notification
#    When User clicks on "Dismiss" link on notification
#    Then Verify "Login successful." notification is not visible
#
#  @Browser @Negative
#  Scenario: Direct access to Dashboard without authentication
#    Given User navigates to Dashboard URL directly
#    Then User should be redirected to the Login page
