package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.log4j.*;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

import java.awt.*;
import java.sql.Timestamp;

public class TestBase {

    public WebDriver driver;
    public Logger log;

    @Parameters({"browser"})
    @BeforeSuite
    public void suiteSetup(@Optional("Chrome") String browser){
        String os = System.getProperty("os.name");

        if (os.contains("Windows")) {

            if (browser.equalsIgnoreCase("Chrome")) {
                ChromeOptions chromeOptions = new ChromeOptions();
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(chromeOptions);
                //driver.manage().deleteAllCookies();
                java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int width = (int) screenSize.getWidth();
                int height = (int) screenSize.getHeight();
                driver.manage().window().setSize(new org.openqa.selenium.Dimension(width,height));
                driver.manage().window().setPosition(new org.openqa.selenium.Point(0,0));
            }else
            if (browser.equalsIgnoreCase("Edge")) {
                EdgeOptions edgeOptions = new EdgeOptions();
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver(edgeOptions);
                java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int width = (int) screenSize.getWidth();
                int height = (int) screenSize.getHeight();
                driver.manage().window().setSize(new org.openqa.selenium.Dimension(width,height));
                driver.manage().window().setPosition(new org.openqa.selenium.Point(0,0));
            }else
            if (browser.equalsIgnoreCase("Firefox")) {
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver(firefoxOptions);
            }
        }else if(os.contains("Mac")){

            if(browser.equalsIgnoreCase("Chrome")){
                ChromeOptions chromeOptions = new ChromeOptions();
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(chromeOptions);
                driver.manage().deleteAllCookies();
                driver.manage().window().fullscreen();
                driver.manage().window().setPosition(new org.openqa.selenium.Point(0,0));
            }else
            if (browser.equalsIgnoreCase("Firefox")) {
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver(firefoxOptions);
            }
        }
    }
    @BeforeTest
    public void testSetup() {
        log = LogManager.getLogger(this.getClass().getName());
        BasicConfigurator.configure();
        FileAppender fileAppender = new FileAppender();
        fileAppender.setFile("logfile.txt");
        fileAppender.setLayout(new SimpleLayout());
        log.addAppender(fileAppender);
        fileAppender.activateOptions();
        getSystemEnvironment();
    }

    private void getSystemEnvironment(){
        Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
        String browserName = cap.getBrowserName().toLowerCase();
        log.info("Browser used: " + browserName);
        String os = System.getProperty("os.name");
        log.info("OS used: " + os);
        String browserVersion = cap.getVersion().toString();
        log.info("Browser version: " + browserVersion);
        String time = "Running time: " + new Timestamp(System.currentTimeMillis());
        log.info(time);
        String testName = this.getClass().getName();
        log.info("Test: " + testName);
    }

    @AfterSuite
    public void afterSuite() {
        driver.quit();
    }


}
