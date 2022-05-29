package tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tests.pages.MainPage;
import tests.pages.ResultPage;
import java.util.Map;

public class VerifyUpperPriceLimit extends TestBase{

    @DataProvider(name = "test1")
    public Object[][] createData() {
        return new Object[][] {
                { "lego city, lego creator",  new Integer(24) },
                { "lego city, lego creator",  new Integer(49)},
        };
    }

    @Test(dataProvider = "test1")
    public void verifyIfTheUpperPriceOfTheItemDoNotHigherThanTheSetPrice(String paramOne, int paramTwo) {
        MainPage mainPage = new MainPage(driver);
        mainPage.navigate();
        mainPage.inputSearchData(paramOne);
        ResultPage resultPage = new ResultPage(driver);
        resultPage.setPrice(paramTwo);
        resultPage.verifyHigherPrice(paramTwo);
        boolean result = ResultPage.resultList.size()==0 ? true : false;
        log.debug(result);
        if(result == false){
            for (Map.Entry<Double, String> pair : ResultPage.resultList.entrySet()) {
                log.info("Upper price is: " + paramTwo + " The search result is: " + pair.getKey() + " -> " + pair.getValue());
            }
        }
        Assert.assertTrue(result);
    }

}
