package pages.theinternet;

import core.elements.BaseElement;
import core.utils.LogUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Drag and Drop page for testing drag and drop interactions
 * URL: https://the-internet.herokuapp.com/drag_and_drop
 */
public class DragAndDropPage extends BaseInternetPage {
    
    // Page elements
    private final BaseElement columnA = new BaseElement($(By.id("column-a")), "Column A");
    private final BaseElement columnB = new BaseElement($(By.id("column-b")), "Column B");
    private final BaseElement columnAHeader = new BaseElement($(By.xpath("//div[@id='column-a']/header")), "Column A Header");
    private final BaseElement columnBHeader = new BaseElement($(By.xpath("//div[@id='column-b']/header")), "Column B Header");
    
    // Page URL
    public static final String PAGE_URL = BASE_URL + "/drag_and_drop";
    
    /**
     * Constructor - validates page is loaded
     */
    public DragAndDropPage() {
        validatePageLoaded();
    }

    /**
     * Drag column A to column B position
     * @return DragAndDropPage for method chaining
     */
    @Step("Drag column A to column B position")
    public DragAndDropPage dragColumnAToB() {
        LogUtils.logPageAction("Dragging column A to column B position");
        columnA.dragAndDropTo(columnB);
        logger.info("Column A dragged to column B position");
        return this;
    }

    /**
     * Drag column B to column A position
     * @return DragAndDropPage for method chaining
     */
    @Step("Drag column B to column A position")
    public DragAndDropPage dragColumnBToA() {
        LogUtils.logPageAction("Dragging column B to column A position");
        columnB.dragAndDropTo(columnA);
        logger.info("Column B dragged to column A position");
        return this;
    }

    /**
     * Get column A header text
     * @return Column A header text
     */
    @Step("Get column A header text")
    public String getColumnAHeaderText() {
        LogUtils.logPageAction("Getting column A header text");
        String headerText = columnAHeader.getText();
        LogUtils.logTestData("Column A Header", headerText);
        logger.info("Column A header text: {}", headerText);
        return headerText;
    }

    /**
     * Get column B header text
     * @return Column B header text
     */
    @Step("Get column B header text")
    public String getColumnBHeaderText() {
        LogUtils.logPageAction("Getting column B header text");
        String headerText = columnBHeader.getText();
        LogUtils.logTestData("Column B Header", headerText);
        logger.info("Column B header text: {}", headerText);
        return headerText;
    }

    /**
     * Verify columns have swapped positions
     * @return True if columns are swapped (A shows B and B shows A)
     */
    @Step("Verify columns have swapped positions")
    public boolean areColumnsSwapped() {
        LogUtils.logPageAction("Verifying if columns have swapped positions");
        String columnAText = getColumnAHeaderText();
        String columnBText = getColumnBHeaderText();
        
        boolean swapped = "B".equals(columnAText) && "A".equals(columnBText);
        LogUtils.logTestData("Columns Swapped", swapped);
        logger.info("Columns swapped: {}", swapped);
        return swapped;
    }

    /**
     * Verify columns are in original positions
     * @return True if columns are in original positions (A shows A and B shows B)
     */
    @Step("Verify columns are in original positions")
    public boolean areColumnsInOriginalPosition() {
        LogUtils.logPageAction("Verifying if columns are in original positions");
        String columnAText = getColumnAHeaderText();
        String columnBText = getColumnBHeaderText();
        
        boolean original = "A".equals(columnAText) && "B".equals(columnBText);
        LogUtils.logTestData("Columns in Original Position", original);
        logger.info("Columns in original position: {}", original);
        return original;
    }

    /**
     * Get current column order
     * @return String representing current order (e.g., "A-B" or "B-A")
     */
    @Step("Get current column order")
    public String getCurrentColumnOrder() {
        LogUtils.logPageAction("Getting current column order");
        String columnAText = getColumnAHeaderText();
        String columnBText = getColumnBHeaderText();
        String order = columnAText + "-" + columnBText;
        LogUtils.logTestData("Current Column Order", order);
        logger.info("Current column order: {}", order);
        return order;
    }

    /**
     * Reset columns to original position by dragging if needed
     * @return DragAndDropPage for method chaining
     */
    @Step("Reset columns to original position")
    public DragAndDropPage resetToOriginalPosition() {
        LogUtils.logPageAction("Resetting columns to original position");
        if (!areColumnsInOriginalPosition()) {
            if (areColumnsSwapped()) {
                dragColumnAToB(); // This should swap them back
            }
        }
        logger.info("Columns reset to original position");
        return this;
    }

    /**
     * Check if column A is visible
     * @return True if column A is visible
     */
    @Step("Check if column A is visible")
    public boolean isColumnAVisible() {
        LogUtils.logPageAction("Checking if column A is visible");
        boolean visible = columnA.isVisible();
        LogUtils.logTestData("Column A Visible", visible);
        logger.info("Column A visible: {}", visible);
        return visible;
    }

    /**
     * Check if column B is visible
     * @return True if column B is visible
     */
    @Step("Check if column B is visible")
    public boolean isColumnBVisible() {
        LogUtils.logPageAction("Checking if column B is visible");
        boolean visible = columnB.isVisible();
        LogUtils.logTestData("Column B Visible", visible);
        logger.info("Column B visible: {}", visible);
        return visible;
    }
}