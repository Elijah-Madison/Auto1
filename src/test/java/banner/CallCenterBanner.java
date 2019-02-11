package banner;

import core.BaseMethod;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

public class CallCenterBanner {
    private static String questionBannerPath = ".//*[@class='modal-dialog']";
    private static By questionBanner = By.xpath(questionBannerPath);
    private static By closeQuestionBanner = By.xpath(questionBannerPath + "//span[contains(text(), '×')]");

    public static void closeCallCenterBanner(WebDriver driver) {
        try {
            BaseMethod.getElementWithWaitForVisibility(driver, closeQuestionBanner, 10).click();
        } catch (NoSuchElementException ignore) {
        }
    }
}

