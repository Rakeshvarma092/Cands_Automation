@GlobalSearch @Regression
Feature: Global Search Functionality

  As a user of the application
  I want to be able to search and filter transactions in the Global Search transaction page

  Background:
    Given User navigates to Url
    When user enters email "superadmin"
    And user enters password "Password@123"
    And user clicks login button
    When User navigates to Global Search page
    Then Global Search page should load successfully

  @Browser @Branch11
  Scenario: Verify tabs are displayed
    Then UPI, Cards, Netbanking and Fee & Others tabs should be visible

  @Browser @Branch12
  Scenario: Verify default active tab
    Then "Cards" tab should be active by default

  @Browser @Branch13
    Scenario: Click on each tab
    When User clicks on "UPI" tab
    When User clicks on "Cards" tab
    When User clicks on "Netbanking" tab
    When User clicks on "Fee & Others" tab

  @Browser @Branch14
  Scenario: Search using valid Transaction Reference Number
    When User selects "Cards" tab
    And User click on filter
    And User enters valid Transaction Reference Number "TXN123456"
    And User clicks Search
    Then Transaction records should be displayed

  @Browser @Branch15
  Scenario: Search using valid Transaction Reference Number
    When User selects "UPI" tab
    And User click on filter
    And User enters valid Transaction Reference Number "TXN123456"
    And User clicks Search
    Then Transaction records should be displayed

  @Browser @Branch16
  Scenario: Search using valid Transaction Reference Number
    When User selects "Netbanking" tab
    And User click on filter
    And User enters valid Transaction Reference Number "TXN123456"
    And User clicks Search
    Then Transaction records should be displayed

  @Browser @Branch17
  Scenario: Search using valid Transaction Reference Number
    When User selects "Fee & Others" tab
    And User click on filter
    And User enters valid Transaction Reference Number "TXN123456"
    And User clicks Search
    Then Transaction records should be displayed

  @Browser @Branch18
  Scenario: Search using invalid Transaction Reference Number
    When User selects "Cards" tab
    And User click on filter
    And User enters invalid Transaction Reference Number "INVALID999"
    And User clicks Search
    Then No records found message should be displayed

  @Browser @Branch19
  Scenario: Search using invalid Transaction Reference Number
    When User selects "UPI" tab
    And User click on filter
    And User enters invalid Transaction Reference Number "INVALID999"
    And User clicks Search
    Then No records found message should be displayed

  @Browser @Branch20
  Scenario: Search using invalid Transaction Reference Number
    When User selects "Netbanking" tab
    And User click on filter
    And User enters invalid Transaction Reference Number "INVALID999"
    And User clicks Search
    Then No records found message should be displayed

  @Browser @Branch21
  Scenario: Search using invalid Transaction Reference Number
    When User selects "Fee & Others" tab
    And User click on filter
    And User enters invalid Transaction Reference Number "INVALID999"
    And User clicks Search
    Then No records found message should be displayed

  @Browser @Branch22
  Scenario: Search using multiple valid filters
    When User selects "Cards" tab
    And User click on filter
    And User enters Card Last 4 Digits "1234"
    And User enters ARN "ARN123456"
    And User enters Merchant ID "MID001"
    And User enters Transaction Amount "5000"
    And User clicks Search
    Then Transaction records should be displayed

  @Browser @Branch23
  Scenario: Search using multiple valid filters
    When User selects "UPI" tab
    And User click on filter
    And User enters Card Last 4 Digits "1234"
    And User enters ARN "ARN123456"
    And User enters Merchant ID "MID001"
    And User enters Transaction Amount "5000"
    And User clicks Search
    Then Transaction records should be displayed

  @Browser @Branch24
  Scenario: Search using multiple valid filters
    When User selects "Netbanking" tab
    And User click on filter
    And User enters Card Last 4 Digits "1234"
    And User enters ARN "ARN123456"
    And User enters Merchant ID "MID001"
    And User enters Transaction Amount "5000"
    And User clicks Search
    Then Transaction records should be displayed

  @Browser @Branch25
  Scenario: Search using multiple valid filters
    When User selects "Fee & Others" tab
    And User click on filter
    And User enters Card Last 4 Digits "1234"
    And User enters ARN "ARN123456"
    And User enters Merchant ID "MID001"
    And User enters Transaction Amount "5000"
    And User clicks Search
    Then Transaction records should be displayed

  @Browser @Branch26
  Scenario: Clear button clears all fields
    When User selects "Cards" tab
    And User click on filter
    And User enters Card Last 4 Digits "1234"
    And User clicks Clear
    Then All fields should be cleared

  @regression
  Scenario Outline: Verify tab switching
    When User selects "<Tab>" tab
    Then "<Tab>" tab should be active

    Examples:
      | Tab          |
      | UPI          |
      | Cards        |
      | Netbanking   |
      | Fee & Others |

     # ========================= UI VALIDATION =========================

  @ui
  Scenario: Verify all filter fields are visible in Cards tab
    When User selects "Cards" tab
    Then All Cards filter fields should be displayed


  # ========================= EMPTY SEARCH =========================

  @negative
  Scenario: Click Search without entering any filter
    When User selects "Cards" tab
    And User clicks Search
    Then Either transactions should be displayed or validation message should appear


  # ========================= INVALID DATA FORMAT =========================

  @negative
  Scenario: Enter alphabets in Card Last 4 Digits
    When User selects "Cards" tab
    And User enters Card Last 4 Digits "ABCD"
    And User clicks Search
    Then Validation error should be displayed for Card Number


  @negative
  Scenario: Enter special characters in Merchant ID
    When User selects "Cards" tab
    And User enters Merchant ID "@@@###"
    And User clicks Search
    Then No records found message should be displayed


  @security
  Scenario: Enter SQL injection string in Transaction Reference
    When User selects "Cards" tab
    And User enters valid Transaction Reference Number "' OR 1=1 --"
    And User clicks Search
    And No records found message should be displayed


  # ========================= DATE RANGE FILTER =========================

  @positive
  Scenario: Search transactions using valid date range
    When User selects "Cards" tab
    And User selects valid transaction date range
    And User clicks Search
    Then Transaction records should be displayed


  @negative
  Scenario: Search using future date range
    When User selects "Cards" tab
    And User selects future transaction date range
    And User clicks Search
    Then No records found message should be displayed


  # ========================= DROPDOWN FILTERS =========================

  @positive
  Scenario: Search using Scheme Status dropdown
    When User selects "Cards" tab
    And User selects Scheme Status "Settled"
    And User clicks Search
    Then Transaction records should be displayed


  @positive
  Scenario: Search using Settlement Status and Transaction Type
    When User selects "Cards" tab
    And User selects Settlement Status "Success"
    And User selects Transaction Type "Purchase"
    And User clicks Search
    Then Transaction records should be displayed


  @negative
  Scenario: Select incompatible filter combination
    When User selects "Cards" tab
    And User selects Settlement Status "Failed"
    And User enters Transaction Amount "99999999"
    And User clicks Search
    Then No records found message should be displayed


  # ========================= AMOUNT VALIDATION =========================

  @boundary
  Scenario: Search using zero transaction amount
    When User selects "Cards" tab
    And User enters Transaction Amount "0"
    And User clicks Search
    Then Validation error should be displayed for Transaction Amount


  @boundary
  Scenario: Search using very large transaction amount
    When User selects "Cards" tab
    And User enters Transaction Amount "9999999999"
    And User clicks Search
    Then No records found message should be displayed


  # ========================= DOWNLOAD =========================

  @negative
  Scenario: Click Download without searching
    When User selects "Cards" tab
    And User clicks Download


  @positive
  Scenario: Download report after successful search
    When User selects "Cards" tab
    And User enters valid Transaction Reference Number "TXN123456"
    And User clicks Search
    And Transaction records should be displayed
    When User clicks Download
    Then Report should be downloaded successfully


  # ========================= CLEAR FUNCTIONALITY =========================

  @regression
  Scenario: Clear button clears dropdown selections
    When User selects "Cards" tab
    And User selects Scheme Status "Settled"
    And User clicks Clear
    Then All dropdowns should reset to default


  # ========================= BACK BUTTON =========================

  @navigation
  Scenario: Verify Back button navigation
    When User selects "Cards" tab
    And User clicks Back
    Then User should navigate to previous page


  # ========================= CROSS TAB VALIDATION =========================

  @regression
  Scenario Outline: Search in different tabs
    When User selects "<Tab>" tab
    And User clicks Search
    Then Page should load results without crashing

    Examples:
      | Tab          |
      | UPI          |
      | Cards        |
      | Netbanking   |
      | Fee & Others |


  # ========================= MULTI FILTER COMBINATION =========================

  @regression
  Scenario: Search using multiple combined filters
    When User selects "Cards" tab
    And User enters Card Last 4 Digits "1234"
    And User enters ARN "ARN12345"
    And User selects Scheme Status "Settled"
    And User selects Settlement Status "Success"
    And User enters Transaction Amount "1000"
    And User clicks Search
    Then System should return filtered transaction records


  # ========================= LIFE CYCLE STATUS =========================

  @positive
  Scenario: Search using Life Cycle Status filter
    When User selects "Cards" tab
    And User selects Life Cycle Status "Completed"
    And User clicks Search
    Then Transaction records should be displayed