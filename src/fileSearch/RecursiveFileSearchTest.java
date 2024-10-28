package fileSearch;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class RecursiveFileSearchTest {

    private File rootDirectory;
    private Map<String, Integer> fileCountMap;

    @BeforeEach
    public void setUp() throws IOException {
        rootDirectory = new File("C:/Users/emana/eclipse-workspace/RecursiveFileSearch/testDir");

        if (rootDirectory.exists()) {
            deleteDirectory(rootDirectory);
        }
        rootDirectory.mkdirs();

        fileCountMap = new HashMap<>();

        File subDir1 = new File(rootDirectory, "subDir1");
        File subDir2 = new File(rootDirectory, "subDir2");
        File nestedDir = new File(subDir1, "nestedDir");

        subDir1.mkdir();
        subDir2.mkdir();
        nestedDir.mkdir();

        Files.createFile(Paths.get(rootDirectory.getPath(), "testFile1.txt"));
        Files.createFile(Paths.get(subDir1.getPath(), "TestFile3.txt"));
        Files.createFile(Paths.get(subDir2.getPath(), "testFile2.txt"));
        Files.createFile(Paths.get(subDir2.getPath(), "TestFile3.txt"));
        Files.createFile(Paths.get(nestedDir.getPath(), "nestedFile.txt"));
    }

    @AfterEach
    public void tearDown() {
        deleteDirectory(rootDirectory);
    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }

    @Test
    public void testFileFoundInRootDirectory() {
        fileCountMap.put("testFile1.txt", 0);
        RecursiveFileSearch.searchFiles(rootDirectory, fileCountMap, true);
        assertEquals(1, fileCountMap.get("testFile1.txt"), 
                     "Expected 1 occurrence of 'testFile1.txt' in root directory.");
    }

    @Test
    public void testFileFoundInSubdirectory() {
        fileCountMap.put("TestFile3.txt", 0);
        RecursiveFileSearch.searchFiles(rootDirectory, fileCountMap, true);
        assertEquals(2, fileCountMap.get("TestFile3.txt"), 
                     "Expected 2 occurrences of 'TestFile3.txt' in subdirectories.");
    }

    @Test
    public void testMultipleOccurrencesOfFile() {
        fileCountMap.put("TestFile3.txt", 0);
        RecursiveFileSearch.searchFiles(rootDirectory, fileCountMap, true);
        assertEquals(2, fileCountMap.get("TestFile3.txt"), 
                     "Expected 2 occurrences of 'TestFile3.txt' across all directories.");
    }

    @Test
    public void testCaseInsensitiveSearch() {
        fileCountMap.put("testfile1.txt", 0);
        RecursiveFileSearch.searchFiles(rootDirectory, fileCountMap, false);
        assertEquals(1, fileCountMap.get("testfile1.txt"), 
                     "Expected 1 occurrence of 'testfile1.txt' (case insensitive search).");
    }

    @Test
    public void testCaseSensitiveSearch() {
        fileCountMap.put("testfile1.txt", 0);
        RecursiveFileSearch.searchFiles(rootDirectory, fileCountMap, true);
        assertEquals(0, fileCountMap.get("testfile1.txt"), 
                     "Expected 0 occurrences of 'testfile1.txt' (case sensitive search).");
    }

    @Test
    public void testFileNotFound() {
        fileCountMap.put("nonexistentFile.txt", 0);
        RecursiveFileSearch.searchFiles(rootDirectory, fileCountMap, true);
        assertEquals(0, fileCountMap.get("nonexistentFile.txt"), 
                     "Expected 0 occurrences of 'nonexistentFile.txt' (file should not exist).");
    }

    @Test
    public void testDirectoryNotFound() {
        File nonExistentDirectory = new File("C:/Users/emana/eclipse-workspace/RecursiveFileSearch/nonExistentDir");
        fileCountMap.put("somefile.txt", 0);

        assertDoesNotThrow(() -> {
            RecursiveFileSearch.searchFiles(nonExistentDirectory, fileCountMap, true);
        }, "Should handle non-existent directory gracefully.");
        assertEquals(0, fileCountMap.get("somefile.txt"), 
                     "Expected 0 occurrences of 'somefile.txt' since directory does not exist.");
    }
}
