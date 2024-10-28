package fileSearch;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RecursiveFileSearch {
    
    public static void main(String[] args) {
        // Validate command-line arguments
        if (args.length < 2) {
            System.out.println("Usage: java RecursiveFileSearch <directory path> <file names> [case-sensitive]");
            return;
        }

        String directoryPath = args[0];
        String[] fileNames = args[1].split(","); // Allow multiple file names separated by commas
        boolean caseSensitive = args.length > 2 && args[2].equalsIgnoreCase("case-sensitive");

        // Initialize the root directory
        File rootDir = new File(directoryPath);
        if (!rootDir.exists() || !rootDir.isDirectory()) {
            System.out.println("Invalid directory path: " + directoryPath);
            return;
        }

        // Initialize a map to store file names and their counts
        Map<String, Integer> fileCountMap = new HashMap<>();
        for (String fileName : fileNames) {
            fileCountMap.put(fileName.trim(), 0); // Initialize count as 0 for each file, trim whitespace
        }

        // Start searching for files
        searchFiles(rootDir, fileCountMap, caseSensitive);

        // Print results for each file searched
        printResults(fileNames, fileCountMap);
    }

    // Recursive method to search for multiple files
    public static void searchFiles(File directory, Map<String, Integer> fileCountMap, boolean caseSensitive) {
        File[] filesAndDirs = directory.listFiles();
        
        // Check if the directory is accessible
        if (filesAndDirs == null) {
            System.out.println("Unable to access directory: " + directory.getAbsolutePath());
            return;
        }

        for (File file : filesAndDirs) {
            if (file.isFile()) {
                for (String fileName : fileCountMap.keySet()) {
                    // Determine if the file matches the search criteria
                    boolean match = caseSensitive ? file.getName().equals(fileName) : file.getName().equalsIgnoreCase(fileName);
                    if (match) {
                        System.out.println("File found: " + file.getAbsolutePath());
                        fileCountMap.put(fileName, fileCountMap.get(fileName) + 1);
                    }
                }
            } else if (file.isDirectory()) {
                // Recurse into subdirectory
                searchFiles(file, fileCountMap, caseSensitive);
            }
        }
    }

    // Method to print results for each searched file
    private static void printResults(String[] fileNames, Map<String, Integer> fileCountMap) {
        for (String fileName : fileNames) {
            int count = fileCountMap.get(fileName.trim()); // Use trimmed name
            if (count > 0) {
                System.out.println("File '" + fileName.trim() + "' found " + count + " time(s).");
            } else {
                System.out.println("File '" + fileName.trim() + "' not found.");
            }
        }
    }
}
