package nl.hva.miw.thepiratebank.domain.transfer;

import nl.hva.miw.thepiratebank.domain.Asset;
import nl.hva.miw.thepiratebank.domain.Valuta;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class AssetHistoryDTO {
    public final String valuta;
    public final String name;
    public final String abbreviation;
    public final BigDecimal current_price;
    public final List<AssetRateDTO> pricehistory;

    private AssetHistoryDTO(AssetHistoryDTOBuilder builder) {
        this.valuta = builder.valuta.getName();
        this.name = builder.asset.getName();
        this.abbreviation = builder.asset.getAbbreviation();
        this.current_price = builder.current_price;
        this.pricehistory = builder.pricehistory;
    }

    public static class AssetHistoryDTOBuilder {
        private Valuta valuta;
        private Asset asset;
        private BigDecimal current_price;
        private List<AssetRateDTO> pricehistory;

        public AssetHistoryDTOBuilder() {
        }

        public AssetHistoryDTOBuilder valuta (Valuta valuta){
            this.valuta = valuta;
            return this;
        }

        public AssetHistoryDTOBuilder asset (Asset asset) {
            this.asset = asset;
            return this;
        }

        public AssetHistoryDTO build () {
            // converts assetrate of asset to selected currency and timestamp long for better easier
            this.pricehistory = asset.getPricehistory()
                    .stream().map(element ->
                            new AssetRateDTO(element.getTimestamp().getTime(),
                                    element.getValue().multiply(valuta.getCurrentExchangeRate())))
                    .collect(Collectors.toList());
            this.current_price = pricehistory.get(pricehistory.size()-1).getValue();
            return new AssetHistoryDTO(this);
        }

    }


}
