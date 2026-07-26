import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * BoundedStack - ADT แทนรายการลำดับรายชื่อหนังและปีที่ฉายที่ผู้ใช้เลือกไว้
 *
 * ค่านามธรรม (A):
 * ลำดับของภาพยนตร์และปีที่ฉาย เช่น
 * (IronMan, 2551), (Titanic, 2540)
 *
 * ตัวอย่างการใช้งาน:
 *     BoundedStack m = new BoundedStack();
 *     m.addMovie("IronMan", 2551);
 *     m.addMovie("Titanic", 2540);
 *     System.out.println(m.size());   // 2
 */
public class BoundedStack {

    public static final int MAX_MOVIES = 10;

    // ===== Representation =====
    private final List<String> movies;
    private final List<Integer> years;

    // ===== Abstraction Function =====
    // AF(movies, years) =
    // แทนสแตกของรายชื่อภาพยนตร์และปีที่ฉาย
    // movies.get(i) คือชื่อภาพยนตร์
    // years.get(i) คือปีที่ฉายของภาพยนตร์เรื่องเดียวกัน
    // สมาชิกตัวสุดท้ายคือ Top ของ Stack

    // ===== Representation Invariant =====
    // 1. movies ต้องไม่เป็น null
    // 2. years ต้องไม่เป็น null
    // 3. movies และ years ต้องมีจำนวนสมาชิกเท่ากัน
    // 4. ไม่มีชื่อภาพยนตร์เป็น null
    // 5. ไม่มีชื่อภาพยนตร์เป็นสตริงว่าง
    // 6. ไม่มีชื่อภาพยนตร์ซ้ำกัน
    // 7. จำนวนภาพยนตร์ต้องไม่เกิน 10 เรื่อง

    // ===== Safety from Rep Exposure =====
    // 1. movies และ years เป็น private และ final
    // 2. คืนข้อมูลแบบ Defensive Copy
    // 3. ไม่คืน reference ของ representation โดยตรง

    private void checkRep() {

        assert movies != null : "movies ต้องไม่เป็น null";
        assert years != null : "years ต้องไม่เป็น null";
        assert movies.size() == years.size()
                : "movies และ years ต้องมีจำนวนเท่ากัน";
        assert movies.size() <= MAX_MOVIES
                : "จำนวนหนังต้องไม่เกิน 10 เรื่อง";

        Set<String> seen = new HashSet<>();

        for (int i = 0; i < movies.size(); i++) {

            String movie = movies.get(i);

            assert movie != null : "ชื่อหนังต้องไม่เป็น null";
            assert !movie.isEmpty() : "ชื่อหนังต้องไม่เป็นค่าว่าง";
            assert seen.add(movie) : "ชื่อหนังซ้ำ : " + movie;

            assert years.get(i) > 0 : "ปีที่ฉายไม่ถูกต้อง";
        }
    }

    // ===== Creator =====

    /**
     * สร้างสแตกของหนังว่าง
     */
    public BoundedStack() {

        movies = new ArrayList<>();
        years = new ArrayList<>();

        checkRep();
    }
}