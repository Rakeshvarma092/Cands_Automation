package pageComponents;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.DriverUtils;

public class DashboardPage extends DriverUtils {
    private WebDriver driver;

    //*****************Locators***********************************//
    @FindBy(xpath = "//*[contains(text(), 'Login successful.')]")
    private WebElement msg_LoginSuccess;

    @FindBy(xpath = "//*[text()='Dismiss']")
    private WebElement link_Dismiss;

    @FindBy(xpath = "//span[contains(text(),'Superadmin')]")
    private WebElement lbl_ProfileName;

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getProfileName() {
        waitUntilVisible(lbl_ProfileName);
        return lbl_ProfileName.getText();
    }
}