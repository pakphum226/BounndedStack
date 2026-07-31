import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoundedStacktest {

    private static int passed = 0;
    private static int failed = 0;

    /** helper กลาง — พิมพ์ PASS/FAIL และนับผลให้เอง */
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
            System.out.println("WARNING: assertions disabled - re-run with: java -ea BoundedStackTest\n");
        }

        System.out.println("=== BoundedStack Test Suite ===\n");

        testCreators();
        testAdd();
        testRemove();
        testObservers();
        testProducer();
        testExposure();

        System.out.println("\n=== Summary ===");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total : " + (passed + failed));
        System.out.println(failed == 0 ? "ALL TESTS PASSED" : "SOME TESTS FAILED");

        if (failed > 0) {
            System.exit(1);
        }
    }

    // --- Partition: ว่าง / มีภาพยนตร์ / input ที่ผิดเงื่อนไข ---
    private static void testCreators() {
        System.out.println("-- Creators --");

        // boundary: สแตคว่างคือขอบล่างที่ถูกต้อง
        BoundedStack empty = new BoundedStack();
        check("new() -> empty", empty.size() == 0);
        check("new() -> contains nothing", !empty.contains("IronMan"));

        List<String> movies = Arrays.asList("IronMan", "Titanic");
        List<Integer> years = Arrays.asList(2551, 2540);

        BoundedStack s = new BoundedStack(movies, years);
        check("new(list) -> size 2", s.size() == 2);
        check("new(list) -> contains Titanic", s.contains("Titanic"));
        check("new(list) -> preserves order",
                s.movies().equals(Arrays.asList("IronMan", "Titanic")));
        check("new(list) -> preserves years",
                s.years().equals(Arrays.asList(2551, 2540)));

        // boundary: list ว่างคือขอบล่างที่ถูกต้อง
        BoundedStack fromEmpty = new BoundedStack(new ArrayList<>(), new ArrayList<>());
        check("new(empty list) -> empty", fromEmpty.size() == 0);

        // input ที่ผิดเงื่อนไขต้องโยน exception  
        boolean threwDup = false;
        try {
            new BoundedStack(Arrays.asList("A", "A"), Arrays.asList(2000, 2001));
        } catch (IllegalArgumentException e) {
            threwDup = true;
        }
        check("new(duplicates) -> throws IllegalArgumentException", threwDup);

        boolean threwNull = false;
        try {
            new BoundedStack(null, null);
        } catch (IllegalArgumentException e) {
            threwNull = true;
        }
        check("new(null) -> throws IllegalArgumentException", threwNull);

        boolean threwDifferentSize = false;
        try {
            new BoundedStack(Arrays.asList("A"), Arrays.asList(2000, 2001));
        } catch (IllegalArgumentException e) {
            threwDifferentSize = true;
        }
        check("new(different size) -> throws IllegalArgumentException", threwDifferentSize);
    }

    // --- Mutator: add ต้องรักษาลำดับและกันข้อมูลซ้ำ ---
    private static void testAdd() {
        System.out.println("\n-- Add --");

        BoundedStack s = new BoundedStack();

        check("add(IronMan) -> returns true", s.add("IronMan", 2551));
        check("add(IronMan) -> size 1", s.size() == 1);
        check("add(IronMan) -> found by contains", s.contains("IronMan"));

        s.add("Titanic", 2540);
        s.add("Avatar", 2552);

        check("add preserves insertion order",
                s.movies().equals(Arrays.asList("IronMan", "Titanic", "Avatar")));

        // ข้อมูลซ้ำไม่ใช่ error — คืน false เฉย ๆ
        check("add duplicate -> returns false", !s.add("IronMan", 2551));
        check("failed add leaves size unchanged", s.size() == 3);

        // input ที่ผิดเงื่อนไขต้องโยน exception
        boolean threwNull = false;
        try {
            s.add(null, 2550);
        } catch (IllegalArgumentException e) {
            threwNull = true;
        }
        check("add(null) -> throws IllegalArgumentException", threwNull);

        boolean threwYear = false;
        try {
            s.add("Movie", 0);
        } catch (IllegalArgumentException e) {
            threwYear = true;
        }
        check("add(invalid year) -> throws IllegalArgumentException", threwYear);

        check("failed adds leave stack unchanged", s.size() == 3);

        // boundary: เติมจนเต็มพอดีแล้วเติมเพิ่ม
        BoundedStack full = new BoundedStack();
        for (int i = 0; i < BoundedStack.MAX_MOVIES; i++) {
            full.add("Movie" + i, 2000 + i);
        }

        check("can fill up to MAX_MOVIES", full.size() == BoundedStack.MAX_MOVIES);
        check("add when full -> returns false", !full.add("Extra", 3000));
        check("full stack stays at MAX_MOVIES", full.size() == BoundedStack.MAX_MOVIES);
    }

    // --- Mutator: remove ทั้งกรณีพบและไม่พบ ---
    private static void testRemove() {
        System.out.println("\n-- Remove --");

        BoundedStack s = new BoundedStack();
        s.add("IronMan", 2551);
        s.add("Titanic", 2540);
        s.add("Avatar", 2552);

        check("remove(Titanic) -> returns true", s.remove("Titanic", 2540));
        check("remove -> size decreases", s.size() == 2);
        check("remove -> movie is gone", !s.contains("Titanic"));
        check("remove keeps the others in order",
                s.movies().equals(Arrays.asList("IronMan", "Avatar")));

        // ลบภาพยนตร์ที่ไม่มีไม่ใช่ error — คืน false เฉย ๆ
        check("remove missing movie -> returns false", !s.remove("Batman", 2548));
        check("failed remove leaves size unchanged", s.size() == 2);

        // boundary: ลบจนหมด
        s.remove("IronMan", 2551);
        s.remove("Avatar", 2552);
        check("remove all -> empty", s.size() == 0);
        check("remove on empty stack -> returns false", !s.remove("IronMan", 2551));
    }

    // --- Observer ต้องไม่มี side effect ---
    private static void testObservers() {
        System.out.println("\n-- Observers --");

        BoundedStack s = new BoundedStack();
        s.add("IronMan", 2551);
        s.add("Titanic", 2540);

        check("size reports 2", s.size() == 2);
        check("contains finds an existing movie", s.contains("IronMan"));
        check("contains rejects a missing movie", !s.contains("Avatar"));
        check("movies returns the full list in order",
                s.movies().equals(Arrays.asList("IronMan", "Titanic")));
        check("years returns the full list in order",
                s.years().equals(Arrays.asList(2551, 2540)));

        int before = s.size();
        s.size();
        s.contains("IronMan");
        s.movies();
        s.years();

        check("observers have no side effects", s.size() == before);
    }

    // --- Producer ต้องคืนตัวใหม่ ไม่แก้ตัวเดิม ---
    private static void testProducer() {
        System.out.println("\n-- Producer (reversed) --");

        BoundedStack original = new BoundedStack();
        original.add("A", 2000);
        original.add("B", 2001);
        original.add("C", 2002);

        BoundedStack reverse = original.reversed();

        check("reversed has the same size", reverse.size() == original.size());
        check("reversed preserves reverse order",
                reverse.movies().equals(Arrays.asList("C", "B", "A")));
        check("reversed does not mutate the original",
                original.movies().equals(Arrays.asList("A", "B", "C")));

        reverse.add("D", 2003);

        check("mutating the result does not affect the original", original.size() == 3);

        // boundary: reverse สแตคว่างต้องไม่พัง
        BoundedStack emptyReverse = new BoundedStack().reversed();
        check("reversing an empty stack is safe", emptyReverse.size() == 0);
    }

    // --- ทดสอบว่าไม่เกิด representation exposure ---
    private static void testExposure() {
        System.out.println("\n-- Representation Exposure --");

        // ขาออก: แก้ list ที่ได้จาก movies() ต้องไม่กระทบ rep
        BoundedStack s = new BoundedStack();
        s.add("IronMan", 2551);

        List<String> movies = s.movies();
        movies.clear();
        check("clearing result of movies() does not affect stack", s.size() == 1);

        movies = s.movies();
        movies.add("Injected");
        check("adding to result of movies() does not affect stack",
                s.size() == 1 && !s.contains("Injected"));

        List<Integer> years = s.years();
        years.clear();
        check("clearing result of years() does not affect stack", s.size() == 1);

        // สองครั้งต้องเป็นคนละ object
        check("movies() returns a fresh list each call", s.movies() != s.movies());
        check("years() returns a fresh list each call", s.years() != s.years());

        // ขาเข้า: แก้ list ที่ส่งให้ constructor ต้องไม่กระทบ rep
        List<String> inputMovies = new ArrayList<>(Arrays.asList("A", "B"));
        List<Integer> inputYears = new ArrayList<>(Arrays.asList(2000, 2001));

        BoundedStack copy = new BoundedStack(inputMovies, inputYears);

        inputMovies.clear();
        inputYears.clear();

        check("clearing constructor argument does not affect stack", copy.size() == 2);

        inputMovies.add("Injected");

        check("adding to constructor argument does not affect stack", !copy.contains("Injected"));
    }
}