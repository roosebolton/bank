package nl.hva.miw.thepiratebank.domain;

public class Administrator extends User {
    private String firstName;
    private String lastName;
    private int employeeNumber = 0;

    public Administrator(String userName, String password, String firstName, String lastName) {
        super(userName, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeNumber = employeeNumber++;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(int employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    @Override
    public String toString() {
        return "Administrator{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
