Feature: File Processing - Outgoing File Trigger

  Background:
    Given User navigates to Url
    When user enters email "superadmin"
    And user enters password "Password@123"
    And user clicks login button
    When user navigates to File Processing page

  # -------------------- Page Verification --------------------

  @smoke @positive
  Scenario: Verify Outgoing File page is opened by default
    Then Outgoing File header should be displayed

  # -------------------- Positive Test Cases --------------------

  @positive
  Scenario: Trigger outgoing file including today

    And user selects outgoing file transaction type "Settlement"
    And user selects "Include Today"
    And user clicks Trigger Now
    Then trigger should be initiated successfully

  @positive
  Scenario: Trigger outgoing file excluding today

    And user selects outgoing file transaction type "Settlement"
    And user selects "Exclude Today"
    And user clicks Trigger Now
    Then trigger should be initiated successfully

  @positive
  Scenario: Trigger outgoing file by selecting transaction type and triggering now

    When user clicks Outgoing File tab
    And user selects outgoing file transaction type "Settlement"
    And user clicks Trigger Now
    Then trigger should be initiated successfully

  # -------------------- Negative Test Cases --------------------

  @negative
  Scenario: Validate Trigger Now without selecting transaction type

    And user selects "Include Today"
    And user clicks Trigger Now
    Then validation message should be displayed for transaction type

  @negative
  Scenario: Validate Trigger Now without selecting Include/Exclude today option

    And user selects outgoing file transaction type "Settlement"
    And user clicks Trigger Now
    Then validation message should be displayed for today selection

  @negative
  Scenario: Validate Trigger Now without selecting transaction type and today option
    When user clicks Outgoing File tab
    And user clicks Trigger Now
    Then validation message should be displayed for transaction type
    And validation message should be displayed for today selection