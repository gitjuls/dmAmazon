package tests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;

public class ResultPage {
    private WebDriver driver;
    public static HashMap<Double, String> resultList = new HashMap<>();
    public ResultPage(WebDriver driver) {
        this.driver = driver;
    }

    By setPrice = By.xpath("//*[@id='high-price']");
    By submitPrice = By.xpath("//*[@id='high-price']/..//input[@type='submit']");
    By priceWhole = By.xpath("//div[@data-component-type='s-search-result']//span[@class='a-price']/*[2]/*[@class='a-price-whole']");
    By priceFraction = By.xpath("//div[@data-component-type='s-search-result']//span[@class='a-price']/*[2]/*[@class='a-price-fraction']");
    By link = By.xpath("//div[contains(@class, 's-price-instructions-style')]/..//div[contains(@class, 's-title-instructions-style')]/h2/a");
    By nextPage = By.xpath("//*[@role='navigation'][contains(@class, 's-pagination-container')]//*[contains(@class, 's-pagination-next')]");


    public void setPrice(int price){
        WebElement inputMaxPrice = (new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(setPrice)));
        inputMaxPrice.sendKeys(String.valueOf(price));

        WebElement submitButton = driver.findElement(submitPrice);
        submitButton.click();
    }

    public void verifyHigherPrice(int price){
        //clear the old resultList after receiving new price for test
        resultList.clear();
        //while condition is true, verify each page and save each item with price that is higher than expected
        while (!this.next().getAttribute("class").contains("s-pagination-disabled")) {
            setResultListWithTheItemsThatHaveHigherPrice(price);
            this.next().click();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }
        //verify price on the last page and save the item with price that is higher than expected
        if(this.next().getAttribute("class").contains("s-pagination-disabled")){
            setResultListWithTheItemsThatHaveHigherPrice(price);
        }
    }

    private WebElement next(){
        WebElement webElement = (new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(nextPage)));
        return webElement;
    }

    private void setResultListWithTheItemsThatHaveHigherPrice(int price){
        HashMap<Integer, Double> map = new HashMap();
        map = createThePriceList();
        for (int e = 0; e < map.size(); e++) {
            if (map.get(e) > price) {
                //save price that is higher than expected and the link on it
                resultList.put(map.get(e), findItemLink(e));
            }
        }
    }

    private HashMap createThePriceList(){
        HashMap<Integer, Double> priceList = new HashMap<>();
        List<WebElement> priceListWh = (new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(priceWhole)));
        List<WebElement> priceListFr = (new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(priceFraction)));

        for (int e = 0; e < priceListWh.size(); e++) {
            String priceWh = priceListWh.get(e).getText();
            String priceFr = priceListFr.get(e).getText();
            priceList.put(e, Double.valueOf(priceWh.concat(".").concat(priceFr)));
        }

        return priceList;
    }

    private String findItemLink(int e){
        List<WebElement> list = (new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(link)));

        String link = list.get(e).getAttribute("href");
        return link;
    }
}
