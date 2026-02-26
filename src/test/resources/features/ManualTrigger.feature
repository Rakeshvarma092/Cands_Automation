Feature: File Processing - Manual Trigger

  Background:
    Given User navigates to Url
    When user enters email "superadmin"
    And user enters password "Password@123"
    And user clicks login button
    When user navigates to File Processing page

  # ------------------- Page & Popup Verification -------------------

  @smoke @positive
  Scenario: Verify Manual Trigger page is displayed
    When user clicks Manual Trigger
    Then Manual Trigger page should be displayed

  @smoke @positive
  Scenario: Verify Confirm Trigger popup is displayed when Trigger Now is clicked
    When user clicks Manual Trigger
    Then Manual Trigger page should be displayed
    When user selects file type "Outgoing File"
    And user clicks Trigger Now
    Then Confirm Trigger popup should be displayed

  # ------------------- Positive Test Cases -------------------

  @positive
  Scenario: Trigger manual file successfully by confirming Yes

    When user clicks Manual Trigger
    Then Manual Trigger page should be displayed
    When user selects file type "Outgoing File"
    And user clicks Trigger Now
    Then Confirm Trigger popup should be displayed
    When user confirms trigger by clicking Yes
#    Then trigger should be initiated successfully

  @positive
  Scenario: Cancel trigger from Confirm Trigger popup

    When user clicks Manual Trigger
    Then Manual Trigger page should be displayed
    When user selects file type "Outgoing File"
    And user clicks Trigger Now
    Then Confirm Trigger popup should be displayed
    When user cancels trigger by clicking Cancel
    Then Confirm Trigger popup should be closed

  # ------------------- Negative Test Cases -------------------

  @negative
  Scenario: Trigger Now without selecting File Type should show validation

    When user clicks Manual Trigger
    Then Manual Trigger page should be displayed
    When user clicks Trigger Now

  @negative
  Scenario: Confirm popup should not appear when File Type is not selected

    When user clicks Manual Trigger
    Then Manual Trigger page should be displayed
    When user clicks Trigger Now
    Then Confirm Trigger popup should not be displayed