package staff.test;

import staff.StaffMember;

import java.time.LocalDate;

import staff.Lecturer;
public class staffTest {
    
    public staffTest() {
    }

    public void printStaffDetails(StaffMember s) {
        System.out.println(s.toString());
    }

    public static void main(String[] args) {
        StaffMember A = new StaffMember("Bill", 3999, LocalDate.of(2020,10,10), LocalDate.of(2024,10,10));
        Lecturer B = new Lecturer("Terry", 5000, LocalDate.of(2011,5,20), LocalDate.of(2020,5,20), "School of Math", "Senior Lecturer");
        staffTest test = new staffTest();
        test.printStaffDetails(A);
        test.printStaffDetails(B);
        System.out.println(A.equals(B));
        StaffMember C = new StaffMember("Bill", 3999, LocalDate.of(2020,10,10), LocalDate.of(2024,10,10));
        System.out.println(A.equals(C));
        Lecturer D = new Lecturer("Terry", 5000, LocalDate.of(2011,5,20), LocalDate.of(2020,5,20), "School of Math", "Senior Lecturer");
        System.out.println(B.equals(D));
    }
}
