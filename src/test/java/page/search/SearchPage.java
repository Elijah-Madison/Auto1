package page.search;

import banner.CallCenterBanner;
import core.BaseMethod;
import core.DriverConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

import static page.search.SearchLeftMenu.switchYear;

public class SearchPage {
    public SearchPage() {
    }

    public SearchPage(String language) {
        URL = "https://www.autohero.com/" + language + "/search";
    }

    private By searchMenuTopSortByFilter = By.xpath(".//select[@name='sort']");
    private By foundResultNumber = By.xpath(".//div[@data-qa-selector='results-amount']");
    private By firstRegistrationMonthAndYear = By.xpath(".//li[@data-qa-selector='spec'][1]");
    private String lastSearchResultPageHref = ".//span[@aria-label='Last']/ancestor::a";
    private By lastSearchResultPage = By.xpath(lastSearchResultPageHref);
    private By lastSearchResultPageDisabled = By.xpath(lastSearchResultPageHref + "/ancestor::li[@class='disabled']");
    private By carPrice = By.xpath(".//div[@data-qa-selector='price']");
    private static String URL = "https://www.autohero.com/de/search";
    private WebDriver driver;
    private Select select;
    private int numForAllResults = 0;

    protected void setBrowserType() {
        driver = DriverConfig.getDriver();
        driver.get(URL);
        CallCenterBanner.closeCallCenterBanner(driver);
    }

    protected void quitDriver() {
        driver.quit();
    }

    protected void switchYearFromLeftMenu(int year) {
        switchYear(driver, year);
    }

    protected void sortBy(String sortBy) {
        WebElement leftMenuYearEl = BaseMethod.getElementWithWaitForVisibility(driver, searchMenuTopSortByFilter, 5);
        select = new Select(leftMenuYearEl);
        if(sortBy.equalsIgnoreCase("Highest price"))
            select.selectByValue("2");
        else {
            select.selectByValue("3"); //Lowest price
        }
    }

    private Integer getFoundResultNumber() {
        WebElement foundResultNumAndText = BaseMethod.getElementWithWaitForVisibility(driver, foundResultNumber, 5);
        int indexOfSpace = foundResultNumAndText.getText().indexOf(" ");
        String foundResultNum = foundResultNumAndText.getText().substring(0, indexOfSpace).trim();
        return Integer.parseInt(foundResultNum);
    }

    private Integer getOverallPagesNum() {
        String lastPageNum;
        boolean isLastPage = true;
        //check that result have more than one page
        try {
            BaseMethod.getElementWithWaitForVisibility(driver, lastSearchResultPageDisabled, 5);
        } catch (NoSuchElementException ignore) {
            isLastPage = false;
        }
        if (isLastPage) return 1;
        System.out.println(isLastPage);
        String lastPageNumHref = BaseMethod.getElementWithWaitForVisibility(driver, lastSearchResultPage, 5).getAttribute("href").toString();
        int indexOfAnd = lastPageNumHref.indexOf("&");
        int indexOfEqual = lastPageNumHref.indexOf("=");

        if (indexOfAnd != -1) {
            lastPageNum = lastPageNumHref.substring(indexOfEqual + 1, indexOfAnd).trim();
        } else {
            lastPageNum = lastPageNumHref.substring(indexOfEqual + 1).trim();
        }
        return Integer.parseInt(lastPageNum);
    }

    protected Boolean checkMatchForAllResults(int year) {
        String currentUrl = driver.getCurrentUrl();
        int overallPagesNum = getOverallPagesNum();

        for (int i = 1; i <= overallPagesNum; ) {
            //close banner if appears
            CallCenterBanner.closeCallCenterBanner(driver);

            if (year != 0) {
                if (!checkFirstRegistrationYearMatchFilterCondition(year, i, BaseMethod.getAllElementsWithWaitForVisibility(driver, firstRegistrationMonthAndYear, 5)))
                    return false;
            } else {
                if (!checkSortedByPriceDescending(i, BaseMethod.getAllElementsWithWaitForVisibility(driver, carPrice, 5)))
                    return false;
            }

            i++;
            if (i != 1 && i <= overallPagesNum) {
                driver.get(currentUrl + "&page=" + i);
            }
        }
        if (numForAllResults < getFoundResultNumber()) {
            System.out.println("Not all results was checked! Expected =" + getFoundResultNumber() + " Checked =" + numForAllResults);
            return false;
        }
        return true;
    }

    protected Boolean checkMatchForAllResults() {
        return checkMatchForAllResults(0);
    }

    private Boolean checkFirstRegistrationYearMatchFilterCondition(int year, int i, List<WebElement> registrationMonthAndYearList) {
        for (WebElement registrationMonthAndYear : registrationMonthAndYearList) {
            numForAllResults++;
            int indexOfSlash = registrationMonthAndYear.getText().indexOf("/");
            int foundResultNum = Integer.parseInt(registrationMonthAndYear.getText().substring(indexOfSlash + 1).trim());
            if (foundResultNum < year) {
                System.out.println("Element " + registrationMonthAndYear + ", on page " + i +
                        " does not match filter conditions, expected year not lower than " + year);
                return false;
            }
        }
        return true;
    }

    private Boolean checkSortedByPriceDescending(int i, List<WebElement> prices) {
        //get first value
        double previousPrice = Double.valueOf(prices.get(0).getText().substring(0, prices.get(0).getText().indexOf(" ")).trim());

        for (WebElement price : prices) {
            numForAllResults++;
            int indexOfAnd = price.getText().indexOf(" ");
            double currentPrice = Double.valueOf(price.getText().substring(0, indexOfAnd).trim());
            if (currentPrice <= previousPrice) {
                previousPrice = currentPrice;
            } else if (currentPrice > previousPrice) {
                System.out.println("Price " + currentPrice + ", on page " + i +
                        " is more than the previous price " + previousPrice);
                return false;
            }
        }
        return true;
    }
}
