package nl.hva.miw.thepiratebank.repository.rootrepositories;

import nl.hva.miw.thepiratebank.domain.Customer;
import nl.hva.miw.thepiratebank.repository.CustomerDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerRootRepository {

    CustomerDAO customerDAO;

    public CustomerRootRepository(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    /**
     * Gets a list with all customers
     * @return A list with all customers
     */
    public List<Customer> getAllCustomers(){
        return customerDAO.getAll();
    }

    /**
     * Creates a customer, given a Customer object
     * @param customer
     */
    public void createCustomer(Customer customer){
        customerDAO.create(customer);
    }

    /**
     * Gets a customer, based on the id.
     * @param id
     * @return Customer by id if exists, null otherwise
     */
    public Customer getCustomer(Integer id){
        return customerDAO.get(id);
    }

    /**
     * Updates an existing client with the information of a given client
     * @param customer
     */
    public void updateCustomer(Customer customer){
        customerDAO.update(customer);
    }

    /**
     * Deletes an existing client, if found by its id
     * @param id
     */
    public void deleteCustomer(Integer id){
        customerDAO.delete(id);
    }


}
