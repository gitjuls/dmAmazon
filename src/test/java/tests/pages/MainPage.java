package tests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MainPage {
    private WebDriver driver;
    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    public void navigate() {
        driver.get("https://www.amazon.com/");
    }

    By inputSearch = By.xpath("//*[@id='twotabsearchtextbox']");
    By submitSearch = By.xpath("//*[@id='nav-search-submit-button']");

    public void inputSearchData(String data){
        WebElement inputData = (new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(inputSearch)));
        inputData.clear();
        inputData.sendKeys(data);
        WebElement submitButton = driver.findElement(submitSearch);
        submitButton.submit();
    }
}
