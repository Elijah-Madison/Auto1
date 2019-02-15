package test.search;


import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import page.search.SearchPage;

public class SearchPageTest extends SearchPage {
    private final static String[] LANGUAGE = {"de", "es", "nl", "nl-be"};
    private static final int[] YEAR = {2015, 2016, 2017};
    private static final String[] sortBy = {"Highest price", "Lowest price"}; //or can use HashMap -> key, value = "Highest price","2"...

    SearchPageTest() {
        super(LANGUAGE[0]);
    }

    @BeforeMethod
    public void setup() {
        setBrowserType();
    }

    @AfterMethod
    public void quit() {
        quitDriver();
    }

    @Test
    public void verifyAllCarsFilteredByFirstRegistrationTest() {
        switchYearFromLeftMenu(YEAR[0]);
        sortBy(sortBy[0]);
        Assert.assertTrue(checkMatchForAllResults(YEAR[0]));
    }

    @Test
    public void verifyAllCarsAreSortedByPriceDescending() {
        switchYearFromLeftMenu(YEAR[0]);
        sortBy(sortBy[0]);
        Assert.assertTrue(checkMatchForAllResults());
    }

}
