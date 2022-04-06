package nl.hva.miw.thepiratebank.domain.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class ValutaConsumeDTO {
    @JsonProperty("EUR_USD")
    private BigDecimal USD;
    @JsonProperty("EUR_GBP")
    private BigDecimal GBP;


    public BigDecimal getUSD() {
        return USD;
    }

    public void setUSD(BigDecimal USD) {
        this.USD = USD;
    }

    public BigDecimal getGBP() {
        return GBP;
    }

    public void setGBP(BigDecimal GBP) {
        this.GBP = GBP;
    }

    @Override
    public String toString() {
        return "ValutaConsumeDTO{" +
                "USD_exchangerate=" + USD +
                ", EUR_echangerate=" + GBP +
                '}';
    }
}
