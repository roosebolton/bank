package nl.hva.miw.thepiratebank.domain.customerattributes;

import nl.hva.miw.thepiratebank.domain.Customer;

import java.util.Objects;

public class Address extends Customer {
private String street;
private String houseNumber;
private String houseNumberAddition;
private String postalCode;
private String city;

    public Address(String straat, String huisnummer, String toevoeging, String postcode, String woonplaats) {
        this.street = straat;
        this.houseNumber = huisnummer;
        this.houseNumberAddition = toevoeging;
        this.postalCode = postcode;
        this.city = woonplaats;
    }

    public Address() {
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getHouseNumberAddition() {
        return houseNumberAddition;
    }

    public void setHouseNumberAddition(String houseNumberAddition) {
        this.houseNumberAddition = houseNumberAddition;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address address = (Address) o;
        return street.equals(address.street) && houseNumber.equals(address.houseNumber) && houseNumberAddition.equals(address.houseNumberAddition) && postalCode.equals(address.postalCode) && city.equals(address.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, houseNumber, houseNumberAddition, postalCode, city);
    }

    @Override
    public String toString() {
        return "Address{" +
                "straat='" + street + '\'' +
                ", huisnummer='" + houseNumber + '\'' +
                ", toevoeging='" + houseNumberAddition + '\'' +
                ", postcode='" + postalCode + '\'' +
                ", woonplaats='" + city + '\'' +
                '}';
    }
}
