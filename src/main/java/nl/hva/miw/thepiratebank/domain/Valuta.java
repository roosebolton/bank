package nl.hva.miw.thepiratebank.domain;

import java.math.BigDecimal;

public class Valuta {
    private String name;
    private BigDecimal currentExchangeRate;

    public Valuta(String name, BigDecimal currentExchangeRate) {
        this.name = name;
        this.currentExchangeRate = currentExchangeRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCurrentExchangeRate() {
        return currentExchangeRate;
    }

    public void setCurrentExchangeRate(BigDecimal currentExchangeRate) {
        this.currentExchangeRate = currentExchangeRate;
    }

    @Override
    public String toString() {
        return "Valuta{" +
                "name='" + name + '\'' +
                ", currentExchangeRate=" + currentExchangeRate +
                '}';
    }
}
