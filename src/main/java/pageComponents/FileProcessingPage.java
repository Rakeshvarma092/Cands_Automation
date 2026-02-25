package pageComponents;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.DriverUtils;

import static DriverFactory.WebDriverFactory.getWait;

public class FileProcessingPage extends DriverUtils {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public FileProcessingPage(WebDriver driver) {
        this.driver = driver;
        this.wait = getWait();
        PageFactory.initElements(driver, this);
    }
    @FindBy(xpath = "(//span[@class='mat-mdc-button-touch-target'])[4]")
    private WebElement btn_FileProcessing;
    @FindBy(xpath = "//div[contains(text(),' Outgoing File ')]")
    private WebElement lbl_OutgoingFile;
    @FindBy(xpath = "//div[contains(text(),' Manual trigger ')]")
    private WebElement lbl_ManualTrigger;
    @FindBy(xpath = "//div[contains(text(),' Exception Trigger ')]")
    private WebElement lbl_ExceptionTrigger;
    @FindBy(xpath = "//p[contains(text(),'Outgoing File ')]")
    private WebElement verify_OutgoingFile;
    @FindBy(xpath = "//mat-select[@name='f1']")
    private WebElement drp_TransactionType;
    @FindBy(xpath = "//label[contains(text(),'Include Today')]")
    private WebElement radio_IncludeToday;
    @FindBy(xpath = "//label[contains(text(),'Exclude Today')]")
    private WebElement radio_ExcludeToday;
    @FindBy(xpath = "//span[contains(text(),' Trigger Now ')]")
    private WebElement btn_TriggerNow;
    @FindBy(xpath = "//*[contains(@class,'snack') or contains(@class,'toast') or contains(@class,'alert')][contains(.,'Triggered') or contains(.,'success') or contains(.,'Successfully')]")
    private WebElement msg_TriggerSuccess;
    @FindBy(xpath = "//mat-error[contains(.,'Transaction Type') or contains(.,'transaction type') or contains(.,'required')]")
    private WebElement err_TransactionType;
    @FindBy(xpath = "//mat-error[contains(.,'Include Today') or contains(.,'Exclude Today') or contains(.,'Today') or contains(.,'required')]")
    private WebElement err_TodaySelection;

    // ---------- Navigation / Page open ----------
    public void openFileProcessing() {
        wait.until(ExpectedConditions.elementToBeClickable(btn_FileProcessing)).click();
    }

    // ---------- Section visibility checks ----------
    public boolean isOutgoingFileTabVisible() {
        return wait.until(ExpectedConditions.visibilityOf(lbl_OutgoingFile)).isDisplayed();
    }

    public boolean isManualTriggerTabVisible() {
        return wait.until(ExpectedConditions.visibilityOf(lbl_ManualTrigger)).isDisplayed();
    }

    public boolean isExceptionTriggerTabVisible() {
        return wait.until(ExpectedConditions.visibilityOf(lbl_ExceptionTrigger)).isDisplayed();
    }

    public boolean isOutgoingFileHeaderVisible() {
        return wait.until(ExpectedConditions.visibilityOf(verify_OutgoingFile)).isDisplayed();
    }

    // ---------- Click actions on tabs/labels ----------
    public void clickOutgoingFileTab() {
        wait.until(ExpectedConditions.elementToBeClickable(lbl_OutgoingFile)).click();
    }

    public void clickManualTriggerTab() {
        wait.until(ExpectedConditions.elementToBeClickable(lbl_ManualTrigger)).click();
    }

    public void clickExceptionTriggerTab() {
        wait.until(ExpectedConditions.elementToBeClickable(lbl_ExceptionTrigger)).click();
    }

    // ---------- Dropdown selection (mat-select) ----------
    public void selectTransactionType(String optionText) {
        wait.until(ExpectedConditions.elementToBeClickable(drp_TransactionType)).click();
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//mat-option//span[normalize-space()='" + optionText + "']")));
        option.click();
    }

    // ---------- Radio buttons ----------
    public void selectIncludeToday() {
        wait.until(ExpectedConditions.elementToBeClickable(radio_IncludeToday)).click();
    }

    public void selectExcludeToday() {
        wait.until(ExpectedConditions.elementToBeClickable(radio_ExcludeToday)).click();
    }

    public boolean isIncludeTodaySelected() {
        return radio_IncludeToday.findElement(By.xpath("./preceding::input[1]")).isSelected();
    }

    public boolean isExcludeTodaySelected() {
        return radio_ExcludeToday.findElement(By.xpath("./preceding::input[1]")).isSelected();
    }

    // ---------- Trigger action ----------
    public void clickTriggerNow() {
        wait.until(ExpectedConditions.elementToBeClickable(btn_TriggerNow)).click();
    }

    // ---------- Successfully Message ----------
    public boolean isTriggerSuccessMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(msg_TriggerSuccess)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ---------- Error Message ----------
    public boolean isTransactionTypeValidationDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(err_TransactionType)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTodaySelectionValidationDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(err_TodaySelection)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

}
