import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * BoundedStack - ADT แทนรายการลำดับรายชื่อหนังที่ผู้ใช้เลือกไว้
 * 
 * ค่านามธรรม (A) : ลำดับรายชื่อภาพยนตร์ เช่น (ภาพยนตร์A , ภาพยนตร์B , ภาพยนตร์C , .....)
 * 
 * ตัวอย่างการใช้งาน:
 *     Movies m = new Movies();
 *     m.add("IronMan");
 *     m.add("Titanic");
 *     System.out.println(m.size());   // 2
 */
public class BoundedStack {
    public static final int MAX_MOVIES = 10;

        // ===== representation =====
    private final List<String> movies;

// AF(movies) เพลย์ลิสต์ที่แสดงรายชื่อหนัง
    // สแตกของรายชื่อภาพยนตร์
    // โดยภาพยนตร์ตัวสุดท้ายใน movies
    // คือภาพยนตร์บน Top ของ Stack
    // และเป็นเรื่องที่สามารถนำออกได้ก่อน

    // RI 
    // 1.ต้อมีรายการภาพยนตร์อยู่จริง
    // 2.ชื่อภาพยนตร์ต้องไม่ซ้ำกัน
    // 3.ทุกภาพยนตร์จะต้องมีชื่อ
    // 4.ต้องมีภาพยนตร์ น้อยกว่าเท่ากับ 10 และไม่น้อยกว่าเท่ากับ 1
    //
     // ===== Safety from Rep Exposure =====
    // 1. movies เป็น private และ final
    // 2. getMovies() คืน defensive copy
    // 3. ไม่คืน movies โดยตรง
    /**
     * เขียน Representation Invariant
     */
      private void checkRep() {

        assert movies != null : "รายการภาพยนตร์ต้องไม่เป็น null";
        assert movies.size() <= MAX_MOVIES : "จำนวนภาพยนตร์ต้องไม่เกิน 10 เรื่อง";

        Set<String> seen = new HashSet<>();

        for (String movie : movies) {

            assert movie != null : "ชื่อภาพยนตร์ต้องไม่เป็น null";
            assert !movie.isEmpty() : "ชื่อภาพยนตร์ต้องไม่เป็นค่าว่าง";
            assert seen.add(movie) : "ห้ามมีชื่อภาพยนตร์ซ้ำกัน : " + movie;
        }
    }

    // ===== Creator =====

    /**
     * สร้าง Movie Stack ว่าง
     */
    public BoundedStack() {

        this.movies = new ArrayList<>();

        checkRep();
    }

}