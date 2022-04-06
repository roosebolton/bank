package nl.hva.miw.thepiratebank.service.dtomapper;

import nl.hva.miw.thepiratebank.domain.Asset;
import nl.hva.miw.thepiratebank.domain.Customer;
import nl.hva.miw.thepiratebank.domain.transfer.OrderDTO;
import nl.hva.miw.thepiratebank.domain.Order;
import nl.hva.miw.thepiratebank.utilities.exceptions.InvalidJsonInputException;

public class OrderDTOtoOrderMapper {

    public static Order mapOrder (OrderDTO orderDTO) {
        if (!orderDTO.isNonNull()) {
            throw  new InvalidJsonInputException();
        }

         return new Order(orderDTO.isBuy(),
                 new Customer(orderDTO.getUserId()),
                 new Asset(orderDTO.getAsset()),
                 orderDTO.getAmount(),
                 orderDTO.getPrice());
    }
}
