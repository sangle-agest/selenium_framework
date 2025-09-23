# The Internet Test Suite - Comprehensive Framework Validation

This test suite demonstrates comprehensive automation testing capabilities using the enhanced Selenium framework with https://the-internet.herokuapp.com/ - a website designed specifically for testing various web automation scenarios.

## Overview

The test suite validates all framework enhancements and element interactions through real-world scenarios available on The Internet website. It demonstrates advanced automation capabilities including drag-and-drop, file uploads, JavaScript execution, window management, and more.

## Test Structure

### 1. TheInternetTests.java
**Purpose**: Core UI interaction testing  
**Test Scenarios**:
- ✅ **Checkbox Interactions** (`testCheckboxInteractions`)
  - Individual checkbox check/uncheck operations
  - Toggle functionality testing
  - State verification and counting
  - Bulk operations (check all/uncheck all)

- ✅ **Dropdown Interactions** (`testDropdownInteractions`)
  - Option selection by text and value
  - Option availability verification
  - Selected option retrieval
  - Multiple option handling

- ✅ **Drag and Drop** (`testDragAndDropInteractions`)
  - Column position verification
  - Drag and drop operations using Actions class
  - Position change validation
  - Reset functionality testing

- ✅ **Basic Navigation** (`testBasicNavigation`)
  - Page navigation verification
  - Title validation
  - Multi-page workflow testing

- ✅ **Framework Capabilities** (`testFrameworkCapabilities`)
  - Logging system demonstration
  - Screenshot capture validation
  - Test data collection and reporting
  - Configuration access testing

### 2. AdvancedElementsTests.java
**Purpose**: Specialized element and complex interaction testing  
**Test Scenarios**:
- ✅ **Table Interactions** (`testTableInteractions`)
  - Cell data access and verification
  - Row finding by column content
  - Table structure validation
  - Data extraction capabilities
  - Sorting functionality (when available)

- ✅ **Slider Interactions** (`testSliderInteractions`)
  - Value setting with precision
  - Step-based movement
  - Percentage positioning
  - Edge value testing (min/max)
  - Real-time value display verification

- ✅ **Advanced Button Operations** (`testAdvancedButtonInteractions`)
  - Dynamic state changes
  - Button properties (location, size)
  - Enable/disable functionality
  - Text change verification

- ✅ **Advanced Text Input** (`testAdvancedTextBoxInteractions`)
  - Text clearing and entry
  - Special key combinations (Ctrl+A, Delete)
  - Input validation
  - Dynamic enable/disable states

- ✅ **Element Properties** (`testElementProperties`)
  - Attribute access (placeholder, type, id)
  - CSS property retrieval
  - State verification (visible, enabled)
  - Property validation

### 3. SpecializedFeaturesTests.java
**Purpose**: Advanced framework features and complex scenarios  
**Test Scenarios**:
- ✅ **File Upload** (`testFileUpload`)
  - Temporary file creation and upload
  - Upload process validation
  - Success verification
  - Multi-file upload support

- ✅ **JavaScript Interactions** (`testJavaScriptInteractions`)
  - Script execution and result capture
  - DOM manipulation
  - Element highlighting
  - Scroll operations (top, bottom, into view)
  - JavaScript-based clicking

- ✅ **Window Management** (`testWindowManagement`)
  - Multi-window handling
  - Window switching operations
  - Handle management
  - Window cleanup

- ✅ **Alert Handling** (`testAlertHandling`)
  - Simple alert acceptance
  - Confirm dialog handling (accept/dismiss)
  - Prompt input and verification
  - Alert text extraction

- ✅ **Dynamic Content** (`testDynamicContentHandling`)
  - Loading state management
  - Dynamic element waiting
  - Content change verification
  - Multiple loading scenarios

- ✅ **iFrame Interactions** (`testIFrameInteractions`)
  - Frame switching
  - Content interaction within frames
  - Context switching back to main content
  - WYSIWYG editor interaction

## Framework Features Demonstrated

### Enhanced Element Classes
- **BaseElement**: Core interaction methods with drag-drop, keyboard input, property access
- **Table**: Specialized table operations with cell access and data extraction
- **Slider**: Range input handling with precise value control
- **FileUpload**: File handling with temporary file creation
- **Button/TextBox**: Enhanced input and interaction capabilities

### Advanced Utilities
- **JavaScriptUtils**: JavaScript execution and DOM manipulation
- **WindowUtils**: Multi-window and tab management
- **AlertUtils**: JavaScript alert handling (via Selenide)
- **WaitUtils**: Enhanced waiting strategies

### Page Object Model
- **BaseInternetPage**: Common functionality for all The Internet pages
- **CheckboxesPage**: Checkbox-specific operations
- **DropdownPage**: Dropdown-specific operations
- **DragAndDropPage**: Drag and drop functionality

### Testing Infrastructure
- **Allure Reporting**: Comprehensive test reporting with screenshots
- **TestNG Configuration**: Parallel execution and group management
- **Logging System**: Detailed test execution logging
- **Screenshot Capture**: Automatic screenshot capture at key points

## Test Execution

### Running Individual Test Suites

```bash
# Run smoke tests only
mvn test -Dgroups=smoke

# Run The Internet specific tests
mvn test -Dtest=TheInternetTests

# Run advanced elements tests
mvn test -Dtest=AdvancedElementsTests

# Run specialized features tests
mvn test -Dtest=SpecializedFeaturesTests

# Run specific test group
mvn test -Dgroups=checkboxes,dropdown,dragdrop
```

### Running Full Test Suite

```bash
# Run all tests
mvn test

# Run full regression suite
mvn test -Dgroups=regression

# Run with specific browser
mvn test -Dbrowser=firefox

# Run in headless mode
mvn test -Dheadless=true
```

### TestNG XML Configurations

The suite includes multiple TestNG configurations:
- **Smoke Tests**: Quick validation of core functionality
- **The Internet Tests**: All The Internet specific scenarios
- **Advanced Elements Tests**: Complex element interactions
- **Specialized Features Tests**: Advanced framework features
- **Full Regression Tests**: Complete test coverage

## Test Data and Validation

### Comprehensive Validation Points
- ✅ Element state verification (checked, selected, enabled, visible)
- ✅ Text content validation
- ✅ Position and size verification
- ✅ Dynamic content change detection
- ✅ Multi-window/frame context validation
- ✅ File upload success verification
- ✅ JavaScript execution result validation

### Test Coverage Metrics
- **UI Elements**: Checkboxes, Dropdowns, Tables, Sliders, Buttons, Text Inputs
- **Interactions**: Click, Type, Drag-Drop, Upload, Select, Toggle
- **Advanced Features**: JavaScript execution, Window management, Alert handling
- **Dynamic Content**: Loading states, Content changes, Frame switching
- **Validation**: State verification, Content validation, Property access

## Key Testing Scenarios Covered

1. **Basic UI Interactions**: All fundamental web element operations
2. **Complex Interactions**: Drag-and-drop, multi-step workflows
3. **Dynamic Content**: Loading states, content changes
4. **File Operations**: Upload handling and validation
5. **JavaScript Integration**: DOM manipulation and script execution
6. **Multi-Context**: Windows, frames, alerts
7. **Advanced Element Types**: Tables, sliders, complex forms
8. **Error Scenarios**: Validation of edge cases and error states

## Framework Validation Results

This test suite serves as comprehensive validation that the enhanced automation framework can handle:
- ✅ All standard web element interactions
- ✅ Complex UI components (tables, sliders, drag-drop)
- ✅ Advanced browser features (file upload, JavaScript, windows)
- ✅ Dynamic content and loading states
- ✅ Multi-context scenarios (frames, windows, alerts)
- ✅ Comprehensive reporting and logging
- ✅ Parallel execution capabilities
- ✅ Cross-browser compatibility

The test suite demonstrates that the framework is production-ready for complex web automation scenarios and provides a solid foundation for building comprehensive test automation solutions.