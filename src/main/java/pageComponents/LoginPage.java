package pageComponents;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.ConfigReader;
import utilities.DriverUtils;
import utilities.OdsReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginPage extends DriverUtils {
    private WebDriver driver;
    private ConfigReader configReader = new ConfigReader();
    private OdsReader odsReader = new OdsReader();
    private static List<Object> loginDetails = new ArrayList<Object>();

    //*****************Locators***********************************//

    @FindBy(xpath = "//h6[normalize-space()='Login']")
    private WebElement lbl_Login;
    @FindBy(css = "input[name='uid']")
    private WebElement txt_Email;

    @FindBy(css = "input[name='password']")
    private WebElement txt_Password;

    @FindBy(xpath = "//button//span[normalize-space()='Login']")
    private WebElement btn_Login;

    @FindBy(css = ".error-message, .alert-danger, [role='alert']")
    private WebElement lbl_Error;

    public LoginPage(WebDriver driver) throws IOException, InvalidFormatException {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void navigateToLoginPage() {
        waitUntilVisible(lbl_Login);
    }

    public void enterEmail(String email) {
        txt_Email.clear();
        txt_Email.sendKeys(email);
    }

    public void enterPassword(String password) {
        txt_Password.clear();
        txt_Password.sendKeys(password);
    }

    public void clickLoginButton() {
        btn_Login.click();
    }

    public void loginAs(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }

    public boolean isLoginButtonDisplayed() {
        return btn_Login.isDisplayed();
    }
}