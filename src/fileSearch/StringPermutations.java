package fileSearch;

import java.util.*;

public class StringPermutations {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Get user input
        System.out.print("Enter a string to generate permutations: ");
        String input = scanner.nextLine();
        
        if (input.isEmpty()) {
            System.out.println("Input string is empty. Please enter a valid string.");
            return;
        }
        
        // Get option for including duplicates
        System.out.print("Do you want to exclude duplicates? (yes/no): ");
        String excludeDuplicates = scanner.nextLine();
        boolean excludeDup = excludeDuplicates.equalsIgnoreCase("yes");

        // Generate and print permutations
        System.out.println("\nRecursive Permutations:");
        List<String> recursivePerms = generatePermutations(input, excludeDup);
        for (String perm : recursivePerms) {
            System.out.println(perm);
        }

        System.out.println("\nIterative Permutations:");
        List<String> iterativePerms = generatePermutationsIteratively(input, excludeDup);
        for (String perm : iterativePerms) {
            System.out.println(perm);
        }

        // Time Complexity Analysis
        System.out.println("\nTime Complexity:");
        System.out.println("Both the recursive and iterative algorithms have a time complexity of O(n * n!).");

        // Close scanner
        scanner.close();
    }

    // Recursive method to generate permutations
    public static List<String> generatePermutations(String str, boolean excludeDup) {
        Set<String> permutations = new HashSet<>();
        generatePermutationsHelper("", str, permutations);
        return new ArrayList<>(permutations); // Return unique permutations
    }

    // Helper method for recursion
    private static void generatePermutationsHelper(String prefix, String remaining, Set<String> permutations) {
        if (remaining.length() == 0) {
            permutations.add(prefix);
        } else {
            for (int i = 0; i < remaining.length(); i++) {
                // Recursive call with updated prefix and remaining string
                generatePermutationsHelper(prefix + remaining.charAt(i), remaining.substring(0, i) + remaining.substring(i + 1), permutations);
            }
        }
    }

    // Iterative method to generate permutations
    public static List<String> generatePermutationsIteratively(String str, boolean excludeDup) {
        Set<String> permutations = new HashSet<>();
        int n = str.length();
        List<Character> chars = new ArrayList<>();

        // Add characters of the string to a list
        for (char c : str.toCharArray()) {
            chars.add(c);
        }

        // Sort the list to ensure duplicates are adjacent
        Collections.sort(chars);
        
        while (true) {
            // Generate the current permutation
            StringBuilder currentPermutation = new StringBuilder();
            for (char c : chars) {
                currentPermutation.append(c);
            }
            permutations.add(currentPermutation.toString());

            // Find the rightmost character that can be incremented
            int i = n - 1;
            while (i > 0 && chars.get(i - 1) >= chars.get(i)) {
                i--;
            }

            // If no such character found, all permutations generated
            if (i == 0) break;

            // Find the rightmost character that exceeds chars[i-1]
            int j = n - 1;
            while (chars.get(j) <= chars.get(i - 1)) {
                j--;
            }

            // Swap the pivot with j
            Collections.swap(chars, i - 1, j);

            // Reverse the sequence from i to end
            Collections.reverse(chars.subList(i, n));
        }

        return new ArrayList<>(permutations); // Return unique permutations
    }
}
