# Agoda Test Execution Commands

This document contains Maven CLI commands to run Agoda test cases individually or as a suite.

## Individual Test Cases

### TC01 - Search and Sort Hotel Test
```bash
mvn test -Dtest=TC01_SearchAndSortHotelTest
```

### TC02 - Search and Filter Hotel Test
```bash
mvn test -Dtest=TC02_SearchAndFilterHotelTest
```

### TC03 - Flexible Date Search Test (if available)
```bash
mvn test -Dtest=TC03_FlexibleDateSearchTest
```

## Run All Agoda Tests

### All tests in agoda package
```bash
mvn test -Dtest="tests.agoda.*Test"
```

### Alternative syntax for all Agoda tests
```bash
mvn test -Dtest="TC*Test" -DfailIfNoTests=false
```

## Test Execution with Different Browsers

### Chrome Browser (default)
```bash
mvn test -Dtest=TC01_SearchAndSortHotelTest -Dbrowser=chrome
```

### Firefox Browser
```bash
mvn test -Dtest=TC01_SearchAndSortHotelTest -Dbrowser=firefox
```

### Edge Browser
```bash
mvn test -Dtest=TC01_SearchAndSortHotelTest -Dbrowser=edge
```

## Headless Execution

### Run in headless mode
```bash
mvn test -Dtest="tests.agoda.*Test" -Dheadless=true
```

### Run with custom window size in headless
```bash
mvn test -Dtest="tests.agoda.*Test" -Dheadless=true -Dwindow.size=1920x1080
```

## Quiet Mode (Minimal Output)

### Quiet execution
```bash
mvn test -Dtest=TC01_SearchAndSortHotelTest -q
```

### Very quiet (errors only)
```bash
mvn test -Dtest=TC01_SearchAndSortHotelTest -q -DtrimStackTrace=false
```

## Parallel Execution

### Run tests in parallel
```bash
mvn test -Dtest="tests.agoda.*Test" -DthreadCount=2 -Dparallel=methods
```

## Test with Specific TestNG Groups (if configured)

### Run smoke tests only
```bash
mvn test -Dtest="tests.agoda.*Test" -Dgroups=smoke
```

### Run regression tests
```bash
mvn test -Dtest="tests.agoda.*Test" -Dgroups=regression
```

## Clean and Test

### Clean before running tests
```bash
mvn clean test -Dtest="tests.agoda.*Test"
```

## Generate Reports

### Run tests and generate Allure report
```bash
mvn clean test -Dtest="tests.agoda.*Test"
mvn allure:serve
```

### Generate Surefire report
```bash
mvn clean test -Dtest="tests.agoda.*Test"
mvn surefire-report:report
```

## Debug Mode

### Run with debug information
```bash
mvn test -Dtest=TC01_SearchAndSortHotelTest -X
```

### Run with Maven debug
```bash
mvn test -Dtest=TC01_SearchAndSortHotelTest -Dmaven.surefire.debug="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000"
```

## Environment-Specific Execution

### Run with specific environment
```bash
mvn test -Dtest="tests.agoda.*Test" -Denvironment=staging
```

### Run with custom base URL
```bash
mvn test -Dtest="tests.agoda.*Test" -Dbase.url=https://www.agoda.com
```

## Output Redirection

### Save output to file
```bash
mvn test -Dtest="tests.agoda.*Test" > agoda_test_results.log 2>&1
```

### Save only errors to file
```bash
mvn test -Dtest="tests.agoda.*Test" 2> agoda_test_errors.log
```

## Quick Reference Commands

| Purpose | Command |
|---------|---------|
| Run TC01 | `mvn test -Dtest=TC01_SearchAndSortHotelTest` |
| Run TC02 | `mvn test -Dtest=TC02_SearchAndFilterHotelTest` |
| Run All Agoda | `mvn test -Dtest="tests.agoda.*Test"` |
| Headless All | `mvn test -Dtest="tests.agoda.*Test" -Dheadless=true` |
| Quiet Mode | `mvn test -Dtest="tests.agoda.*Test" -q` |
| With Firefox | `mvn test -Dtest="tests.agoda.*Test" -Dbrowser=firefox` |
| Clean + Test | `mvn clean test -Dtest="tests.agoda.*Test"` |

## Test Class Information

- **Base Class**: `AgodaBaseTest` - Provides Agoda-specific setup/teardown
- **Test Package**: `tests.agoda`
- **Page Objects**: `AgodaHomePage`, `AgodaSearchResultsPage`
- **Test Architecture**: Extends AgodaBaseTest for automatic Agoda navigation

## Notes

1. All Agoda tests extend `AgodaBaseTest` which automatically navigates to Agoda homepage
2. Tests use Selenide for web automation
3. Allure reports are generated in `target/allure-results/`
4. Screenshots are captured on test failures in `target/screenshots/`
5. TestNG is used as the testing framework

---
*Generated for Agoda Test Suite - Selenium Framework*