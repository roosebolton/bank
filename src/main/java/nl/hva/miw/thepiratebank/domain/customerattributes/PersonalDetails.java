package nl.hva.miw.thepiratebank.domain.customerattributes;

import nl.hva.miw.thepiratebank.domain.Customer;

import java.util.Objects;

public class PersonalDetails extends Customer {
    private String firstName;
    private String inFix;
    private String lastName;

    public PersonalDetails(String firstname, String tussenvoegsel, String lastname) {
        super();
        this.firstName = firstname;
        this.inFix = tussenvoegsel;
        this.lastName = lastname;
    }

    public PersonalDetails() {
        super();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getInFix() {
        return inFix;
    }

    public void setInFix(String inFix) {
        this.inFix = inFix;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonalDetails)) return false;
        PersonalDetails that = (PersonalDetails) o;
        return firstName.equals(that.firstName) && inFix.equals(that.inFix) && lastName.equals(that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, inFix, lastName);
    }

    @Override
    public String toString() {
        return "PersonalDetails{" +
                "firstname='" + firstName + '\'' +
                ", tussenvoegsel='" + inFix + '\'' +
                ", lastname='" + lastName + '\'' +
                '}';
    }
}
