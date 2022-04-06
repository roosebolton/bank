package nl.hva.miw.thepiratebank.service;



import nl.hva.miw.thepiratebank.domain.transfer.AssetPriceApiConsumeDTO;
import nl.hva.miw.thepiratebank.config.AssetList;
import nl.hva.miw.thepiratebank.domain.Asset;
import nl.hva.miw.thepiratebank.domain.AssetRate;
import nl.hva.miw.thepiratebank.repository.assetrate.AssetRateDAO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AssetConsumerService {
    /**
     *  Api settings
     */
    private String API_URL = "https://api.coingecko.com/api/v3/coins/markets?";
    private String CURRENCY_SELECT = "EUR";
    private String CURRENCY_SELECT_OPTION = "vs_currency=";

    private final AssetRateDAO assetRateDAO;


    public AssetConsumerService(AssetRateDAO assetRateDAO) {
        this.assetRateDAO = assetRateDAO;
    }

    public String getPreparedAPIUrl() {
       return prepareAPI_URl(API_URL,prepareFormattedCoinList(AssetList.AVAILABLE_ASSET_COINS_MAP.keySet()),CURRENCY_SELECT);
    }

    public String prepareFormattedCoinList (Set<String> assets) {
       return assets.stream()
                .collect(Collectors.joining(","));
    }

    public String prepareAPI_URl (String apiUrl,String coinList, String currency) {
        return apiUrl + CURRENCY_SELECT_OPTION +currency+"&ids="+coinList;
    }

    public void createList(AssetPriceApiConsumeDTO[] assets) {
        List<AssetRate> assetValueList = Arrays.stream(assets).map(asset ->
                        new AssetRate(
                                new Asset(asset.getId()),
                                new Timestamp(System.currentTimeMillis()),
                                new BigDecimal(asset.getValue())))
                .collect(Collectors.toList());
        assetValueList.forEach( a -> assetRateDAO.create(a));
    }

}
