package pageComponents;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utilities.DriverUtils;

public class TransactionPage extends DriverUtils {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public TransactionPage(WebDriver driver) {
        this.driver = driver;
        this.wait = getWait();
        PageFactory.initElements(driver, this);
    }

    // ======================== NAVIGATION ========================

    @FindBy(xpath = "(//span[@class='mat-mdc-button-touch-target'])[3]")
    private WebElement nav_GlobalSearch;

    // ====================== VERIFY PAGE  ========================
    @FindBy(xpath = "//p[contains(text(),'Global Search Transactions')]")
    private WebElement lbl_GlobalSearchTitle;

    @FindBy(xpath = "//h3[contains(text(),'Filter By')]")
    private WebElement lbl_FilterBy;

    // ======================== TABS ==============================

    @FindBy(xpath = "//span[normalize-space()='UPI']")
    private WebElement option_UPI;

    @FindBy(xpath = "//span[normalize-space()='Cards']")
    private WebElement option_Cards;

    @FindBy(xpath = "//span[normalize-space()='Netbanking']")
    private WebElement option_NetBanking;

    @FindBy(xpath = "//span[normalize-space()='Fee & Others']")
    private WebElement option_FeeAndOthers;

    // ======================== BUTTONS ========================

    @FindBy(xpath = "//button//span[normalize-space()='Download']")
    private WebElement btn_Download;

    @FindBy(xpath = "//button//span[normalize-space()='Clear']")
    private WebElement btn_Clear;

    @FindBy(xpath = "//button//span[normalize-space()='Filter']")
    private WebElement btn_Filter;

    // ======================== FILTER FIELDS =====================

    @FindBy(xpath = "//mat-form-field[.//mat-label[contains(text(),'Card Number (Last 4 Digits)')]]//input")
    private WebElement txt_CardNumber;

    @FindBy(xpath = "//input[@name='cardbin']")
    private WebElement txt_CardBin;

    @FindBy(xpath = "//input[@name='invoiceNumber']")
    private WebElement txt_InvoiceNumber;

    @FindBy(xpath = "//input[@name='txRefNumber']")
    private WebElement txt_TransactionReferenceNumber;

    @FindBy(xpath = "//input[@name='arn']")
    private WebElement txt_ARN;

    @FindBy(xpath = "//input[@name='runNumber']")
    private WebElement txt_RunNumber;

    @FindBy(xpath = "//input[@name='mid']")
    private WebElement txt_MerchantID;

    @FindBy(xpath = "//input[@name='terminalNumber']")
    private WebElement txt_TerminalNumber;

    @FindBy(xpath = "//mat-select[@name='setlSts']")
    private WebElement drp_SchemeStatus;

    @FindBy(xpath = "//input[@name='mcc']")
    private WebElement txt_MCC;

    @FindBy(xpath = "//input[@name='mName']")
    private WebElement txt_MerchantName;

    @FindBy(xpath = "//input[@name='txAmountValue']")
    private WebElement txt_TransactionAmount;

    @FindBy(xpath = "//mat-select[@name='sc']")
    private WebElement drp_SchemeCode;

    @FindBy(xpath = "//mat-select[@name='lcs']")
    private WebElement drp_LifeCycleStatus;

    @FindBy(xpath = "(//mat-select[@name='ts'])[1]")
    private WebElement drp_SettlementStatus;

    @FindBy(xpath = "(//mat-select[@name='ts'])[2]")
    private WebElement drp_TransactionType;

    @FindBy(xpath = "//mat-form-field[.//mat-label[contains(text(),'Payment Release StatusEnum')]]//mat-select")
    private WebElement drp_PaymentReleaseStatus;

    // ======================== SEARCH BUTTONS ====================

    @FindBy(xpath = "//button[@name='searchData']")
    private WebElement btn_Search;

    @FindBy(xpath = "(//button[@name='clr2'])[2]")
    private WebElement btn_Clear2;

    @FindBy(xpath = "//button[@id='back']")
    private WebElement btn_Back;

    @FindBy(xpath = "//p[contains(text(),'No records found')]")
    private WebElement msg_NoTransactions;

    @FindBy(xpath = "//table//tbody/tr")
    private List<WebElement> transactionRows;

    // ======================== ACTION METHODS ========================

    public void openGlobalSearchPage() {
        wait.until(ExpectedConditions.elementToBeClickable(nav_GlobalSearch)).click();
    }

    public boolean isPageLoaded() {
        return wait.until(ExpectedConditions.visibilityOf(lbl_GlobalSearchTitle)).isDisplayed();
    }

    public void selectTab(String tab) {
        switch (tab.toLowerCase()) {
            case "upi" -> option_UPI.click();
            case "cards" -> option_Cards.click();
            case "netbanking" -> option_NetBanking.click();
            case "fee & others" -> option_FeeAndOthers.click();
            default -> throw new IllegalArgumentException("Invalid Tab");
        }
    }

    public void enterTxRef(String value) {
        txt_TransactionReferenceNumber.clear();
        txt_TransactionReferenceNumber.sendKeys(value);
    }

    public void enterCardLast4(String value) {
        txt_CardNumber.clear();
        txt_CardNumber.sendKeys(value);
    }

    public void enterARN(String value) {
        txt_ARN.clear();
        txt_ARN.sendKeys(value);
    }

    public void enterMerchantID(String value) {
        txt_MerchantID.clear();
        txt_MerchantID.sendKeys(value);
    }

    public void enterAmount(String value) {
        txt_TransactionAmount.clear();
        txt_TransactionAmount.sendKeys(value);
    }

    public void clickSearch() {
        btn_Search.click();
        waitForResults();
    }

    public void clickClear() {
        btn_Clear.click();
    }

    public void clickDownload() {
        btn_Download.click();
    }

    public boolean isNoRecordMessageDisplayed() {
        try {
            return msg_NoTransactions.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTransactionDisplayed() {
        return transactionRows.size() > 0;
    }

    private void waitForResults() {
        wait.until(driver ->
                transactionRows.size() > 0 || msg_NoTransactions.isDisplayed());
    }

    public boolean areAllTabsVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(
                    option_UPI,
                    option_Cards,
                    option_NetBanking,
                    option_FeeAndOthers
            ));

            return true;

        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean areAllFieldsCleared() {

        return txt_CardNumber.getAttribute("value").isEmpty()
                && txt_CardBin.getAttribute("value").isEmpty()
                && txt_InvoiceNumber.getAttribute("value").isEmpty()
                && txt_TransactionReferenceNumber.getAttribute("value").isEmpty()
                && txt_ARN.getAttribute("value").isEmpty()
                && txt_RunNumber.getAttribute("value").isEmpty()
                && txt_MerchantID.getAttribute("value").isEmpty()
                && txt_TerminalNumber.getAttribute("value").isEmpty()
                && txt_MCC.getAttribute("value").isEmpty()
                && txt_MerchantName.getAttribute("value").isEmpty()
                && txt_TransactionAmount.getAttribute("value").isEmpty();

    }
    public boolean isTabActive(String tabName) {

        WebElement activeTab;

        switch (tabName.toLowerCase()) {
            case "upi":
                activeTab = option_UPI;
                break;
            case "cards":
                activeTab = option_Cards;
                break;
            case "netbanking":
                activeTab = option_NetBanking;
                break;
            case "fee & others":
                activeTab = option_FeeAndOthers;
                break;
            default:
                throw new IllegalArgumentException("Invalid tab name: " + tabName);
        }

        String classAttribute = activeTab.getAttribute("class");

        return classAttribute.contains("active")
                || classAttribute.contains("mdc-tab--active")
                || classAttribute.contains("mat-mdc-tab-label-active");
    }
    //    public void selectValidDateRange() {
//        btn_TransactionDateRange.click();
//
//        // Example: Select last 7 days (adjust based on your calendar DOM)
//        driver.findElement(By.xpath("//button[contains(text(),'Last 7 Days')]")).click();
//    }
//
//    public void selectFutureDateRange() {
//        btn_TransactionDateRange.click();
//
//        // Example future date selection
//        driver.findElement(By.xpath("//button[contains(text(),'Next 7 Days')]")).click();
//    }
    private void selectFromDropdown(WebElement dropdown, String visibleText) {
        dropdown.click();
        WebElement option = driver.findElement(
                By.xpath("//mat-option//span[normalize-space()='" + visibleText + "']"));
        option.click();
    }
    public void selectSchemeStatus(String value) {
        selectFromDropdown(drp_SchemeStatus, value);
    }

    public void selectSettlementStatus(String value) {
        selectFromDropdown(drp_SettlementStatus, value);
    }

    public void selectTransactionType(String value) {
        selectFromDropdown(drp_TransactionType, value);
    }

    public void selectLifeCycleStatus(String value) {
        selectFromDropdown(drp_LifeCycleStatus, value);
    }

    public void selectPaymentReleaseStatus(String value) {
        selectFromDropdown(drp_PaymentReleaseStatus, value);
    }
    @FindBy(xpath = "//mat-error[contains(text(),'Transaction Amount')]")
    private WebElement txt_AmountValidationError;

    public boolean isTransactionAmountValidationDisplayed() {
        try {
            return txt_AmountValidationError.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    public boolean areDropdownsReset() {
        return drp_SchemeStatus.getText().isEmpty()
                && drp_SettlementStatus.getText().isEmpty()
                && drp_TransactionType.getText().isEmpty()
                && drp_LifeCycleStatus.getText().isEmpty();
    }
    public void clickBack() {
        btn_Back.click();
    }

    public boolean isNavigatedBack() {
        return !lbl_GlobalSearchTitle.isDisplayed();
    }
    public boolean isPageStable() {
        return driver.getTitle() != null;
    }
}