# 📌 Automation Framework Requirements (Java + Selenide)

## 1. **Core Tech Stack**

-   **Java 17** (LTS, stable for enterprise use)\
-   **Selenide 7.9.2** (wrapper around Selenium, simplifies
    waits/actions)\
-   **Selenium 4.33** (WebDriver protocol compliance)\
-   **Maven** (build + dependency management)\
-   **TestNG** (test runner, annotations, parallel execution,
    data-driven support)\
-   **Allure Report** (rich reporting with steps, screenshots, logs)\
-   **SLF4J + Logback** (logging to console + file)

------------------------------------------------------------------------

## 2. **Project Structure**

    src
     ├─ main
     │   ├─ java
     │   │   ├─ core
     │   │   │    ├─ config        (ConfigManager, BrowserManager)
     │   │   │    ├─ driver        (DriverFactory, WebDriver settings)
     │   │   │    ├─ elements      (BaseElement, Button, TextBox, etc.)
     │   │   │    ├─ utils         (BrowserUtils, AlertUtils, DateTimeUtils, FileUtils, LogUtils, WaitUtils)
     │   │   │    └─ reporting     (AllureListener, ScreenshotHandler)
     │   │   └─ pages              (Page Objects)
     │   └─ resources
     │        └─ config.properties (browser, headless, timeout, remote grid URL)
     └─ test
         ├─ java
         │   ├─ tests              (TestNG test classes)
         └─ resources
             └─ testng.xml         (suite + parallel config)

------------------------------------------------------------------------

## 3. **Configuration & Browser Handling**

-   Centralized **ConfigManager** reading from `config.properties` or
    environment variables.\
-   Properties:
    -   `browser=chrome|firefox|edge`\
    -   `headless=true|false`\
    -   `remote.url=http://localhost:4444/wd/hub`\
    -   `timeout=10000` (in ms)\
-   **BrowserManager / DriverFactory**:
    -   Sets up Selenide `Configuration` (browser, headless, timeout,
        start-maximized).
    -   Supports **local** and **remote execution** (Selenium Grid,
        Selenoid, Docker).

------------------------------------------------------------------------

## 4. **Element Wrappers**

-   **BaseElement** wraps `SelenideElement`.\
-   Provides reusable methods:
    -   `click()`, `type(text)`, `getText()`, `isDisplayed()`,
        `getAttribute(name)`, `getValue()`, `hover()`.\
-   Logging:
    -   Each action logs via SLF4J + Logback.\
    -   Each action annotated with Allure `@Step`.\
-   Specialized elements:
    -   `Button`, `TextBox`, `CheckBox`, `Dropdown`, `Link`, etc.

------------------------------------------------------------------------

## 5. **Smart Waits**

-   No `Thread.sleep()`.\
-   Use **Selenide conditions**:
    -   `element.shouldBe(visible)`\
    -   `element.shouldBe(clickable)`\
    -   `element.shouldNotBe(visible)`\
-   Centralized `WaitUtils` with helper methods:
    -   `waitForVisible(element)`\
    -   `waitForClickable(element)`\
    -   `waitForDisappear(element)`

------------------------------------------------------------------------

## 6. **Utilities**

-   **BrowserUtils** → open/close browser, switch tabs, maximize,
    screenshots.\
-   **AlertUtils** → handle JS alerts (accept, dismiss, get text).\
-   **DateTimeUtils** → formatting, adding/subtracting days,
    timestamps.\
-   **FileUtils** → read/write files, check download, cleanup.\
-   **LogUtils** → standardized log messages.\
-   **WaitUtils** → encapsulated waits as above.

------------------------------------------------------------------------

## 7. **Reporting**

-   **Allure TestNG Adapter** integrated via Maven.\
-   Requirements:
    -   Log steps with `@Step`.\
    -   Auto-screenshot on failure.\
    -   Attach console log / page source when needed.\
    -   Show environment info (OS, browser, Java version).\
-   Output: `allure-results/` → `allure serve` → HTML report.

------------------------------------------------------------------------

## 8. **Parallel Execution**

-   TestNG XML suite with `parallel="tests"` or `parallel="classes"`.\
-   Thread safety:
    -   Selenide manages WebDriver instances per thread.\
-   Ensure test data is isolated (avoid shared mutable static vars).

------------------------------------------------------------------------

## 9. **Logging**

-   **Logback** configuration (`logback.xml`).\
-   Console + file logging:
    -   Example:
        `2025-09-18 12:33:22 INFO  [main] Button - Clicking on [Login Button]`.\
-   Log file path: `/logs/test.log`.

------------------------------------------------------------------------

## 10. **Error Handling & Recovery**

-   On failure:
    -   Capture screenshot.\
    -   Attach logs.\
    -   Optionally capture DOM snapshot.\
-   Retry mechanism (optional via TestNG `IRetryAnalyzer`).

------------------------------------------------------------------------

## 11. **CI/CD Integration**

-   Run tests in **GitHub Actions / Jenkins**.\
-   Store **Allure reports as build artifacts**.\
-   Option to run against **Dockerized Selenium Grid/Selenoid** for
    scaling.

------------------------------------------------------------------------

## 12. **Future Enhancements (Optional)**

-   **Data-driven testing** with JSON/Excel/CSV.\
-   **Environment switching** (`qa`, `staging`, `prod`) via profiles.\
-   **Docker support** for running tests in containers.\
-   **Parallel cross-browser execution** on cloud providers (e.g.,
    BrowserStack, LambdaTest).

------------------------------------------------------------------------

## ✅ Acceptance Criteria

1.  Framework can run tests on Chrome, Firefox, Edge (local & remote).\
2.  Wrapper logs each action (console, file, Allure).\
3.  Waits are handled via conditions (no sleeps).\
4.  Utilities cover common needs (Browser, Alert, File, DateTime, Log).\
5.  Allure report generates with steps + screenshots.\
6.  Tests run in parallel with isolation.\
7.  Failures capture screenshots automatically.\
8.  CI pipeline integrates with test execution + reporting.
