package nl.hva.miw.thepiratebank.service.dtomapper;

import nl.hva.miw.thepiratebank.domain.customerattributes.Address;
import nl.hva.miw.thepiratebank.domain.Customer;
import nl.hva.miw.thepiratebank.domain.customerattributes.IdentifyingInformation;
import nl.hva.miw.thepiratebank.domain.customerattributes.PersonalDetails;
import nl.hva.miw.thepiratebank.domain.transfer.CustomerDTO;
import java.time.LocalDate;

public class CustomerDTOtoCustomerMapper {
    private static PersonalDetails personalDetails;
    private static Address address;
    private static IdentifyingInformation identifyingInformation;


    public static Customer mapDTOToCustomer (CustomerDTO customerDTO) {
        personalDetails = new PersonalDetails(customerDTO.getVoornaam(), customerDTO.getTussenvoegsel(),
                customerDTO.getAchternaam());
        address = new Address(customerDTO.getStraat(), customerDTO.getHuisnummer(),
                customerDTO.getHuisnummertoevoeging(), customerDTO.getPostcode(), customerDTO.getWoonplaats());
        identifyingInformation = new IdentifyingInformation (Integer.parseInt(customerDTO.getBsnnummer()),
                customerDTO.getIbannummer(), LocalDate.parse(customerDTO.getGeboortedatum()));
        return new Customer(customerDTO.getEmailadres(), customerDTO.getWachtwoord(), personalDetails, address,
                identifyingInformation);
    }
}
