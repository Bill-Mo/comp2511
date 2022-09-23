package staff;

import java.time.LocalDate;

public class Lecturer extends StaffMember {
    private String school;
    private String academicStatus;
    
    public Lecturer(String name, int salary, LocalDate hireDate, LocalDate endDate, String school,
            String academicStatus) {
        super(name, salary, hireDate, endDate);
        this.school = school;
        this.academicStatus = academicStatus;
    }

    /**
     * get the school of lecturer
     * @return the school of lecturer
     */
    public String getSchool() {
        return school;
    }

    /**
     * set the school of lecturer
     * @param school of lecturer
     */
    public void setSchool(String school) {
        this.school = school;
    }

    /**
     * get the academic status of lecturer
     * @return academic status of lecturer
     */
    public String getAcademicStatus() {
        return academicStatus;
    }
    
    /**
     * set the academic status of lecturer
     * @param academicStatus of lecturer
     */
    public void setAcademicStatus(String academicStatus) {
        this.academicStatus = academicStatus;
    }

    @Override
    public String toString() {
        return "Lecturer [academicStatus=" + academicStatus + ", school=" + school + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Lecturer other = (Lecturer) obj;
        if (academicStatus == null) {
            if (other.academicStatus != null)
                return false;
        } else if (!academicStatus.equals(other.academicStatus))
            return false;
        if (school == null) {
            if (other.school != null)
                return false;
        } else if (!school.equals(other.school))
            return false;
        return true;
    }
}
