package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SelfHealingSelenium {

    // Example method for self-healing element finding
    public WebElement findElementWithRetry(WebDriver driver, String primaryLocator, String[] alternativeLocators) {
        WebElement element = null;

        try {
            // Attempt to find the element using the primary locator
            element = driver.findElement(By.xpath(primaryLocator));
        } catch (Exception e) {
            // Log or handle the exception as needed

            System.out.println(primaryLocator + "- Primary locator failed and trying self-healing with alternate locators");

            // If the primary locator fails, try alternative locators
            for (String altLocator : alternativeLocators) {
                try {
                    element = driver.findElement(By.xpath(altLocator));
                    System.out.println(altLocator + " - Alternate locator is chosen and worked. Please update locators.");
                    // If alternative locator succeeds, break the loop
                    break;
                } catch (Exception ex) {
                    // Log or handle the exception for the alternative locator
                    System.err.println(altLocator + " - Alternate locator is chosen and failed. Please update locators.");
                }
            }
        }

        // Return the located element (null if not found)
        return element;
    }

    public static void main(String[] args) {
        // Example usage

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        SelfHealingSelenium selfHealing = new SelfHealingSelenium();
        driver.get("https://www.saucedemo.com/");

        // Example locators
        String primaryLocator = "//*[@id='user-name']";
        String[] alternativeLocators = {"//*[@name='user-name']", "//*[@class='input_error form_input']",
                "//*[@data-test='username']", "//*[@placeholder='Username']"};

        // Find the element with self-healing
        WebElement element = selfHealing.findElementWithRetry(driver, primaryLocator, alternativeLocators);

        // Use the located element as needed
        if (element != null) {
            // Perform actions on the element
            element.click();
            element.sendKeys("standard_user");
        } else {
            // Handle the case when the element is not found even with alternative locators
            System.out.println("Element not found.");
        }

        // Close the WebDriver
       // driver.quit();
    }
}
