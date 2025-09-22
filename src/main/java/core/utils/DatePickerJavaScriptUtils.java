package core.utils;

import static com.codeborne.selenide.Selenide.executeJavaScript;

/**
 * Utility class for JavaScript operations on Agoda DatePicker
 */
public class DatePickerJavaScriptUtils {

    /**
     * Click on a date element using JavaScript
     */
    public static String clickDate(String dateString) {
        String script = 
            "var span = document.querySelector('span[data-selenium-date=\"" + dateString + "\"]'); " +
            "if(span) { " +
            "  var clickableParent = span.closest('div[role=\"button\"]'); " +
            "  if(clickableParent) { " +
            "    clickableParent.click(); " +
            "    return 'Clicked parent div[role=button] for date: " + dateString + "'; " +
            "  } else { " +
            "    span.click(); " +
            "    return 'Clicked span directly for date: " + dateString + "'; " +
            "  } " +
            "} " +
            "return 'Date element not found: " + dateString + "';";
        
        return (String) executeJavaScript(script);
    }

    /**
     * Get all available dates in current calendar view
     */
    public static String getAvailableDates() {
        String script = 
            "var dates = document.querySelectorAll('span[data-selenium-date]'); " +
            "var result = []; " +
            "for(var i = 0; i < dates.length; i++) { " +
            "  var date = dates[i].getAttribute('data-selenium-date'); " +
            "  if(date) result.push(date); " +
            "} " +
            "return result.join(', ');";
        
        return (String) executeJavaScript(script);
    }

    /**
     * Get current month caption from calendar
     */
    public static String getCurrentMonthCaption() {
        String script = 
            "var caption = document.querySelector('.DayPicker-Caption'); " +
            "return caption ? caption.textContent.trim() : 'Month not found';";
        
        return (String) executeJavaScript(script);
    }

    /**
     * Click alternative date in the same month
     */
    public static String clickAlternativeDate(String targetMonthPrefix) {
        String script = 
            "var dates = document.querySelectorAll('span[data-selenium-date]'); " +
            "for(var i = 0; i < dates.length; i++) { " +
            "  var date = dates[i].getAttribute('data-selenium-date'); " +
            "  if(date && date.startsWith('" + targetMonthPrefix + "')) { " +
            "    var parent = dates[i].closest('div[role=\"button\"]'); " +
            "    if(parent) { " +
            "      parent.click(); " +
            "      return 'Clicked alternative date: ' + date; " +
            "    } " +
            "  } " +
            "} " +
            "return 'No suitable alternative found in month " + targetMonthPrefix + "';";
        
        return (String) executeJavaScript(script);
    }
}