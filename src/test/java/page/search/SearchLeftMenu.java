package page.search;

import core.BaseMethod;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class SearchLeftMenu extends SearchPage {
    private static final String leftMenuPath = ".//div[@class='col-md-3']";
    private static final By leftMenu = By.xpath(leftMenuPath);
    private static final By leftMenuYear = By.xpath(leftMenuPath + "//select[@name='yearRange.min']");
    private static Select select;

    public static void switchYear(WebDriver driver, int year) {
        BaseMethod.getElementWithWaitForVisibility(driver, leftMenu, 5).click();
        WebElement leftMenuYearEl = driver.findElement(leftMenuYear);
        select = new Select(leftMenuYearEl);
        select.selectByVisibleText(Integer.toString(year));
    }
}
