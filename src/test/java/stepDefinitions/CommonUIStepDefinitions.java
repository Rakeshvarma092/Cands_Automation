package stepDefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.testng.Assert;
import pageComponents.CorePage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static stepDefinitions.ApplicationHooks.test;

public class CommonUIStepDefinitions extends CorePage {

    public HashMap<String, String> testData = new HashMap();
    public HashMap<Object, Object> loginData = new HashMap();

    public HashMap<Object, Object> organizationData = new HashMap();
    public static HashMap<String, String> customerData = new HashMap<>();
    public static HashMap<String, String> officeData = new HashMap<>();
    public static HashMap<String, String> presentAddressData = new HashMap<>();
    public static HashMap<String, String> cardData = new HashMap<>();
    public static HashMap<String, String> applicationData = new HashMap<>();

    public CommonUIStepDefinitions() throws IOException, InvalidFormatException {
    }

    //************************************************ LOGIN *********************************************************//

    @Given("User navigates to Url")
    public void userNavigatesToURL() {
        navigateURL(configReader.getUrlDetails());
    }

    @Then("login button should be visible")
    public void login_button_should_be_visible() {
        Assert.assertTrue(loginPage.isLoginButtonDisplayed(), "Login button is not visible");
    }

    @When("user enters email {string}")
    public void user_enters_email(String email) {
        loginPage.enterEmail(email);
    }

    @When("user enters password {string}")
    public void user_enters_password(String password) {
        loginPage.enterPassword(password);
    }

    @When("user clicks login button")
    public void user_clicks_login_button() {
        loginPage.clickLoginButton();
    }

    @When("user logs in with email {string} and password {string}")
    public void user_logs_in_with_email_and_password(String email, String password) {
        loginPage.loginAs(email, password);
    }

    @Then("user should be navigated to home page with title containing {string}")
    public void user_should_be_navigated_to_home_page_with_title_containing(String expectedTitlePart) {
        Assert.assertTrue(
                driver.getTitle().contains(expectedTitlePart),
                "Expected title to contain: " + expectedTitlePart + " but was: " + driver.getTitle()
        );
    }

//    @Then("Verify error message {string} is displayed")
//    public void verifyErrorMessageIsDisplayed(String expectedError) {
//        try {
//            String actualError = loginPage.getErrorMessage();
//            Assert.assertEquals(actualError.trim(), expectedError.trim(), "Error message mismatch!");
//            test.pass("Error message verified: " + actualError);
//        } catch (Exception e) {
//            test.fail("Failed to verify error message: " + e.getMessage());
//            Assert.fail("Failed to verify error message: " + e.getMessage());
//        }
//    }


    // ******************************************** VERIFY PAGES *****************************************************//

    @Then("Verify {string} page is visible")
    public void verifyPageIsVisible(String pageName) {
        try {
            switch (pageName.toUpperCase()) {
                case "LOGIN":
                    loginPage.navigateToLoginPage();
                    break;
                default:
                    throw new IllegalArgumentException("Invalid Page Name Supplied: " + pageName);
            }
            test.pass(pageName + "-Page Name Verified successfully");
        } catch (Exception e) {
            test.fail("Failed to load page: " + pageName + " -" + e.getClass().getSimpleName());
            Assert.fail("Failed to load page: " + pageName + " -" + e.getClass().getSimpleName());
        }
    }
//================================================== DASHBOARD PAGE ==================================================//

    @And("Verify profile name {string} is visible")
    public void verifyProfileNameIsVisible() {
        dashboardPage.getProfileName();
    }

    @And("Verify side navigation icons are displayed")
    public void verifySideNavigationIconsAreDisplayed() {
    }

//================================================= TRANSACTION PAGE ===================================================

    @When("User navigates to Global Search page")
    public void navigateToPage() {
        transactionPage.openGlobalSearchPage();
    }

    @Then("Global Search page should load successfully")
    public void verifyPageLoad() {
        Assert.assertTrue(transactionPage.isPageLoaded());
    }

    @When("User selects {string} tab")
    public void selectTab(String tab) {
        transactionPage.selectTab(tab);
    }

    @When("User enters valid Transaction Reference Number {string}")
    public void enterValidRef(String ref) {
        transactionPage.enterTxRef(ref);
    }

    @When("User enters invalid Transaction Reference Number {string}")
    public void enterInvalidRef(String ref) {
        transactionPage.enterTxRef(ref);
    }

    @When("User enters Card Last 4 Digits {string}")
    public void enterCard(String value) {
        transactionPage.enterCardLast4(value);
    }

    @When("User enters ARN {string}")
    public void enterArn(String value) {
        transactionPage.enterARN(value);
    }

    @When("User enters Merchant ID {string}")
    public void enterMID(String value) {
        transactionPage.enterMerchantID(value);
    }

    @When("User enters Transaction Amount {string}")
    public void enterAmount(String value) {
        transactionPage.enterAmount(value);
    }

    @When("User clicks Search")
    public void clickSearch() {
        transactionPage.clickSearch();
    }

    @When("User clicks Clear")
    public void clickClear() {
        transactionPage.clickClear();
    }

    @Then("Transaction records should be displayed")
    public void verifyRecords() {
        Assert.assertTrue(transactionPage.isTransactionDisplayed());
    }

    @Then("No records found message should be displayed")
    public void verifyNoRecords() {
        Assert.assertTrue(transactionPage.isNoRecordMessageDisplayed());
    }

    @Then("UPI, Cards, Netbanking and Fee & Others tabs should be visible")
    public void verifyAllTabsVisible() {
        Assert.assertTrue(transactionPage.areAllTabsVisible());
    }

    @Then("All fields should be cleared")
    public void allFieldsShouldBeCleared() {
        Assert.assertTrue(transactionPage.areAllFieldsCleared(),
                "Fields are not cleared properly");
    }

    @Then("{string} tab should be active")
    public void tabShouldBeActive(String tabName) {
        Assert.assertTrue(transactionPage.isTabActive(tabName),
                tabName + " tab is not active");
    }

//    @And("User selects valid transaction date range")
//    public void userSelectsValidTransactionDateRange() {
//        transactionPage.selectValidDateRange();
//    }
//
//    @And("User selects future transaction date range")
//    public void userSelectsFutureTransactionDateRange() {
//        transactionPage.selectFutureDateRange();
//    }

    @And("User selects Scheme Status {string}")
    public void userSelectsSchemeStatus(String value) {
        transactionPage.selectSchemeStatus(value);
    }

    @And("User selects Settlement Status {string}")
    public void userSelectsSettlementStatus(String value) {
        transactionPage.selectSettlementStatus(value);
    }

    @And("User selects Transaction Type {string}")
    public void userSelectsTransactionType(String value) {
        transactionPage.selectTransactionType(value);
    }

    @Then("Validation error should be displayed for Transaction Amount")
    public void validationErrorShouldBeDisplayedForTransactionAmount() {
        Assert.assertTrue(transactionPage.isTransactionAmountValidationDisplayed());
    }

    @And("User clicks Download")
    public void userClicksDownload() {
        transactionPage.clickDownload();
    }

    @Then("Report should be downloaded successfully")
    public void reportShouldBeDownloadedSuccessfully() {

        // Example: Verify file in Downloads folder
        File folder = new File(System.getProperty("user.home") + "/Downloads");

        File[] files = folder.listFiles((dir, name) ->
                name.contains("Transaction") && name.endsWith(".csv"));

        Assert.assertTrue(files != null && files.length > 0, "Download failed");
    }

    @Then("All dropdowns should reset to default")
    public void allDropdownsShouldResetToDefault() {
        Assert.assertTrue(transactionPage.areDropdownsReset());
    }

    @And("User clicks Back")
    public void userClicksBack() {
        transactionPage.clickBack();
    }

    @Then("User should navigate to previous page")
    public void userShouldNavigateToPreviousPage() {
        Assert.assertTrue(transactionPage.isNavigatedBack());
    }

    @Then("Page should load results without crashing")
    public void pageShouldLoadResultsWithoutCrashing() {
        Assert.assertTrue(transactionPage.isPageStable());
    }

    @Then("System should return filtered transaction records")
    public void systemShouldReturnFilteredTransactionRecords() {
        Assert.assertTrue(transactionPage.isTransactionDisplayed());
    }

    @And("User selects Life Cycle Status {string}")
    public void userSelectsLifeCycleStatus(String value) {
        transactionPage.selectLifeCycleStatus(value);
    }
}