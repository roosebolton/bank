package nl.hva.miw.thepiratebank.domain.transfer;


import java.math.BigDecimal;

import java.util.List;

public class AssetPriceIntervalListDTO {
    public String valuta;
    public List<AssetPriceIntervalDTO> coins;

    public AssetPriceIntervalListDTO(String valuta, List<AssetPriceIntervalDTO> coins) {
        this.valuta = valuta;
        this.coins = coins;
    }

    public String getValuta() {
        return valuta;
    }

    public void setValuta(String valuta) {
        this.valuta = valuta;
    }

    public List<AssetPriceIntervalDTO> getCoins() {
        return coins;
    }

    public void setCoins(List<AssetPriceIntervalDTO> coins) {
        this.coins = coins;
    }
}
