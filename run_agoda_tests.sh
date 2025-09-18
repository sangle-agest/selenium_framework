#!/bin/bash

# Agoda Test Execution Script
# This script runs all Agoda tests and generates reports

echo "======================================"
echo "    AGODA TEST SUITE EXECUTION"
echo "======================================"
echo ""

# Set variables
PROJECT_DIR="/home/sangle/Documents/selenium_framework"
AGODA_TEST_PACKAGE="tests.agoda"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
RESULTS_DIR="$PROJECT_DIR/test-results/agoda"
REPORT_FILE="$RESULTS_DIR/agoda_test_report_$TIMESTAMP.md"

# Create results directory if it doesn't exist
mkdir -p "$RESULTS_DIR"

echo "Starting Agoda test execution at: $(date)"
echo "Results will be saved to: $RESULTS_DIR"
echo ""

# Navigate to project directory
cd "$PROJECT_DIR" || exit 1

# Clean and compile project
echo "🔧 Cleaning and compiling project..."
mvn clean compile -q
if [ $? -ne 0 ]; then
    echo "❌ Compilation failed!"
    exit 1
fi
echo "✅ Compilation successful"
echo ""

# Run individual test cases
echo "🚀 Running Agoda Test Cases..."
echo ""

# TC01 - Search and Sort Hotel Test
echo "📋 Running TC01: Search and Sort Hotel Test"
mvn test -Dtest=TC01_SearchAndSortHotelTest -q > "$RESULTS_DIR/tc01_output.log" 2>&1
TC01_RESULT=$?

# TC02 - Search and Filter Hotel Test  
echo "📋 Running TC02: Search and Filter Hotel Test"
mvn test -Dtest=TC02_SearchAndFilterHotelTest -q > "$RESULTS_DIR/tc02_output.log" 2>&1
TC02_RESULT=$?

# Run all Agoda tests together
echo "📋 Running All Agoda Tests Together"
mvn test -Dtest="tests.agoda.*Test" -q > "$RESULTS_DIR/all_agoda_tests.log" 2>&1
ALL_TESTS_RESULT=$?

echo ""
echo "🎯 Test execution completed at: $(date)"
echo ""

# Generate Markdown Report
echo "📄 Generating test report..."

cat > "$REPORT_FILE" << EOF
# Agoda Test Suite Execution Report

**Execution Date:** $(date)  
**Project:** Selenium Framework - Agoda Tests  
**Branch:** agoda  

## Test Summary

| Test Case | Status | Description |
|-----------|--------|-------------|
| TC01_SearchAndSortHotelTest | $([ $TC01_RESULT -eq 0 ] && echo "✅ PASSED" || echo "❌ FAILED") | Search for hotels in Phuket and sort by price |
| TC02_SearchAndFilterHotelTest | $([ $TC02_RESULT -eq 0 ] && echo "✅ PASSED" || echo "❌ FAILED") | Search for hotels and apply Resort + 5-star filters |
| All Agoda Tests | $([ $ALL_TESTS_RESULT -eq 0 ] && echo "✅ PASSED" || echo "❌ FAILED") | Complete Agoda test suite execution |

## Test Cases Details

### TC01: Search and Sort Hotel Test
**Objective:** Verify hotel search and sorting functionality  
**Steps:**
1. Navigate to agoda.com
2. Input destination: Phuket  
3. Input dates: Next Friday + 3 days
4. Input travelers: Family Travelers → 2 rooms, 4 adults
5. Click Search
6. Sort by: Price (low to high)
7. Verify: Hotels are displayed and sorted

**Result:** $([ $TC01_RESULT -eq 0 ] && echo "PASSED" || echo "FAILED")

### TC02: Search and Filter Hotel Test  
**Objective:** Verify hotel search and filtering functionality  
**Steps:**
1. Navigate to agoda.com
2. Input destination: Phuket
3. Input dates: Next Friday + 3 days  
4. Input travelers: Family Travelers → 2 rooms, 4 adults
5. Click Search
6. Filter by: Property type - Resort
7. Filter by: Star rating - 5 stars
8. Verify: Hotels are displayed with applied filters

**Result:** $([ $TC02_RESULT -eq 0 ] && echo "PASSED" || echo "FAILED")

## Execution Commands Used

\`\`\`bash
# Individual test execution
mvn test -Dtest=TC01_SearchAndSortHotelTest
mvn test -Dtest=TC02_SearchAndFilterHotelTest

# All Agoda tests execution  
mvn test -Dtest="tests.agoda.*Test"

# With specific browser (if needed)
mvn test -Dtest="tests.agoda.*Test" -Dbrowser=chrome
mvn test -Dtest="tests.agoda.*Test" -Dbrowser=firefox

# Headless execution
mvn test -Dtest="tests.agoda.*Test" -Dheadless=true
\`\`\`

## Test Architecture

- **Base Class:** \`AgodaBaseTest\` - Provides Agoda-specific setup and teardown
- **Page Objects:** \`AgodaHomePage\`, \`AgodaSearchResultsPage\`
- **Utilities:** \`DateTimeUtils\`, \`DayOfWeekEnum\` for flexible date handling
- **Reporting:** Allure reports with screenshots on failure

## Files Generated

- Test logs: \`$RESULTS_DIR/\`
- Screenshots: \`target/screenshots/\` (on failure)  
- Allure results: \`target/allure-results/\`
- Surefire reports: \`target/surefire-reports/\`

## Quick Commands

\`\`\`bash
# Run this script
./run_agoda_tests.sh

# View latest report
cat $RESULTS_DIR/agoda_test_report_*.md | tail -1

# Generate Allure report  
mvn allure:serve
\`\`\`

---
*Generated automatically by Agoda Test Execution Script*
EOF

echo "✅ Test report generated: $REPORT_FILE"
echo ""

# Display summary
echo "======================================"
echo "         EXECUTION SUMMARY"
echo "======================================"
echo "TC01 Search and Sort: $([ $TC01_RESULT -eq 0 ] && echo "✅ PASSED" || echo "❌ FAILED")"
echo "TC02 Search and Filter: $([ $TC02_RESULT -eq 0 ] && echo "✅ PASSED" || echo "❌ FAILED")" 
echo "All Agoda Tests: $([ $ALL_TESTS_RESULT -eq 0 ] && echo "✅ PASSED" || echo "❌ FAILED")"
echo ""
echo "📄 Report: $REPORT_FILE"
echo "📁 Logs: $RESULTS_DIR/"
echo ""

# Open report if possible
if command -v code &> /dev/null; then
    echo "📖 Opening report in VS Code..."
    code "$REPORT_FILE"
fi

echo "🎉 Agoda test execution completed!"