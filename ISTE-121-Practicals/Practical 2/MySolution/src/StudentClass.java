import java.io.Serializable;
import java.util.ArrayList;
/**
 * Constructor
 * @author Klara Makek
 * Course/Section: ISTE-121 800
 * Practical #2
 * Date: 31.3.2023. 
 */
public class StudentClass {
    private String name;
    private double gpa;
    private Long recordTimeStamp;
    ArrayList<String> list = new ArrayList<>();

    public StudentClass(String name, double gpa, Long recordTimeStamp, ArrayList<String> list) {
        this.name = name;
        this.gpa = gpa;
        this.recordTimeStamp = recordTimeStamp;
        this.list = list;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getGpa() {
        return gpa;
    }
    public void setGpa(double gpa) {
        this.gpa = gpa;
    }
    public Long getRecordTimeStamp() {
        return recordTimeStamp;
    }
    public void setRecordTimeStamp(Long recordTimeStamp) {
        this.recordTimeStamp = recordTimeStamp;
    }
    public ArrayList<String> getList() {
        return list;
    }
    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public String toString(){
        System.out.println("Name:" + getName() + "GPA:" +getGpa() + "TIME[s]:" + getRecordTimeStamp() + "AWARDS:2" + list);
        return null;
    }
}
