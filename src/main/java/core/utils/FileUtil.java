package core.utils;

import core.constants.ApplicationConstants;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Properties;

/**
 * FileUtils provides utility methods for file operations
 */
public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * Read file content as string
     * @param filePath Path to file
     * @return File content as string
     */
    @Step("Read file content: {filePath}")
    public static String readFileAsString(String filePath) {
        logger.info("Reading file content: {}", filePath);
        try {
            String content = Files.readString(Paths.get(filePath));
            logger.debug("Successfully read file content, length: {} characters", content.length());
            return content;
        } catch (IOException e) {
            logger.error("Failed to read file {}: {}", filePath, e.getMessage());
            throw new RuntimeException("Failed to read file: " + filePath, e);
        }
    }

    /**
     * Read file content as lines
     * @param filePath Path to file
     * @return List of lines
     */
    @Step("Read file lines: {filePath}")
    public static List<String> readFileAsLines(String filePath) {
        logger.info("Reading file lines: {}", filePath);
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            logger.debug("Successfully read {} lines from file", lines.size());
            return lines;
        } catch (IOException e) {
            logger.error("Failed to read file lines {}: {}", filePath, e.getMessage());
            throw new RuntimeException("Failed to read file lines: " + filePath, e);
        }
    }

    /**
     * Write string content to file
     * @param filePath Path to file
     * @param content Content to write
     */
    @Step("Write content to file: {filePath}")
    public static void writeStringToFile(String filePath, String content) {
        logger.info("Writing content to file: {}", filePath);
        try {
            // Create parent directories if they don't exist
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            
            Files.writeString(path, content);
            logger.debug("Successfully wrote content to file, length: {} characters", content.length());
        } catch (IOException e) {
            logger.error("Failed to write to file {}: {}", filePath, e.getMessage());
            throw new RuntimeException("Failed to write to file: " + filePath, e);
        }
    }

    /**
     * Write lines to file
     * @param filePath Path to file
     * @param lines Lines to write
     */
    @Step("Write lines to file: {filePath}")
    public static void writeLinesToFile(String filePath, List<String> lines) {
        logger.info("Writing {} lines to file: {}", lines.size(), filePath);
        try {
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            
            Files.write(path, lines);
            logger.debug("Successfully wrote {} lines to file", lines.size());
        } catch (IOException e) {
            logger.error("Failed to write lines to file {}: {}", filePath, e.getMessage());
            throw new RuntimeException("Failed to write lines to file: " + filePath, e);
        }
    }

    /**
     * Append content to file
     * @param filePath Path to file
     * @param content Content to append
     */
    @Step("Append content to file: {filePath}")
    public static void appendToFile(String filePath, String content) {
        logger.info("Appending content to file: {}", filePath);
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }
            
            try (FileWriter writer = new FileWriter(filePath, true)) {
                writer.write(content);
            }
            logger.debug("Successfully appended content to file");
        } catch (IOException e) {
            logger.error("Failed to append to file {}: {}", filePath, e.getMessage());
            throw new RuntimeException("Failed to append to file: " + filePath, e);
        }
    }

    /**
     * Check if file exists
     * @param filePath Path to file
     * @return True if file exists
     */
    @Step("Check if file exists: {filePath}")
    public static boolean fileExists(String filePath) {
        boolean exists = Files.exists(Paths.get(filePath));
        logger.debug("File {} exists: {}", filePath, exists);
        return exists;
    }

    /**
     * Check if directory exists
     * @param dirPath Path to directory
     * @return True if directory exists
     */
    @Step("Check if directory exists: {dirPath}")
    public static boolean directoryExists(String dirPath) {
        boolean exists = Files.isDirectory(Paths.get(dirPath));
        logger.debug("Directory {} exists: {}", dirPath, exists);
        return exists;
    }

    /**
     * Create directory
     * @param dirPath Path to directory
     */
    @Step("Create directory: {dirPath}")
    public static void createDirectory(String dirPath) {
        logger.info("Creating directory: {}", dirPath);
        try {
            Files.createDirectories(Paths.get(dirPath));
            logger.debug("Successfully created directory");
        } catch (IOException e) {
            logger.error("Failed to create directory {}: {}", dirPath, e.getMessage());
            throw new RuntimeException("Failed to create directory: " + dirPath, e);
        }
    }

    /**
     * Delete file
     * @param filePath Path to file
     */
    @Step("Delete file: {filePath}")
    public static void deleteFile(String filePath) {
        logger.info("Deleting file: {}", filePath);
        try {
            Files.deleteIfExists(Paths.get(filePath));
            logger.debug("Successfully deleted file");
        } catch (IOException e) {
            logger.error("Failed to delete file {}: {}", filePath, e.getMessage());
            throw new RuntimeException("Failed to delete file: " + filePath, e);
        }
    }

    /**
     * Delete directory and all its contents
     * @param dirPath Path to directory
     */
    @Step("Delete directory: {dirPath}")
    public static void deleteDirectory(String dirPath) {
        logger.info("Deleting directory: {}", dirPath);
        try {
            File directory = new File(dirPath);
            if (directory.exists()) {
                FileUtils.deleteDirectory(directory);
                logger.debug("Successfully deleted directory");
            } else {
                logger.debug("Directory does not exist");
            }
        } catch (IOException e) {
            logger.error("Failed to delete directory {}: {}", dirPath, e.getMessage());
            throw new RuntimeException("Failed to delete directory: " + dirPath, e);
        }
    }

    /**
     * Copy file
     * @param sourcePath Source file path
     * @param targetPath Target file path
     */
    @Step("Copy file from {sourcePath} to {targetPath}")
    public static void copyFile(String sourcePath, String targetPath) {
        logger.info("Copying file from {} to {}", sourcePath, targetPath);
        try {
            Path source = Paths.get(sourcePath);
            Path target = Paths.get(targetPath);
            
            // Create parent directories if they don't exist
            Files.createDirectories(target.getParent());
            
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            logger.debug("Successfully copied file");
        } catch (IOException e) {
            logger.error("Failed to copy file from {} to {}: {}", sourcePath, targetPath, e.getMessage());
            throw new RuntimeException("Failed to copy file", e);
        }
    }

    /**
     * Move file
     * @param sourcePath Source file path
     * @param targetPath Target file path
     */
    @Step("Move file from {sourcePath} to {targetPath}")
    public static void moveFile(String sourcePath, String targetPath) {
        logger.info("Moving file from {} to {}", sourcePath, targetPath);
        try {
            // Handle file URI paths
            String actualSourcePath = sourcePath;
            if (sourcePath.startsWith("file://")) {
                actualSourcePath = sourcePath.replace("file://", "");
            }
            
            Path source = Paths.get(actualSourcePath);
            Path target = Paths.get(targetPath);
            
            // Check if source file exists
            if (!Files.exists(source)) {
                logger.warn("Source file does not exist: {}", actualSourcePath);
                return;
            }
            
            // Create parent directories if they don't exist
            Files.createDirectories(target.getParent());
            
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
            logger.debug("Successfully moved file");
        } catch (IOException e) {
            logger.error("Failed to move file from {} to {}: {}", sourcePath, targetPath, e.getMessage());
            throw new RuntimeException("Failed to move file", e);
        }
    }

    /**
     * Get file size in bytes
     * @param filePath Path to file
     * @return File size in bytes
     */
    @Step("Get file size: {filePath}")
    public static long getFileSize(String filePath) {
        logger.debug("Getting file size: {}", filePath);
        try {
            long size = Files.size(Paths.get(filePath));
            logger.debug("File size: {} bytes", size);
            return size;
        } catch (IOException e) {
            logger.error("Failed to get file size {}: {}", filePath, e.getMessage());
            throw new RuntimeException("Failed to get file size: " + filePath, e);
        }
    }

    /**
     * Wait for file to exist
     * @param filePath Path to file
     * @param timeoutMs Timeout in milliseconds
     * @return True if file exists within timeout
     */
    @Step("Wait for file to exist: {filePath}")
    public static boolean waitForFileToExist(String filePath, long timeoutMs) {
        logger.info("Waiting for file to exist: {} (timeout: {}ms)", filePath, timeoutMs);
        
        long startTime = System.currentTimeMillis();
        long endTime = startTime + timeoutMs;
        
        while (System.currentTimeMillis() < endTime) {
            if (fileExists(filePath)) {
                logger.debug("File exists");
                return true;
            }
            
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        logger.warn("File did not exist within timeout: {}", filePath);
        return false;
    }

    /**
     * Wait for file download to complete
     * @param fileName Expected file name
     * @return True if download completed
     */
    @Step("Wait for file download: {fileName}")
    public static boolean waitForDownload(String fileName) {
        return waitForDownload(fileName, 30000); // 30 seconds default timeout
    }

    /**
     * Wait for file download to complete with timeout
     * @param fileName Expected file name
     * @param timeoutMs Timeout in milliseconds
     * @return True if download completed
     */
    @Step("Wait for file download with timeout: {fileName}")
    public static boolean waitForDownload(String fileName, long timeoutMs) {
        String downloadPath = ApplicationConstants.TestConfig.DOWNLOAD_PATH;
        String fullPath = Paths.get(downloadPath, fileName).toString();
        
        logger.info("Waiting for download to complete: {} (timeout: {}ms)", fullPath, timeoutMs);
        
        return waitForFileToExist(fullPath, timeoutMs);
    }

    /**
     * Clean up downloads directory
     */
    @Step("Clean up downloads directory")
    public static void cleanupDownloads() {
        String downloadPath = ApplicationConstants.TestConfig.DOWNLOAD_PATH;
        logger.info("Cleaning up downloads directory: {}", downloadPath);
        
        try {
            if (directoryExists(downloadPath)) {
                File downloadDir = new File(downloadPath);
                File[] files = downloadDir.listFiles();
                
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile()) {
                            file.delete();
                            logger.debug("Deleted file: {}", file.getName());
                        }
                    }
                }
                logger.debug("Downloads directory cleaned up");
            }
        } catch (Exception e) {
            logger.error("Failed to cleanup downloads directory: {}", e.getMessage());
        }
    }

    /**
     * Read properties file
     * @param filePath Path to properties file
     * @return Properties object
     */
    @Step("Read properties file: {filePath}")
    public static Properties readPropertiesFile(String filePath) {
        logger.info("Reading properties file: {}", filePath);
        Properties properties = new Properties();
        
        try (InputStream inputStream = new FileInputStream(filePath)) {
            properties.load(inputStream);
            logger.debug("Successfully loaded {} properties", properties.size());
            return properties;
        } catch (IOException e) {
            logger.error("Failed to read properties file {}: {}", filePath, e.getMessage());
            throw new RuntimeException("Failed to read properties file: " + filePath, e);
        }
    }

    /**
     * Write properties to file
     * @param properties Properties object
     * @param filePath Path to properties file
     * @param comments Comments to add to file
     */
    @Step("Write properties to file: {filePath}")
    public static void writePropertiesFile(Properties properties, String filePath, String comments) {
        logger.info("Writing properties to file: {}", filePath);
        
        try {
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            
            try (OutputStream outputStream = new FileOutputStream(filePath)) {
                properties.store(outputStream, comments);
                logger.debug("Successfully wrote {} properties to file", properties.size());
            }
        } catch (IOException e) {
            logger.error("Failed to write properties file {}: {}", filePath, e.getMessage());
            throw new RuntimeException("Failed to write properties file: " + filePath, e);
        }
    }

    /**
     * Attach file to Allure report
     * @param filePath Path to file
     * @param attachmentName Name for attachment
     * @return File content as byte array
     */
    @Attachment(value = "{attachmentName}", type = "application/octet-stream")
    public static byte[] attachFileToAllure(String filePath, String attachmentName) {
        logger.info("Attaching file to Allure: {} as {}", filePath, attachmentName);
        try {
            byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
            logger.debug("Successfully attached file to Allure, size: {} bytes", fileContent.length);
            return fileContent;
        } catch (IOException e) {
            logger.error("Failed to attach file to Allure {}: {}", filePath, e.getMessage());
            return new byte[0];
        }
    }

    /**
     * Get file extension
     * @param fileName File name
     * @return File extension (without dot)
     */
    @Step("Get file extension: {fileName}")
    public static String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return "";
        }
        
        String extension = fileName.substring(lastDotIndex + 1);
        logger.debug("File extension for {}: {}", fileName, extension);
        return extension;
    }

    /**
     * Get file name without extension
     * @param fileName File name
     * @return File name without extension
     */
    @Step("Get file name without extension: {fileName}")
    public static String getFileNameWithoutExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return fileName;
        }
        
        String nameWithoutExt = fileName.substring(0, lastDotIndex);
        logger.debug("File name without extension for {}: {}", fileName, nameWithoutExt);
        return nameWithoutExt;
    }
}