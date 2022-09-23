package staff;

import java.time.LocalDate;

/**
 * A staff member
 * @author Robert Clifton-Everest
 *
 */
public class StaffMember {
    private String name;
    private int salary;
    private LocalDate hireDate;
    private LocalDate endDate;
    /**
     * Constractor of staff member
     * @param name
     * @param salary
     * @param hireDate
     * @param endDate
     */
    public StaffMember(String name, int salary, LocalDate hireDate, LocalDate endDate) {
        this.name = name;
        this.salary = salary;
        this.hireDate = hireDate;
        this.endDate = endDate;
    }
    /**
     * get the name of staff
     * @return name of staff
     */
    public String getName() {
        return name;
    }

    /**
     * set the name of staff
     * @param name of staff
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get the salary of staff
     * @return salary of staff
     */
    public int getSalary() {
        return salary;
    }

    /**
     * set the salary of staff
     * @param salary of staff
     */
    public void setSalary(int salary) {
        this.salary = salary;
    }

    /**
     * get the hire date of staff
     * @return hire date of staff
     */
    public LocalDate getHireDate() {
        return hireDate;
    }

    /**
     * set the hire date of staff
     * @param hireDate of staff
     */
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    /**
     * get the end date of staff
     * @return end date of staff
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * set end date of staff
     * @param endDate of staff
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


    @Override
    public String toString() {
        return "StaffMember [endDate=" + endDate + ", hireDate=" + hireDate + ", name=" + name + ", salary=" + salary
                + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StaffMember other = (StaffMember) obj;
        if (endDate == null) {
            if (other.endDate != null)
                return false;
        } else if (!endDate.equals(other.endDate))
            return false;
        if (hireDate == null) {
            if (other.hireDate != null)
                return false;
        } else if (!hireDate.equals(other.hireDate))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (salary != other.salary)
            return false;
        return true;
    }

    
}

