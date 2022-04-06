package nl.hva.miw.thepiratebank.domain.customerattributes;

import nl.hva.miw.thepiratebank.domain.Customer;

import java.time.LocalDate;
import java.util.Objects;

public class IdentifyingInformation extends Customer {
    private int bsnNumber;
    private String ibanNumber;
    private LocalDate dateOfBirth;

    public IdentifyingInformation(int bsnNumber, String ibanNumber, LocalDate dateOfBirth) {
        this.bsnNumber = bsnNumber;
        this.ibanNumber = ibanNumber;
        this.dateOfBirth = dateOfBirth;
    }

    public int getBsnNumber() {
        return bsnNumber;
    }

    public void setBsnNumber(int bsnNumber) {
        this.bsnNumber = bsnNumber;
    }

    public String getIbanNumber() {
        return ibanNumber;
    }

    public void setIbanNumber(String ibanNumber) {
        this.ibanNumber = ibanNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IdentifyingInformation)) return false;
        IdentifyingInformation that = (IdentifyingInformation) o;
        return bsnNumber == that.bsnNumber && ibanNumber.equals(that.ibanNumber) && dateOfBirth.equals(that.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bsnNumber, ibanNumber, dateOfBirth);
    }

    @Override
    public String toString() {
        return "IdentifyingInformation{" +
                "bsnNumber=" + bsnNumber +
                ", ibanNumber='" + ibanNumber + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
