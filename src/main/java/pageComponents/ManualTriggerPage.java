package pageComponents;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.DriverUtils;

public class ManualTriggerPage extends DriverUtils {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public ManualTriggerPage (WebDriver driver) {
        this.driver = driver;
        this.wait = getWait();
        PageFactory.initElements(driver, this);
    }
    @FindBy(xpath = "//p[contains(text(),'Manual Trigger ')]")
    private WebElement verify_ManualTrigger;
    @FindBy(xpath = "//mat-select[@name='selecte']")
    private WebElement drp_FileType;
    @FindBy(xpath = "//span[contains(text(),' Trigger Now ')]")
    private WebElement btn_TriggerNow;
    @FindBy(xpath = "//h2[contains(text(),'Confirm Trigger')]")
    private WebElement verify_ConfirmTriggerPopup;
    @FindBy(xpath = "//span[contains(text(),'Cancel')]")
    private WebElement btn_CancelTrigger;
    @FindBy(xpath = "//span[contains(text(),'Yes')]")
    private WebElement btn_ConfirmTrigger;
    private final String selectDropdownData = "//span[contains(text(),'%s')]";

    // ---------- Page verification ----------
    public void isManualTriggerPageDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(verify_ManualTrigger)).isDisplayed();
    }

    // ---------- File Type dropdown ----------
    public void selectFileType(String fileType) {
        wait.until(ExpectedConditions.elementToBeClickable(drp_FileType)).click();
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(String.format(selectDropdownData, fileType))
        ));
        option.click();
    }

    // ---------- Trigger Now ----------
    public void clickTriggerNow() {
        wait.until(ExpectedConditions.elementToBeClickable(btn_TriggerNow)).click();
    }

    // ---------- Confirm Trigger popup ----------
    public boolean isConfirmTriggerPopupDisplayed() {
        return wait.until(ExpectedConditions.visibilityOf(verify_ConfirmTriggerPopup)).isDisplayed();
    }

    public void clickCancelTrigger() {
        wait.until(ExpectedConditions.elementToBeClickable(btn_CancelTrigger)).click();
    }

    public void clickConfirmTriggerYes() {
        wait.until(ExpectedConditions.elementToBeClickable(btn_ConfirmTrigger)).click();
    }

    // ---------- Combined flow helpers ----------
    public void triggerManualFile(String fileType) {
        selectFileType(fileType);
        clickTriggerNow();
        wait.until(ExpectedConditions.visibilityOf(verify_ConfirmTriggerPopup));
        clickConfirmTriggerYes();
    }

    public void triggerManualFileAndCancel(String fileType) {
        selectFileType(fileType);
        clickTriggerNow();
        wait.until(ExpectedConditions.visibilityOf(verify_ConfirmTriggerPopup));
        clickCancelTrigger();
    }
}
