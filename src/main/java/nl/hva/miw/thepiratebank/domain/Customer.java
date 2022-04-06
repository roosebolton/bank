package nl.hva.miw.thepiratebank.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import nl.hva.miw.thepiratebank.domain.customerattributes.Address;
import nl.hva.miw.thepiratebank.domain.customerattributes.IdentifyingInformation;
import nl.hva.miw.thepiratebank.domain.customerattributes.PersonalDetails;
import java.util.Objects;

public class Customer extends User {
    private PersonalDetails personalDetails;
    private Address address;
    private IdentifyingInformation identifyingInformation;
    @JsonManagedReference
    private Wallet wallet;
    @JsonManagedReference
    private Account account;


    public Customer(String userName, String password, PersonalDetails personalDetails, Address address, IdentifyingInformation identifyingInformation) {
        super(userName, password);
        this.personalDetails = personalDetails;
        this.address = address;
        this.identifyingInformation = identifyingInformation;
    }

    public Customer(PersonalDetails personalDetails, Address address, IdentifyingInformation identifyingInformation) {
        this.personalDetails = personalDetails;
        this.address = address;
        this.identifyingInformation = identifyingInformation;
    }

    public Customer(int userId, String userName, String password, PersonalDetails personalDetails, Address address, IdentifyingInformation identifyingInformation) {
        super(userId,userName, password);
        this.personalDetails = personalDetails;
        this.address = address;
        this.identifyingInformation = identifyingInformation;
    }

    public Customer(int userId) {
        super(userId);
    }

    public Customer() {

    }


    public PersonalDetails getPersonalDetails() {
        return personalDetails;
    }

    public void setPersonalDetails(PersonalDetails personalDetails) {
        this.personalDetails = personalDetails;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public IdentifyingInformation getIdentifyingInformation() {
        return identifyingInformation;
    }

    public void setIdentifyingInformation(IdentifyingInformation identifyingInformation) {
        this.identifyingInformation = identifyingInformation;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Account getAccount() { return account; }

    public void setAccount(Account account) { this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return identifyingInformation.equals(customer.identifyingInformation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifyingInformation);
    }

    @Override
    public String toString() {
        return "Customer{" +
                super.toString()+
                "personalDetails=" + personalDetails +
                ", address=" + address +
                ", identifyingInformation=" + identifyingInformation +
                ", wallet=" + wallet +
                ", account=" + account +
                '}';
    }
}
