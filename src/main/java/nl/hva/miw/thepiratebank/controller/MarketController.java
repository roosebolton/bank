package nl.hva.miw.thepiratebank.controller;

import nl.hva.miw.thepiratebank.domain.Asset;
import nl.hva.miw.thepiratebank.domain.Order;
import nl.hva.miw.thepiratebank.domain.transfer.OrderBookSumDTO;
import nl.hva.miw.thepiratebank.domain.transfer.OrderDTO;
import nl.hva.miw.thepiratebank.service.market.MarketService;
import nl.hva.miw.thepiratebank.service.OrderService;
import nl.hva.miw.thepiratebank.service.market.*;
import nl.hva.miw.thepiratebank.utilities.authorization.token.AccessTokenService;
import nl.hva.miw.thepiratebank.utilities.exceptions.AuthorizationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/market")
public class MarketController {
    private final MarketService marketService;
    private final OrderService orderService;
    private final AccessTokenService accessTokenService;

    public MarketController(MarketService marketService, OrderService orderService, AccessTokenService accessTokenService) {
        this.marketService = marketService;
        this.orderService = orderService;
        this.accessTokenService = accessTokenService;
    }

    @GetMapping("")
    @ResponseBody
    public ResponseEntity<?> getOrderbooks(@RequestParam(defaultValue = "all") List<String> id) {
        List<MarketOrderBook<Asset>> orderbooks = marketService.getOrderBooks(id);
        return ResponseEntity.ok().body(orderbooks);
    }

    @GetMapping("/orderbook")
    @ResponseBody
    public ResponseEntity<?> getOrderbook(@RequestParam(defaultValue = "bitcoin") String asset) {
        OrderBookSumDTO orderbook = marketService.getOrderBookSum(asset);
        return ResponseEntity.ok().body(orderbook);
    }

    @PostMapping(value = "/trade")
    public ResponseEntity<String> tradeAssets(@RequestBody OrderDTO orderDTO, HttpServletRequest request) {
        orderService.verifyOrderDTO(orderDTO, request);
        return ResponseEntity.ok("Order succesvol geplaatst");
    }

    @GetMapping("/orders")
    @ResponseBody
    public ResponseEntity<?> getOrder(HttpServletRequest request) {
        List<Order> ordersOfUser = orderService.getOpenOrdersByUserId(
                accessTokenService.getTokenFromRequest(request)
                .orElseThrow(()-> new AuthorizationException("Authorization failed"))) ;
        return ResponseEntity.ok().body(ordersOfUser);
    }
}

