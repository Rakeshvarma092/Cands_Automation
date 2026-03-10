package pageComponents;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.DriverUtils;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.List;

public class SchemeRejectTxnsPage extends DriverUtils {

    private final WebDriverWait wait;

    @FindBy(xpath = "//div[contains(text(),'Scheme Reject Txns')]")
    private WebElement lbl_SchemeRejectTxnsPage;
    @FindBy(xpath = "//p[contains(text(),'Scheme Reject Txns')]")
    private WebElement verify_SchemeRejectTxnsPage;
    @FindBy(xpath = "//input[@name='f1']")
    private WebElement txt_ARN;
    @FindBy(xpath = "//input[@name='f2']")
    private WebElement txt_RRN;
    @FindBy(xpath = "//span[contains(text(),'Clear')]")
    private WebElement btn_Clear;
    @FindBy(xpath = "//span[contains(text(),' Search ')]")
    private WebElement btn_Search;
    @FindBy(xpath = "//tr[@role='row' and contains(@class,'mat-mdc-header-row')]")
    private WebElement tbl_SchemeRejectTxnsHeader;
    @FindBy(xpath = "//table//tbody/tr")
    private List<WebElement> tbl_Rows;
    @FindBy(xpath = "//p[contains(text(),'No records found')]")
    private WebElement msg_NoRecords;
    @FindBy(xpath = "//mat-error")
    private List<WebElement> lst_ValidationErrors;

    public SchemeRejectTxnsPage(WebDriver driver){
        this.driver = driver;
        this.wait = getWait(); // Assuming DriverUtils has getWait() or similar
        PageFactory.initElements(driver, this);
    }

    public void openPage() {
        wait.until(ExpectedConditions.elementToBeClickable(lbl_SchemeRejectTxnsPage)).click();
    }

     public boolean isSchemeRejectTxnsPageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(verify_SchemeRejectTxnsPage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void enterRRN(String rrn) {
        wait.until(ExpectedConditions.visibilityOf(txt_RRN));
        txt_RRN.clear();
        txt_RRN.sendKeys(rrn);
    }

    public void enterARN(String arn) {
        wait.until(ExpectedConditions.visibilityOf(txt_ARN));
        txt_ARN.clear();
        txt_ARN.sendKeys(arn);
    }

    public void clickClear() {
        wait.until(ExpectedConditions.elementToBeClickable(btn_Clear)).click();
    }
    public void clickSearch() {
        wait.until(ExpectedConditions.elementToBeClickable(btn_Search)).click();
    }

    public boolean areFieldsCleared() {
        return txt_ARN.getAttribute("value").isEmpty() && txt_RRN.getAttribute("value").isEmpty();
    }

    public boolean isValueMatchedInResults(String expectedValue) {
        try {
            wait.until(driver -> tbl_Rows.size() > 0 || msg_NoRecords.isDisplayed());
            if (tbl_Rows.isEmpty()) return false;
            
            for (WebElement row : tbl_Rows) {
                if (row.getText().contains(expectedValue)) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public boolean isNoRecordsMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(msg_NoRecords)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValidationErrorMessageDisplayed() {
        try {
            return !lst_ValidationErrors.isEmpty() && lst_ValidationErrors.get(0).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
