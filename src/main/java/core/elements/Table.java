package core.elements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Table element wrapper for handling HTML tables
 */
public class Table extends BaseElement {

    /**
     * Constructor for Table element
     * @param element SelenideElement representing the table
     * @param elementName Name for logging and reporting
     */
    public Table(SelenideElement element, String elementName) {
        super(element, elementName);
    }

    /**
     * Constructor for Table element with default name
     * @param element SelenideElement representing the table
     */
    public Table(SelenideElement element) {
        super(element, "Table");
    }

    /**
     * Get all rows in the table
     * @return ElementsCollection of table rows
     */
    @Step("Get all rows from table [{elementName}]")
    public ElementsCollection getRows() {
        logger.info("Getting all rows from table [{}]", elementName);
        try {
            ElementsCollection rows = element.$$("tr");
            logger.debug("Found {} rows in table [{}]", rows.size(), elementName);
            return rows;
        } catch (Exception e) {
            logger.error("Failed to get rows from table [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to get rows from table " + elementName, e);
        }
    }

    /**
     * Get all header cells in the table
     * @return ElementsCollection of header cells
     */
    @Step("Get header cells from table [{elementName}]")
    public ElementsCollection getHeaders() {
        logger.info("Getting header cells from table [{}]", elementName);
        try {
            ElementsCollection headers = element.$$("th");
            if (headers.isEmpty()) {
                // Try to get headers from first row if no th elements
                headers = element.$$("tr").first().$$("td");
            }
            logger.debug("Found {} header cells in table [{}]", headers.size(), elementName);
            return headers;
        } catch (Exception e) {
            logger.error("Failed to get header cells from table [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to get header cells from table " + elementName, e);
        }
    }

    /**
     * Get header texts as list
     * @return List of header texts
     */
    @Step("Get header texts from table [{elementName}]")
    public List<String> getHeaderTexts() {
        logger.info("Getting header texts from table [{}]", elementName);
        try {
            List<String> headerTexts = getHeaders().texts();
            logger.debug("Header texts from table [{}]: {}", elementName, headerTexts);
            return headerTexts;
        } catch (Exception e) {
            logger.error("Failed to get header texts from table [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to get header texts from table " + elementName, e);
        }
    }

    /**
     * Get specific row by index (0-based)
     * @param rowIndex Row index (0-based)
     * @return SelenideElement representing the row
     */
    @Step("Get row {rowIndex} from table [{elementName}]")
    public SelenideElement getRow(int rowIndex) {
        logger.info("Getting row {} from table [{}]", rowIndex, elementName);
        try {
            ElementsCollection rows = getRows();
            if (rowIndex >= rows.size()) {
                throw new IndexOutOfBoundsException("Row index " + rowIndex + " is out of bounds. Table has " + rows.size() + " rows.");
            }
            SelenideElement row = rows.get(rowIndex);
            logger.debug("Retrieved row {} from table [{}]", rowIndex, elementName);
            return row;
        } catch (Exception e) {
            logger.error("Failed to get row {} from table [{}]: {}", rowIndex, elementName, e.getMessage());
            throw new RuntimeException("Failed to get row " + rowIndex + " from table " + elementName, e);
        }
    }

    /**
     * Get specific cell by row and column index (0-based)
     * @param rowIndex Row index (0-based)
     * @param colIndex Column index (0-based)
     * @return SelenideElement representing the cell
     */
    @Step("Get cell [{rowIndex}, {colIndex}] from table [{elementName}]")
    public SelenideElement getCell(int rowIndex, int colIndex) {
        logger.info("Getting cell [{}, {}] from table [{}]", rowIndex, colIndex, elementName);
        try {
            SelenideElement row = getRow(rowIndex);
            ElementsCollection cells = row.$$("td, th");
            if (colIndex >= cells.size()) {
                throw new IndexOutOfBoundsException("Column index " + colIndex + " is out of bounds. Row has " + cells.size() + " cells.");
            }
            SelenideElement cell = cells.get(colIndex);
            logger.debug("Retrieved cell [{}, {}] from table [{}]", rowIndex, colIndex, elementName);
            return cell;
        } catch (Exception e) {
            logger.error("Failed to get cell [{}, {}] from table [{}]: {}", rowIndex, colIndex, elementName, e.getMessage());
            throw new RuntimeException("Failed to get cell [" + rowIndex + ", " + colIndex + "] from table " + elementName, e);
        }
    }

    /**
     * Get cell text by row and column index
     * @param rowIndex Row index (0-based)
     * @param colIndex Column index (0-based)
     * @return Cell text content
     */
    @Step("Get cell text [{rowIndex}, {colIndex}] from table [{elementName}]")
    public String getCellText(int rowIndex, int colIndex) {
        logger.info("Getting cell text [{}, {}] from table [{}]", rowIndex, colIndex, elementName);
        try {
            String cellText = getCell(rowIndex, colIndex).getText();
            logger.debug("Cell text [{}, {}] from table [{}]: '{}'", rowIndex, colIndex, elementName, cellText);
            return cellText;
        } catch (Exception e) {
            logger.error("Failed to get cell text [{}, {}] from table [{}]: {}", rowIndex, colIndex, elementName, e.getMessage());
            throw new RuntimeException("Failed to get cell text from table " + elementName, e);
        }
    }

    /**
     * Find row by cell content
     * @param columnIndex Column index to search in (0-based)
     * @param searchText Text to search for
     * @return Row index containing the text, -1 if not found
     */
    @Step("Find row containing '{searchText}' in column {columnIndex} of table [{elementName}]")
    public int findRowByColumnText(int columnIndex, String searchText) {
        logger.info("Finding row containing '{}' in column {} of table [{}]", searchText, columnIndex, elementName);
        try {
            ElementsCollection rows = getRows();
            for (int i = 0; i < rows.size(); i++) {
                try {
                    String cellText = getCellText(i, columnIndex);
                    if (cellText.equals(searchText)) {
                        logger.debug("Found text '{}' in row {} of table [{}]", searchText, i, elementName);
                        return i;
                    }
                } catch (Exception e) {
                    // Skip rows that don't have enough columns
                    continue;
                }
            }
            logger.debug("Text '{}' not found in column {} of table [{}]", searchText, columnIndex, elementName);
            return -1;
        } catch (Exception e) {
            logger.error("Failed to find row by column text in table [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to find row by column text in table " + elementName, e);
        }
    }

    /**
     * Click on a specific cell
     * @param rowIndex Row index (0-based)
     * @param colIndex Column index (0-based)
     * @return This Table for method chaining
     */
    @Step("Click cell [{rowIndex}, {colIndex}] in table [{elementName}]")
    public Table clickCell(int rowIndex, int colIndex) {
        logger.info("Clicking cell [{}, {}] in table [{}]", rowIndex, colIndex, elementName);
        try {
            getCell(rowIndex, colIndex).shouldBe(Condition.visible).click();
            logger.debug("Successfully clicked cell [{}, {}] in table [{}]", rowIndex, colIndex, elementName);
        } catch (Exception e) {
            logger.error("Failed to click cell [{}, {}] in table [{}]: {}", rowIndex, colIndex, elementName, e.getMessage());
            throw new RuntimeException("Failed to click cell in table " + elementName, e);
        }
        return this;
    }

    /**
     * Get all data from the table as a list of lists
     * @return List of rows, where each row is a list of cell texts
     */
    @Step("Get all data from table [{elementName}]")
    public List<List<String>> getAllData() {
        logger.info("Getting all data from table [{}]", elementName);
        try {
            ElementsCollection rows = getRows();
            List<List<String>> tableData = rows.stream()
                .map(row -> row.$$("td, th").texts())
                .collect(Collectors.toList());
            logger.debug("Retrieved {} rows of data from table [{}]", tableData.size(), elementName);
            return tableData;
        } catch (Exception e) {
            logger.error("Failed to get all data from table [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to get all data from table " + elementName, e);
        }
    }

    /**
     * Get number of rows in the table
     * @return Number of rows
     */
    @Step("Get row count from table [{elementName}]")
    public int getRowCount() {
        logger.info("Getting row count from table [{}]", elementName);
        try {
            int rowCount = getRows().size();
            logger.debug("Table [{}] has {} rows", elementName, rowCount);
            return rowCount;
        } catch (Exception e) {
            logger.error("Failed to get row count from table [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to get row count from table " + elementName, e);
        }
    }

    /**
     * Get number of columns in the table (based on first row)
     * @return Number of columns
     */
    @Step("Get column count from table [{elementName}]")
    public int getColumnCount() {
        logger.info("Getting column count from table [{}]", elementName);
        try {
            if (getRowCount() == 0) {
                return 0;
            }
            int columnCount = getRow(0).$$("td, th").size();
            logger.debug("Table [{}] has {} columns", elementName, columnCount);
            return columnCount;
        } catch (Exception e) {
            logger.error("Failed to get column count from table [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to get column count from table " + elementName, e);
        }
    }

    /**
     * Sort table by clicking on column header
     * @param columnIndex Column index to sort by (0-based)
     * @return This Table for method chaining
     */
    @Step("Sort table [{elementName}] by column {columnIndex}")
    public Table sortByColumn(int columnIndex) {
        logger.info("Sorting table [{}] by column {}", elementName, columnIndex);
        try {
            ElementsCollection headers = getHeaders();
            if (columnIndex >= headers.size()) {
                throw new IndexOutOfBoundsException("Column index " + columnIndex + " is out of bounds. Table has " + headers.size() + " columns.");
            }
            headers.get(columnIndex).shouldBe(Condition.visible).click();
            logger.debug("Successfully sorted table [{}] by column {}", elementName, columnIndex);
        } catch (Exception e) {
            logger.error("Failed to sort table [{}] by column {}: {}", elementName, columnIndex, e.getMessage());
            throw new RuntimeException("Failed to sort table " + elementName, e);
        }
        return this;
    }
}