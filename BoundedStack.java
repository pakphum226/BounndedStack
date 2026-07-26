import java.util.*;


/**
 * BoundedStack - ADT แทนรายการลำดับรายชื่อหนังและปีที่ฉายที่ผู้ใช้เลือกไว้
 * 
 * ค่านามธรรม (A) : ลำดับรายชื่อภาพยนตร์และปีที่ฉาย เช่น (ภาพยนตร์A , ภาพยนตร์B , ภาพยนตร์C , .....)
 * 
 * ตัวอย่างการใช้งาน:
 *     Movies m = new Movies();
 *     m.add("IronMan", 2551);
 *     m.add("Titanic", 2540);
 *     System.out.println(m.size());   // 2
 */




public class BoundedStack {
    
    private final List<String> movies ;
    private final int MAX_movies = 10 ;
    
    // AF(movies) เพลย์ลิสต์ที่แสดงรายชื่อหนัง
    // RI 
    // 1.ต้อมีรายการภาพยนตร์อยู่จริง
    // 2.ชื่อภาพยนตร์ต้องไม่ซ้ำกัน
    // 3.ทุกภาพยนตร์จะต้องมีชื่อ
    // 4.ต้องมีภาพยนตร์ น้อยกว่าเท่ากับ 10 และไม่น้อยกว่าเท่ากับ 1
    // 5.ทุกภาพยนตร์จะต้องมีปีที่ฉาย
    //
    // ให้ movies เป็น final และต้องก็อปปี้ก่อนที่จะส่งกลับไป
    
    /**
     * 
     * 
     *
     * @param s
     */
    public BoundedStack(){
        this.movies = new ArrayList<>() ;

    }

    
}
