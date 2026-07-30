import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * BoundedStack - ADT แทนรายการลำดับรายชื่อภาพยนตร์และปีที่ฉายที่ผู้ใช้เลือกไว้
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
    // แทนรายชื่อภาพยนตร์และปีที่ฉาย
    // movies.get(i) คือชื่อภาพยนตร์
    // years.get(i) คือปีที่ฉายของภาพยนตร์เรื่องเดียวกัน
    // สมาชิกตัวสุดท้ายคือ Top ของรายชื่อภาพยนตร์และปีที่ฉาย

    // ===== Representation Invariant =====
    // ภาพยนตร์ต้องมีอยู่จริง
    // ปีที่ฉายต้องมีอยู่จริง
    // ภาพยนตร์และปีที่ฉายต้องมีจำนวนเท่ากัน
    // ทุกภาพยนตร์ต้องมีอยู่จริง
    // ภาพยนตร์ต้องมีชื่อภาพยนตร์
    // ปีที่ฉายต้องมากกว่า 0
    // ไม่มีชื่อภาพยนตร์ซ้ำกัน
    // มีภาพยนตร์ต้องไม่เกิน 10 เรื่อง

    // ===== Safety from Rep Exposure =====
    // ให้ movies และ years เป็น final object
    // ก็อปปี้ข้อมูลก่ออนส่งออกไป

    private void checkRep() {

        assert movies != null : "ภาพยนตร์ต้องมีอยู่จริง";
        assert years != null : "ปีที่ฉายต้องมีอยู่จริง";
        assert movies.size() == years.size() : "ภาพยนตร์และปีที่ฉายต้องมีจำนวนเท่ากัน";
        assert movies.size() <= MAX_MOVIES: "จำนวนภาพยนตร์ต้องไม่เกิน 10 เรื่อง";

        Set<String> seen = new HashSet<>();

       for (int i = 0; i < movies.size(); i++) {
        String M = movies.get(i);
        Integer Y = years.get(i);

        assert M != null : "ชื่อภาพยนตร์ต้องมีอยู่จริง";
        assert !M.isEmpty() : "ภาพยนตร์ต้องมีชื่อภาพยนตร์";
        assert seen.add(M) : "ชื่อภาพยนตร์ซ้ำ : " + M;

        assert Y != null : "ปีที่ฉายต้องมีอยู่จริง";
        assert Y > 0 : "ปีที่ฉายต้องมากกว่า 0 : " + Y;
    }
    }

    // Creator 

    /**
     * สร้างสแตกของภาพยนตร์ว่าง
     */
    public BoundedStack() {

        this.movies = new ArrayList<>();
        this.years = new ArrayList<>();
        checkRep();
    }
    // Creater 2
    /**
    * สร้างสแตคของรายชื่อภาพยนตร์ที่ให้มา
    *
    * @param initial รายชื่อภาพยนตร์และปีที่ฉายเริ่มต้น ต้องไม่ซ้ำและไม่เกิน 10 เรื่อง
    * @throws IllegalArgumentException ถ้า initial ผิดเงื่อนไข
    */
    public BoundedStack(List<String> initialmovies , List<Integer> initialyears ){
        if (initialmovies == null || initialyears == null) throw new IllegalArgumentException("ภาพยนตร์ต้องมีอยู่จริง");
        if (initialmovies.size() != initialyears.size()) throw new IllegalArgumentException("จำนวนภาพยนตร์และปีที่ฉายต้องเท่ากัน");
        if (initialmovies.size()> MAX_MOVIES) throw new IllegalArgumentException("จำนวนภาพยนตร์ต้องไม่เกิน 10 เรื่อง");
        Set<String> seen = new HashSet<>();
        for (int i = 0; i < initialmovies.size(); i++) {
            String movie = initialmovies.get(i);
            Integer year = initialyears.get(i);

            if (movie == null || movie.isEmpty()) {
                throw new IllegalArgumentException("ชื่อภาพยนตร์ต้องมีอยยู่จริงหรือภาพยนตร์ต้องมีชื่อ");
            }
            if (!seen.add(movie)) {
                throw new IllegalArgumentException("ชื่อภาพยนตร์ต้องห้ามซ้ำกัน: " + movie);
            }

            if (year == null || year <= 0) {
                throw new IllegalArgumentException("ปีที่ฉายต้องมีอยู่จริง");
            }
        }
        this.movies = new ArrayList<>(initialmovies);
        this.years = new ArrayList<>(initialyears) ;  
        checkRep();
    }
    // Mutator
    //
    //เพิ่มรายการภาพยนตร์และปีต่อท้าย

    /**
    *
    * @param movie,year ชื่อภาพยนตร์และปีที่ฉาย ต้องไม่เป็น null และไม่เป็นช่องว่าง
    * @return true ถ้าเพิ่มสำเร็จ, false ถ้ามีภาพยนตร์นี้มีอยู่แล้วหรือเต็มแล้ว
    * @throws IllegalArgumentException ถ้า movie,year เป็น null หรือช่องว่าง,น้อยกว่า  0
    */
    public boolean add(String movie , Integer year ) {
        if(movie == null || movie.isEmpty()) throw new IllegalArgumentException("ภาพยนตร์ต้องมีอยู่จริง");
        if(year == null || year <= 0) throw new IllegalArgumentException("ปีที่ฉายต้องมีอยู่จริงหรือต้องมากกว่า 0");
        if(movies.contains(movie) || movies.size() >= MAX_MOVIES) return false ;
        movies.add(movie);
        years.add(year);
        checkRep();
        return true;   
    }
    /**
    * ลบรายการภาพยนตร์ออก
    *
    * @param movie,year รายการภาพยนตร์ปีที่ฉายที่ต้องการลบ
    * @return true ถ้าลบสำเร็จ , false ถ้าไม่เจอชื่อรายการภาพยนตร์
    */
   public boolean remove(String movie , Integer year ){
        if(!movie.contains(movie)) return false ;
        movies.remove(movie);
        years.remove(year);
        checkRep();
        return true ;
   }
   // Observers
    /**
     * คืนจำนวนรายการภาพยนตร์และปีที่ฉาย
     */
    public int size() {
        return movies.size() ;   
        
    }
    /**
     * ตรวจเช็คว่ามีภาพยนตร์นี้อยู่หรือไม่
     */
    public boolean contains(String movie) {
        return movies.contains(movie);   
    }
    /**
     * คืนรายชื่อภาพยนตร์และปีทั้งหมดตามลำดับทั้งหมดตามลำดับ
     */
    public List<String> movies() {
        return new ArrayList<>(movies) ;
    }
    public List<Integer> years(){
        return new ArrayList<>(years);
    }
    // Producer 
    /**
     * คืนสแตคใหม่ที่มีภาพยนตร์และปีที่ฉายเดียวกันแต่สลับลำดับ
     *
     * @return เพลย์ลิสต์ใหม่ที่สลับลำดับแล้ว
     */
    public BoundedStack reversed() {
        List<String> copymovies = new ArrayList<>(movies);
        List<Integer> copyyears = new ArrayList<>(years);
        Collections.reverse(copymovies);
        Collections.reverse(copyyears);
        return new BoundedStack(copymovies,copyyears);   
    }

   @Override
    public String toString() {
        return "Movies: " + movies.toString() + ", Years: " + years.toString();
    }
   
}