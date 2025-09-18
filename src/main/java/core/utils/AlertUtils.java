package core.utils;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * AlertUtils provides utility methods for handling JavaScript alerts, confirms, and prompts
 */
public class AlertUtils {
    private static final Logger logger = LoggerFactory.getLogger(AlertUtils.class);
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);

    /**
     * Accept alert (click OK)
     */
    @Step("Accept alert")
    public static void acceptAlert() {
        logger.info("Accepting alert");
        try {
            Alert alert = waitForAlert();
            alert.accept();
            logger.debug("Alert accepted successfully");
        } catch (Exception e) {
            logger.error("Failed to accept alert: {}", e.getMessage());
            throw new RuntimeException("Failed to accept alert", e);
        }
    }

    /**
     * Dismiss alert (click Cancel)
     */
    @Step("Dismiss alert")
    public static void dismissAlert() {
        logger.info("Dismissing alert");
        try {
            Alert alert = waitForAlert();
            alert.dismiss();
            logger.debug("Alert dismissed successfully");
        } catch (Exception e) {
            logger.error("Failed to dismiss alert: {}", e.getMessage());
            throw new RuntimeException("Failed to dismiss alert", e);
        }
    }

    /**
     * Get alert text
     * @return Alert text
     */
    @Step("Get alert text")
    public static String getAlertText() {
        logger.info("Getting alert text");
        try {
            Alert alert = waitForAlert();
            String text = alert.getText();
            logger.debug("Alert text: '{}'", text);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get alert text: {}", e.getMessage());
            throw new RuntimeException("Failed to get alert text", e);
        }
    }

    /**
     * Type text into prompt alert and accept
     * @param text Text to type
     */
    @Step("Type text '{text}' into prompt and accept")
    public static void typeInPromptAndAccept(String text) {
        logger.info("Typing text '{}' into prompt and accepting", text);
        try {
            Alert alert = waitForAlert();
            alert.sendKeys(text);
            alert.accept();
            logger.debug("Text typed into prompt and accepted successfully");
        } catch (Exception e) {
            logger.error("Failed to type text into prompt: {}", e.getMessage());
            throw new RuntimeException("Failed to type text into prompt", e);
        }
    }

    /**
     * Type text into prompt alert and dismiss
     * @param text Text to type
     */
    @Step("Type text '{text}' into prompt and dismiss")
    public static void typeInPromptAndDismiss(String text) {
        logger.info("Typing text '{}' into prompt and dismissing", text);
        try {
            Alert alert = waitForAlert();
            alert.sendKeys(text);
            alert.dismiss();
            logger.debug("Text typed into prompt and dismissed successfully");
        } catch (Exception e) {
            logger.error("Failed to type text into prompt and dismiss: {}", e.getMessage());
            throw new RuntimeException("Failed to type text into prompt and dismiss", e);
        }
    }

    /**
     * Check if alert is present
     * @return True if alert is present, false otherwise
     */
    @Step("Check if alert is present")
    public static boolean isAlertPresent() {
        logger.debug("Checking if alert is present");
        try {
            Selenide.webdriver().driver().switchTo().alert();
            logger.debug("Alert is present");
            return true;
        } catch (NoAlertPresentException e) {
            logger.debug("Alert is not present");
            return false;
        } catch (Exception e) {
            logger.debug("Error checking for alert: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Wait for alert to be present
     * @return Alert object
     */
    @Step("Wait for alert to be present")
    public static Alert waitForAlert() {
        return waitForAlert(DEFAULT_TIMEOUT);
    }

    /**
     * Wait for alert to be present with timeout
     * @param timeout Timeout duration
     * @return Alert object
     */
    @Step("Wait for alert to be present with timeout")
    public static Alert waitForAlert(Duration timeout) {
        logger.debug("Waiting for alert to be present with timeout: {}", timeout);
        try {
            WebDriverWait wait = new WebDriverWait(Selenide.webdriver().object(), timeout);
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            logger.debug("Alert is now present");
            return alert;
        } catch (TimeoutException e) {
            logger.error("Alert did not appear within {}: {}", timeout, e.getMessage());
            throw new RuntimeException("Alert did not appear within " + timeout, e);
        } catch (Exception e) {
            logger.error("Failed to wait for alert: {}", e.getMessage());
            throw new RuntimeException("Failed to wait for alert", e);
        }
    }

    /**
     * Wait for alert to disappear
     */
    @Step("Wait for alert to disappear")
    public static void waitForAlertToDisappear() {
        waitForAlertToDisappear(DEFAULT_TIMEOUT);
    }

    /**
     * Wait for alert to disappear with timeout
     * @param timeout Timeout duration
     */
    @Step("Wait for alert to disappear with timeout")
    public static void waitForAlertToDisappear(Duration timeout) {
        logger.debug("Waiting for alert to disappear with timeout: {}", timeout);
        
        long startTime = System.currentTimeMillis();
        long endTime = startTime + timeout.toMillis();
        
        while (System.currentTimeMillis() < endTime) {
            if (!isAlertPresent()) {
                logger.debug("Alert has disappeared");
                return;
            }
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Wait interrupted", e);
            }
        }
        
        logger.error("Alert did not disappear within {}", timeout);
        throw new RuntimeException("Alert did not disappear within " + timeout);
    }

    /**
     * Handle alert by accepting if present
     */
    @Step("Handle alert by accepting if present")
    public static void handleAlertIfPresent() {
        logger.debug("Handling alert if present");
        if (isAlertPresent()) {
            logger.info("Alert found - accepting");
            acceptAlert();
        } else {
            logger.debug("No alert present");
        }
    }

    /**
     * Handle alert by dismissing if present
     */
    @Step("Handle alert by dismissing if present")
    public static void dismissAlertIfPresent() {
        logger.debug("Dismissing alert if present");
        if (isAlertPresent()) {
            logger.info("Alert found - dismissing");
            dismissAlert();
        } else {
            logger.debug("No alert present");
        }
    }

    /**
     * Get alert text if present, otherwise return null
     * @return Alert text or null if no alert
     */
    @Step("Get alert text if present")
    public static String getAlertTextIfPresent() {
        logger.debug("Getting alert text if present");
        if (isAlertPresent()) {
            return getAlertText();
        } else {
            logger.debug("No alert present");
            return null;
        }
    }
}