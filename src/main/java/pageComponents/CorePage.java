package pageComponents;


import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import utilities.*;

import java.io.IOException;

public class CorePage extends DriverUtils {
    public LoginPage loginPage = new LoginPage(driver);
    public DashboardPage dashboardPage = new DashboardPage(driver);
    public TransactionPage transactionPage = new TransactionPage(driver);
    public OutgoingFilePage outgoingFilePage = new OutgoingFilePage(driver);
    public ManualTriggerPage manualTriggerPage = new ManualTriggerPage(driver);
    public OdsReader odsReader = new OdsReader();
    public OTPSteps otpSteps = new OTPSteps();
    public ConfigReader configReader = new ConfigReader();
    public MailReader mailReader = new MailReader();
    public String testDataPath = System.getProperty("user.dir") + "\\testData\\";

    protected CorePage() throws IOException, InvalidFormatException {
    }
}