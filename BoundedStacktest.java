import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoundedStacktest {

    private static int passed = 0;
    private static int failed = 0;

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("[PASS] " + name);
        } else {
            failed++;
            System.out.println("[FAIL] " + name);
        }
    }

    public static void main(String[] args) {

        boolean assertsOn = false;
        assert assertsOn = true;
        if (!assertsOn) {
            System.out.println("WARNING: assertions disabled - re-run with: java -ea BoundedStacktest\n");
        }

        System.out.println("=== BoundedStack Test Suite ===");

        try {
            testCreators();
            testPush();
            testPop();
            testPeek();
            testIsEmpty();
            testProducer();
            testExposure();
            testBoundaryAndExceptions();
        } catch (Exception e) {
            System.out.println("\n[ERROR] Test suite crashed unexpectedly: " + e.getMessage());
            e.printStackTrace();
        }

        // ส่วนพิมพ์สรุปผล จะทำงานเสมอ
        System.out.println("\n=== Summary ===");
        System.out.println("Passed = " + passed);
        System.out.println("Failed = " + failed);
        System.out.println("Total  = " + (passed + failed));
        System.out.println((failed == 0 && passed > 0) ? "ALL TESTS PASSED" : "SOME TESTS FAILED");

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void testCreators() {
        System.out.println("\n-- Creators --");

        BoundedStack stack = new BoundedStack();
        check("new stack is empty", stack.isEmpty());
        check("size is 0", stack.size() == 0);

        BoundedStack stack2 = new BoundedStack(
                Arrays.asList("A", "B"),
                Arrays.asList(2000, 2001));

        check("constructor size", stack2.size() == 2);
        check("constructor top movie", stack2.peekMovie() != null && stack2.peekMovie().equals("B"));
    }

    private static void testPush() {
        System.out.println("\n-- Push --");

        BoundedStack stack = new BoundedStack();

        check("push returns true", stack.push("IronMan", 2551));
        check("size becomes 1", stack.size() == 1);
        check("top is IronMan", stack.peekMovie() != null && stack.peekMovie().equals("IronMan"));

        boolean duplicate = false;
        try {
            stack.push("IronMan", 2551);
        } catch (IllegalArgumentException e) {
            duplicate = true;
        }

        check("duplicate movie throws exception", duplicate);
    }

    private static void testPop() {
        System.out.println("\n-- Pop --");

        BoundedStack stack = new BoundedStack();

        stack.push("A", 2000);
        stack.push("B", 2001);

        check("pop returns true", stack.pop());
        check("top becomes A", stack.peekMovie() != null && stack.peekMovie().equals("A"));
        check("size becomes 1", stack.size() == 1);
    }

    private static void testPeek() {
        System.out.println("\n-- Peek --");

        BoundedStack stack = new BoundedStack();

        stack.push("Titanic", 2540);

        check("peekMovie()", stack.peekMovie() != null && stack.peekMovie().equals("Titanic"));
        check("peekYear()", stack.peekYear() != null && stack.peekYear() == 2540);
        check("peek does not remove data", stack.size() == 1);
    }

    private static void testIsEmpty() {
        System.out.println("\n-- isEmpty --");

        BoundedStack stack = new BoundedStack();

        check("empty stack", stack.isEmpty());

        stack.push("Avatar", 2552);

        check("non-empty stack", !stack.isEmpty());

        stack.pop();

        check("empty after pop", stack.isEmpty());
    }

    private static void testProducer() {
        System.out.println("\n-- Producer --");

        BoundedStack stack = new BoundedStack(
                Arrays.asList("A", "B", "C"),
                Arrays.asList(2000, 2001, 2002));

        BoundedStack reversed = stack.reversed();

        check("same size", reversed.size() == stack.size());
        check("reverse top", reversed.peekMovie() != null && reversed.peekMovie().equals("A"));
        check("original unchanged", stack.peekMovie() != null && stack.peekMovie().equals("C"));
    }

    private static void testExposure() {
        System.out.println("\n-- Representation Exposure --");

        List<String> movies = new ArrayList<>(Arrays.asList("A", "B"));
        List<Integer> years = new ArrayList<>(Arrays.asList(2000, 2001));

        BoundedStack stack = new BoundedStack(movies, years);

        movies.clear();
        years.clear();
        check("constructor copies input list", stack.size() == 2);

        List<String> internalMovies = stack.movies();
        List<Integer> internalYears = stack.years();
        internalMovies.clear();
        internalYears.clear();
        check("observers return defensive copy", stack.size() == 2);
    }

    private static void testBoundaryAndExceptions() {
        System.out.println("\n-- Boundary & Exceptions --");

        BoundedStack emptyStack = new BoundedStack();
        check("pop on empty stack returns false", !emptyStack.pop());

        boolean threwNull = false;
        try {
            emptyStack.push(null, 2020);
        } catch (IllegalArgumentException e) {
            threwNull = true;
        }
        check("push null movie throws exception", threwNull);

        boolean threwEmptyName = false;
        try {
            emptyStack.push("", 2020);
        } catch (IllegalArgumentException e) {
            threwEmptyName = true;
        }
        check("push empty movie throws exception", threwEmptyName);

        boolean threwInvalidYear = false;
        try {
            emptyStack.push("Movie", -5);
        } catch (IllegalArgumentException e) {
            threwInvalidYear = true;
        }
        check("push negative year throws exception", threwInvalidYear);

        BoundedStack fullStack = new BoundedStack();
        for (int i = 0; i < BoundedStack.MAX_MOVIES; i++) {
            fullStack.push("Movie_" + i, 2000 + i);
        }
        check("can push up to MAX_MOVIES", fullStack.size() == BoundedStack.MAX_MOVIES);

        boolean threwFull = false;
        try {
            fullStack.push("ExtraMovie", 3000);
        } catch (IllegalArgumentException e) {
            threwFull = true;
        }
        check("push when full throws exception", threwFull);
    }
}