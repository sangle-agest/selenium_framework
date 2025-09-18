package core.elements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * FileUpload element wrapper for handling file upload inputs
 */
public class FileUpload extends BaseElement {

    /**
     * Constructor for FileUpload element
     * @param element SelenideElement representing the file input
     * @param elementName Name for logging and reporting
     */
    public FileUpload(SelenideElement element, String elementName) {
        super(element, elementName);
    }

    /**
     * Constructor for FileUpload element with default name
     * @param element SelenideElement representing the file input
     */
    public FileUpload(SelenideElement element) {
        super(element, "FileUpload");
    }

    /**
     * Upload file by file path
     * @param filePath Path to file to upload
     * @return This FileUpload for method chaining
     */
    @Step("Upload file to [{elementName}]: {filePath}")
    public FileUpload uploadFile(String filePath) {
        logger.info("Uploading file to [{}]: {}", elementName, filePath);
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new RuntimeException("File not found: " + filePath);
            }
            if (!file.canRead()) {
                throw new RuntimeException("File is not readable: " + filePath);
            }
            
            element.shouldBe(Condition.visible).uploadFile(file);
            logger.debug("Successfully uploaded file to [{}]: {}", elementName, filePath);
        } catch (Exception e) {
            logger.error("Failed to upload file to [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to upload file to " + elementName, e);
        }
        return this;
    }

    /**
     * Upload multiple files
     * @param filePaths Array of file paths to upload
     * @return This FileUpload for method chaining
     */
    @Step("Upload multiple files to [{elementName}]")
    public FileUpload uploadFiles(String... filePaths) {
        logger.info("Uploading {} files to [{}]", filePaths.length, elementName);
        try {
            File[] files = new File[filePaths.length];
            for (int i = 0; i < filePaths.length; i++) {
                File file = new File(filePaths[i]);
                if (!file.exists()) {
                    throw new RuntimeException("File not found: " + filePaths[i]);
                }
                if (!file.canRead()) {
                    throw new RuntimeException("File is not readable: " + filePaths[i]);
                }
                files[i] = file;
            }
            
            element.shouldBe(Condition.visible).uploadFile(files);
            logger.debug("Successfully uploaded {} files to [{}]", filePaths.length, elementName);
        } catch (Exception e) {
            logger.error("Failed to upload files to [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to upload files to " + elementName, e);
        }
        return this;
    }

    /**
     * Create a temporary file with specific content and upload it
     * @param fileName Name for the temporary file
     * @param content Content to write to the file
     * @return This FileUpload for method chaining
     */
    @Step("Create and upload temporary file [{fileName}] to [{elementName}]")
    public FileUpload uploadTemporaryFile(String fileName, String content) {
        logger.info("Creating and uploading temporary file [{}] to [{}]", fileName, elementName);
        try {
            // Create temporary file
            Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));
            Path tempFile = tempDir.resolve(fileName);
            Files.write(tempFile, content.getBytes());
            
            // Upload the file
            uploadFile(tempFile.toString());
            
            // Mark for deletion on exit
            tempFile.toFile().deleteOnExit();
            
            logger.debug("Successfully created and uploaded temporary file [{}] to [{}]", fileName, elementName);
        } catch (Exception e) {
            logger.error("Failed to create and upload temporary file to [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to create and upload temporary file to " + elementName, e);
        }
        return this;
    }

    /**
     * Get the uploaded file name (if visible in UI)
     * @return Uploaded file name
     */
    @Step("Get uploaded file name from [{elementName}]")
    public String getUploadedFileName() {
        logger.info("Getting uploaded file name from [{}]", elementName);
        try {
            // Try to get value attribute which often contains the file name
            String fileName = element.getValue();
            if (fileName != null && !fileName.isEmpty()) {
                // Extract just the file name from full path
                if (fileName.contains("\\")) {
                    fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
                } else if (fileName.contains("/")) {
                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                }
            }
            logger.debug("Uploaded file name from [{}]: {}", elementName, fileName);
            return fileName != null ? fileName : "";
        } catch (Exception e) {
            logger.error("Failed to get uploaded file name from [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to get uploaded file name from " + elementName, e);
        }
    }

    /**
     * Clear the file upload input
     * @return This FileUpload for method chaining
     */
    @Step("Clear file upload [{elementName}]")
    public FileUpload clear() {
        logger.info("Clearing file upload [{}]", elementName);
        try {
            element.shouldBe(Condition.visible).clear();
            logger.debug("Successfully cleared file upload [{}]", elementName);
        } catch (Exception e) {
            logger.error("Failed to clear file upload [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to clear file upload " + elementName, e);
        }
        return this;
    }

    /**
     * Check if file upload accepts specific file type
     * @param fileType File type/extension to check (e.g., ".jpg", ".pdf")
     * @return True if file type is accepted
     */
    @Step("Check if file upload [{elementName}] accepts file type {fileType}")
    public boolean acceptsFileType(String fileType) {
        logger.info("Checking if file upload [{}] accepts file type {}", elementName, fileType);
        try {
            String acceptAttribute = element.getAttribute("accept");
            if (acceptAttribute == null) {
                logger.debug("File upload [{}] has no accept attribute - likely accepts all types", elementName);
                return true;
            }
            
            boolean accepts = acceptAttribute.toLowerCase().contains(fileType.toLowerCase());
            logger.debug("File upload [{}] accepts {}: {}", elementName, fileType, accepts);
            return accepts;
        } catch (Exception e) {
            logger.error("Failed to check file type acceptance for [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to check file type acceptance for " + elementName, e);
        }
    }

    /**
     * Check if multiple file upload is supported
     * @return True if multiple files can be uploaded
     */
    @Step("Check if file upload [{elementName}] supports multiple files")
    public boolean supportsMultipleFiles() {
        logger.info("Checking if file upload [{}] supports multiple files", elementName);
        try {
            String multipleAttribute = element.getAttribute("multiple");
            boolean supports = multipleAttribute != null;
            logger.debug("File upload [{}] supports multiple files: {}", elementName, supports);
            return supports;
        } catch (Exception e) {
            logger.error("Failed to check multiple file support for [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to check multiple file support for " + elementName, e);
        }
    }

    /**
     * Check if file upload is required
     * @return True if file upload is required
     */
    @Step("Check if file upload [{elementName}] is required")
    public boolean isRequired() {
        logger.info("Checking if file upload [{}] is required", elementName);
        try {
            String requiredAttribute = element.getAttribute("required");
            boolean required = requiredAttribute != null;
            logger.debug("File upload [{}] is required: {}", elementName, required);
            return required;
        } catch (Exception e) {
            logger.error("Failed to check if file upload [{}] is required: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to check if file upload " + elementName + " is required", e);
        }
    }

    /**
     * Get accepted file types from accept attribute
     * @return String containing accepted file types
     */
    @Step("Get accepted file types for [{elementName}]")
    public String getAcceptedFileTypes() {
        logger.info("Getting accepted file types for [{}]", elementName);
        try {
            String acceptedTypes = element.getAttribute("accept");
            if (acceptedTypes == null) {
                acceptedTypes = "All file types";
            }
            logger.debug("Accepted file types for [{}]: {}", elementName, acceptedTypes);
            return acceptedTypes;
        } catch (Exception e) {
            logger.error("Failed to get accepted file types for [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to get accepted file types for " + elementName, e);
        }
    }
}