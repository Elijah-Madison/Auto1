package core;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class BaseMethod {

    public static WebElement getElementWithWaitForVisibility(WebDriver driver, By xpath, int time) {
        WebDriverWait wait = new WebDriverWait(driver, time);
        return wait.withMessage("Element by path " + xpath + " is not visible").until(ExpectedConditions.visibilityOf(driver.findElement(xpath)));
    }

    public static List<WebElement> getAllElementsWithWaitForVisibility(WebDriver driver, By xpath, int time) {
        WebDriverWait wait = new WebDriverWait(driver, time);
        return wait.withMessage("Element by path " + xpath + " is not visible").until(ExpectedConditions.visibilityOfAllElementsLocatedBy(xpath));
    }

}
