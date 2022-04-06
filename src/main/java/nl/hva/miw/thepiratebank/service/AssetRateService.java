package nl.hva.miw.thepiratebank.service;

import nl.hva.miw.thepiratebank.config.AssetList;
import nl.hva.miw.thepiratebank.domain.Asset;
import nl.hva.miw.thepiratebank.domain.Valuta;
import nl.hva.miw.thepiratebank.domain.transfer.AssetHistoryDTO;
import nl.hva.miw.thepiratebank.domain.transfer.AssetPriceIntervalDTO;
import nl.hva.miw.thepiratebank.domain.AssetRate;
import nl.hva.miw.thepiratebank.domain.transfer.calculation.IntervalCalculation;
import nl.hva.miw.thepiratebank.domain.transfer.calculation.IntervalCalculationDelta;
import nl.hva.miw.thepiratebank.domain.transfer.calculation.IntervalCalculationPrice;
import nl.hva.miw.thepiratebank.repository.RootRepository;
import nl.hva.miw.thepiratebank.repository.assetrate.AssetRateDAO;
import nl.hva.miw.thepiratebank.domain.transfer.AssetPriceIntervalListDTO;
import nl.hva.miw.thepiratebank.service.dtomapper.AssetPriceIntervalDTOMaker;
import nl.hva.miw.thepiratebank.utilities.exceptions.ConflictException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssetRateService {
    private final AssetRateDAO assetRateDAO;
    private final ValutaService valutaService;
    private final RootRepository rootRepository;

    public AssetRateService(AssetRateDAO assetRateDAO, ValutaService valutaService, RootRepository rootRepository) {
        this.assetRateDAO = assetRateDAO;
        this.valutaService = valutaService;
        this.rootRepository = rootRepository;
    }

    public void create(AssetRate assetRate) {
        assetRateDAO.create(assetRate);
    }

    public BigDecimal getCurrentValue(String name) {
        return assetRateDAO.get(name).getValue();

    }

    public List<String> getAvailableAssetIDs() {
        return AssetList.AVAILABLE_ASSET_COINS_MAP.keySet().stream().collect(Collectors.toList());
    }

    public List<Asset> getAvailableAssets() {
        return rootRepository.getAllAssets().stream().collect(Collectors.toList());
    }

    /** Method getAssetHistory
     * Gives full history of assetprice in selected valuta
     * @param valuta
     * @param name
     * @return
     */
     public AssetHistoryDTO getAssetHistory(String valuta, String name ) {
        AssetHistoryDTO coinDTO = new AssetHistoryDTO.AssetHistoryDTOBuilder()
                .valuta(valutaService.getValuta(valuta))
                .asset(rootRepository.getAssetWithFullHistory(name).orElseThrow(() -> new ConflictException("No history of coin found.")))
                .build();
        return coinDTO;
    }

    /** Method getInterval
     * Gives a history of selected coins in interval period,
     * Converts to selected currency and has a variable display type
     * display options :
     * price -> value of asset in selected currency.
     * delta -> displays price in delta parameter (currentprice - price at date in time /  price at date in time)
     * @param inputValuta
     * @param requestedIds
     * @param displayType
     * @return AssetPriceIntervalListDTO
     */
    public AssetPriceIntervalListDTO getInterval(String inputValuta, List<String> requestedIds, String displayType) {
        Valuta valuta = valutaService.getValuta(inputValuta);

        List<Asset> assetList = getRequestedList(requestedIds);

        List<AssetPriceIntervalDTO> assetsInterval = assetList.stream().map(asset -> AssetPriceIntervalDTOMaker.makeDTO(valuta, asset,
                getCurrentValue(asset.getName()),getClosest1day(asset.getName(), 1),
                getClosest1Week(asset.getName(), 1), getClosestMonth(asset.getName(), 1),
                getClosestMonth(asset.getName(), 3), getClosestMonth(asset.getName(), 6),
                getClosest1Year(asset.getName(), 1), getIntervalTypeCalculation(displayType))).collect(Collectors.toList());

        return new AssetPriceIntervalListDTO(valuta.getName(),assetsInterval);
    }

    /** Method getRequestedList
     * Validates controller input for existing coins/all coins and returns assetsList for corresponding id's
     * @param requestedIds
     * @return
     */
    public List<Asset> getRequestedList (List<String> requestedIds) {
        // if all is in keyword -> get all assets, otherwise get assets in list
        if (requestedIds.contains("all")) {
            return getAvailableAssetIDs().stream().map(assetname -> rootRepository.getAssetByName(assetname).orElseThrow()).collect(Collectors.toList());
        } else {
            List<Asset> assetlist = new ArrayList();
            for (String id : requestedIds) {
                Optional<Asset> asset = rootRepository.getAssetByName(id);
                if (asset.isPresent()) {
                    assetlist.add(asset.get());
                }
            }return assetlist;
        }
    }

    public IntervalCalculation getIntervalTypeCalculation(String calculationType) {
        if(calculationType.equals("price")) {
            return new IntervalCalculationPrice();
        } else return new IntervalCalculationDelta();
    }


    /** Methods for getting specific values in time of an Asset.
     * Beware: Only returns Bigdecimal, it doesnt return asset/object or full pricehistory,
     * @param name
     * @return
     */
    public BigDecimal getClosest (String name) {
        BigDecimal closest_current = assetRateDAO.getClosest(name, Timestamp.valueOf(LocalDateTime.now()))
                .stream().findFirst().map(e -> e.getValue()).orElse(BigDecimal.valueOf(0));
        return closest_current;
    }

    public BigDecimal getClosest1day(String name, int days) {
        return assetRateDAO.getClosest(name, Timestamp.valueOf(LocalDateTime.now().minusDays(days)))
                .stream().findFirst().map(e -> e.getValue()).orElse(BigDecimal.valueOf(0));
    }

    public BigDecimal getClosest1Week(String name, int weeks) {
        return assetRateDAO.getClosest(name, Timestamp.valueOf(LocalDateTime.now().minusWeeks(weeks)))
                .stream().findFirst().map(e -> e.getValue()).orElse(BigDecimal.valueOf(0));
    }

    public BigDecimal getClosestMonth(String name,int months) {
        return assetRateDAO.getClosest(name, Timestamp.valueOf(LocalDateTime.now().minusMonths(months)))
                .stream().findFirst().map(e -> e.getValue()).orElse(BigDecimal.valueOf(0));
    }

    public BigDecimal getClosest1Year(String name, int years) {
        return assetRateDAO.getClosest(name, Timestamp.valueOf(LocalDateTime.now().minusYears(years)))
                .stream().findFirst().map(e -> e.getValue()).orElse(BigDecimal.valueOf(0));
    }


}
