package nl.hva.miw.thepiratebank.controller;

import com.google.gson.*;
import nl.hva.miw.thepiratebank.service.AssetConsumerService;
import nl.hva.miw.thepiratebank.domain.transfer.AssetPriceApiConsumeDTO;
import nl.hva.miw.thepiratebank.config.AssetList;
import nl.hva.miw.thepiratebank.domain.Asset;
import nl.hva.miw.thepiratebank.domain.AssetRate;
import nl.hva.miw.thepiratebank.repository.AssetDAO;
import nl.hva.miw.thepiratebank.service.AssetRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;

@RestController
@EnableScheduling
public class AssetConsumerController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final static int API_FETCH_TIMER_SCHEDULE_MS = 300000;
    private AssetConsumerService assetConsumerService;
    private AssetRateService assetRateService;
    private AssetDAO assetDAO;

    public AssetConsumerController(AssetConsumerService assetConsumerService, AssetRateService assetRateService, AssetDAO assetDAO) {
        this.assetConsumerService = assetConsumerService;
        this.assetRateService = assetRateService;
        this.assetDAO = assetDAO;
    }


    /** Consumes api from external api every set time interval   */
    @Scheduled(fixedRate = API_FETCH_TIMER_SCHEDULE_MS)
    public void consumeExternalApi() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            AssetPriceApiConsumeDTO[] list = restTemplate.getForObject(assetConsumerService.getPreparedAPIUrl(), AssetPriceApiConsumeDTO[].class);
            assetConsumerService.createList(list);
        } catch (Exception exception) {
            log.debug(exception.getMessage());
        }
    }

//@PostConstruct
    public void onServerBootGetAssetHistory () {
        System.out.println("Filling up database with records");
        AssetList.AVAILABLE_ASSET_COINS_MAP.entrySet().stream().forEach(e -> assetDAO.create(new Asset(e.getKey(),e.getValue())));
        AssetList.AVAILABLE_ASSET_COINS_MAP.entrySet().stream().forEach(e -> saveCoinHistory(e.getKey()));
        System.out.println("Database fill completed");
    }

    public void saveCoinHistory (String coin) {
        JsonArray jsonArray = convertObjectToArray(getJsonFromApi(coin));
        for (JsonElement tempobject: jsonArray) {
            JsonArray tweedearray = tempobject.getAsJsonArray();
            AssetRate tempAsset = new AssetRate(new Asset(coin),new Timestamp(tweedearray.get(0).getAsLong()),
                    tweedearray.get(1).getAsBigDecimal());
            assetRateService.create(tempAsset);
        }
    }

    public JsonObject getJsonFromApi (String coin) {
        JsonObject rootobj = null;
        try {
            URL url = new URL("https","api.coingecko.com", 443,"/api/v3/coins/"+coin+"/market_chart?vs_currency=eur&days=1826&interval=daily");
            URLConnection request = url.openConnection();
            request.connect();
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
            rootobj = root.getAsJsonObject(); //May be an array, may be an object.
        } catch(Exception error) {
            log.debug("Connection error: "+error);
        }
        return rootobj;
    }


    public JsonArray convertObjectToArray (JsonObject jsonObject) {
        if (jsonObject != null) {
        JsonElement results = jsonObject.getAsJsonObject().get("prices");
        JsonArray objectArray = results.getAsJsonArray();
            return objectArray;
        } else return null;

    }

}

