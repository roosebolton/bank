package nl.hva.miw.thepiratebank.domain.transfer;

import java.math.BigDecimal;

public class AssetPriceIntervalDTO {
    public String symbol;
    public BigDecimal current;
    public BigDecimal day;
    public BigDecimal week;
    public BigDecimal month;
    public BigDecimal quarter;
    public BigDecimal hyear;
    public BigDecimal year;


    public AssetPriceIntervalDTO(String symbol, BigDecimal current, BigDecimal day, BigDecimal week, BigDecimal month, BigDecimal quarter, BigDecimal hyear, BigDecimal year) {
        this.symbol = symbol;
        this.current = current;
        this.day = day;
        this.week = week;
        this.month = month;
        this.quarter = quarter;
        this.hyear = hyear;
        this.year = year;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return "AssetPrice{" +
                "symbol='" + symbol + '\'' +
                ", current=" + current +
                ", day=" + day +
                ", week=" + week +
                ", month=" + month +
                ", quarter=" + quarter +
                ", hyear=" + hyear +
                ", year=" + year +
                '}';
    }
}