package nl.hva.miw.thepiratebank.service;

import nl.hva.miw.thepiratebank.domain.Customer;
import nl.hva.miw.thepiratebank.domain.Order;
import nl.hva.miw.thepiratebank.domain.Valuta;
import nl.hva.miw.thepiratebank.domain.transfer.OrderDTO;
import nl.hva.miw.thepiratebank.repository.RootRepository;
import nl.hva.miw.thepiratebank.repository.TradeRepository;
import nl.hva.miw.thepiratebank.service.dtomapper.OrderDTOtoOrderMapper;
import nl.hva.miw.thepiratebank.service.market.*;
import nl.hva.miw.thepiratebank.utilities.authorization.token.AccessTokenService;
import nl.hva.miw.thepiratebank.utilities.exceptions.ConflictException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final RootRepository rootRepository;
    private final TradeRepository tradeRepository;
    private final MarketService marketService;
    private final WalletService walletService;
    private final AccessTokenService accessTokenService;
    private final ValutaService valutaService;

    public OrderService(RootRepository rootRepository, TradeRepository tradeRepository, MarketService marketService, WalletService walletService, AccessTokenService accessTokenService, ValutaService valutaService) {
        this.rootRepository = rootRepository;
        this.tradeRepository = tradeRepository;
        this.marketService = marketService;
        this.walletService = walletService;
        this.accessTokenService = accessTokenService;
        this.valutaService = valutaService;
    }


    /** Verifies if userid in order is placed by userId of the token
     * */
    public boolean verifyAuthorizationOrder(OrderDTO orderDTO, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String[] strippedToken = authHeader.split(" ");
            return accessTokenService.validateUserIdInToken(strippedToken[1],orderDTO.getUserId());
        }else return false;
    }


    public void verifyOrderDTO(OrderDTO orderDTO, HttpServletRequest request) {
        if (!verifyAuthorizationOrder(orderDTO, request)) {
            throw new ConflictException("Authorisatie van gebruiker onjuist.");
        }
        Valuta valutaData = valutaService.currencyConversionMap.get(orderDTO.getValutaType());
        Order mappedOrder = OrderDTOtoOrderMapper.mapOrder(orderDTO);
        mappedOrder.setAmount(mappedOrder.getAmount().divide(valutaData.getCurrentExchangeRate(), RoundingMode.HALF_UP));
        mappedOrder.setAsset(rootRepository.getAssetByName(orderDTO.getAsset()).orElseThrow(()-> new ConflictException("Type asset van order niet gevonden.")));
        mappedOrder.setUser(rootRepository.getCustomerWithWallet(mappedOrder.getUser()));

        if(!checkOrderValidAmount(mappedOrder)) {
            throw new ConflictException("Onvoldoende hoeveelheid in portefeuille.");
        } else {
            saveOrder(mappedOrder);
            marketService.matchOrderToOrderBook(mappedOrder);
        }
    }

    public boolean checkOrderValidAmount(Order order) {
       return(hasSufficientBalance(order)&&hasSufficientNumberofAssets(order));
    }

    public boolean hasSufficientNumberofAssets(Order order) {
        // only if its a sell order check assets, else true
        if (!order.isBuy()) {
            BigDecimal assetInWallet = walletService.getAmountOfSingleAssetInWallet(order.getUser().getUserId(), order.getAsset().getName());
            return (assetInWallet.compareTo(order.getAmount()) >= 0);
        } else return true;
    }

    public boolean hasSufficientBalance(Order order) {
        // only if its a buy order, check balanace
        if(order.isBuy()) {
            Customer buyer = rootRepository.getCustomerWithAccount(order.getUser());
            return ((order.getAmount().multiply(order.getLimitPrice()))
                    .compareTo(buyer.getAccount().getBalance()) <= 0);
        } return true;
    }

    public List<Order> getOpenOrdersByUserId (int userId) {
        return tradeRepository.findAllOrdersByUserId(userId);
    }


    public List<Order> getAllOrders() {
      List<Order> orders = tradeRepository.findAllOrders();
      if (orders.isEmpty()) { throw new ConflictException("Geen order gevonden.");
      } else return orders;
    }

    public void saveOrder(Order order) {
        tradeRepository.saveOrder(order);
    }

    public Order getOrder(Integer id) {
       return tradeRepository.findOrder(id)
               .orElseThrow(()->new ConflictException("Order niet gevonden."));
    }

    public void deleteOrder(Integer id) {
        Optional<Order> orderOptional = tradeRepository.findOrder(id);
        if(orderOptional.isPresent()) {
            marketService.removeOrder(orderOptional.get());
            tradeRepository.deleteOrder(id);
        } else {
            throw new ConflictException("Order niet gevonden.");
        }
    }

    public void updateOrder(Order order) {
        tradeRepository.updateOrder(order);
    }

}
