package nl.hva.miw.thepiratebank.service;

import nl.hva.miw.thepiratebank.domain.Valuta;
import nl.hva.miw.thepiratebank.domain.transfer.ValutaConsumeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class ValutaService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final static String API_CURRENCY_URL = "https://free.currconv.com/api/v7/convert?q=EUR_USD,EUR_GBP&compact=ultra&apiKey=";
    private final static String APi_CURRENCY_KEY = "fccf1ff797181da9bf18";

    Map<String, Valuta> currencyConversionMap;

    public ValutaService() {
        this.currencyConversionMap = new HashMap<>();
        currencyConversionMap.put("EUR",new Valuta("EUR", BigDecimal.ONE));
        //Backup if api fails
        currencyConversionMap.put("USD",new Valuta("USD", BigDecimal.valueOf(1.12)));
        currencyConversionMap.put("GBP",new Valuta("GBP", BigDecimal.valueOf(0.85)));
    }

    /**
     * /**
     *  Consumes currency api from external at fixed time
     */
    @Scheduled(fixedRate = 3600000 )
    public void consumeExternalApi() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ValutaConsumeDTO valutaDTO = restTemplate.getForObject(API_CURRENCY_URL+APi_CURRENCY_KEY, ValutaConsumeDTO.class);
           if(valutaDTO != null) {
               saveValutaDTOToMap(valutaDTO);
           }
        } catch (Exception exception) {
            log.debug(exception.getMessage());
        }
    }

    /**
     * Saves consumed API DTO to valutamap
     * @param valutaConsumeDTO
     */
    public void saveValutaDTOToMap(ValutaConsumeDTO valutaConsumeDTO) {
        currencyConversionMap.put("GBP",new Valuta("GBP", valutaConsumeDTO.getGBP()));
        currencyConversionMap.put("USD",new Valuta("USD", valutaConsumeDTO.getUSD()));
    }

    /**
     * Returns valuta object with current exhangerate and name
     * @param valuta
     * @return
     */
    public Valuta getValuta (String valuta) {
       return (currencyConversionMap.containsKey(valuta)) ? currencyConversionMap.get(valuta) : currencyConversionMap.get("EUR");
    }

}
