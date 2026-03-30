import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.nio.file.*;

public class autograde {
    private static class TestResult {
        String testName;
        boolean passed;
        String errorMessage;
        
        TestResult(String testName, boolean passed, String errorMessage) {
            this.testName = testName;
            this.passed = passed;
            this.errorMessage = errorMessage;
        }
    }
    
    private static List<TestResult> results = new ArrayList<>();
    private static int passedTests = 0;
    private static int totalTests = 0;
    
    public static void main(String[] args) {
        System.out.println("=== Automated Grading System ===\n");
        
        try {
            // Load the student's submission
            String studentClassPath = args.length > 0 ? args[0] : "StudentSubmission";
            runTests(studentClassPath);
            
            // Print results
            printResults();
        } catch (Exception e) {
            System.err.println("Error during grading: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void runTests(String className) {
        try {
            Class<?> studentClass = Class.forName(className);
            Method[] methods = studentClass.getDeclaredMethods();
            
            for (Method method : methods) {
                if (method.getName().startsWith("test")) {
                    runSingleTest(studentClass, method);
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Could not find class " + className);
        }
    }
    
    private static void runSingleTest(Class<?> clazz, Method method) {
        totalTests++;
        String testName = method.getName();
        
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            method.setAccessible(true);
            method.invoke(instance);
            
            passedTests++;
            results.add(new TestResult(testName, true, null));
            System.out.println("✓ " + testName + " PASSED");
        } catch (InvocationTargetException e) {
            String errorMsg = e.getCause().getMessage();
            results.add(new TestResult(testName, false, errorMsg));
            System.out.println("✗ " + testName + " FAILED: " + errorMsg);
        } catch (Exception e) {
            results.add(new TestResult(testName, false, e.getMessage()));
            System.out.println("✗ " + testName + " ERROR: " + e.getMessage());
        }
    }
    
    private static void printResults() {
        System.out.println("\n=== Test Results Summary ===");
        System.out.println("Tests Passed: " + passedTests + "/" + totalTests);
        
        double percentage = totalTests > 0 ? (passedTests * 100.0) / totalTests : 0;
        System.out.printf("Grade: %.1f%%\n", percentage);
        
        if (percentage >= 90) {
            System.out.println("Letter Grade: A");
        } else if (percentage >= 80) {
            System.out.println("Letter Grade: B");
        } else if (percentage >= 70) {
            System.out.println("Letter Grade: C");
        } else if (percentage >= 60) {
            System.out.println("Letter Grade: D");
        } else {
            System.out.println("Letter Grade: F");
        }
    }
    
    // Helper methods for assertions
    public static void assertEquals(Object expected, Object actual) throws AssertionError {
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected: " + expected + ", but got: " + actual);
        }
    }
    
    public static void assertTrue(boolean condition) throws AssertionError {
        if (!condition) {
            throw new AssertionError("Assertion failed: expected true");
        }
    }
    
    public static void assertFalse(boolean condition) throws AssertionError {
        if (condition) {
            throw new AssertionError("Assertion failed: expected false");
        }
    }
    
    public static void fail(String message) throws AssertionError {
        throw new AssertionError(message);
    }
              }
