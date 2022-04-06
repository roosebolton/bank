package nl.hva.miw.thepiratebank.domain.transfer.calculation;

import nl.hva.miw.thepiratebank.domain.Asset;
import nl.hva.miw.thepiratebank.domain.transfer.AssetPriceIntervalDTO;
import nl.hva.miw.thepiratebank.domain.Valuta;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class IntervalCalculationPrice implements IntervalCalculation {

    @Override
    public AssetPriceIntervalDTO calculate(Valuta valuta, Asset asset, BigDecimal current, BigDecimal day, BigDecimal week, BigDecimal month, BigDecimal quarter, BigDecimal hyear, BigDecimal year) {
        AssetPriceIntervalDTO assetPriceIntervalDTO = new AssetPriceIntervalDTO(asset.getName(),
                current.multiply(valuta.getCurrentExchangeRate()).setScale(4, RoundingMode.HALF_UP),
                day.multiply(valuta.getCurrentExchangeRate()).setScale(4, RoundingMode.HALF_UP),
                week.multiply(valuta.getCurrentExchangeRate()).setScale(4, RoundingMode.HALF_UP),
                month.multiply(valuta.getCurrentExchangeRate()).setScale(4, RoundingMode.HALF_UP),
                quarter.multiply(valuta.getCurrentExchangeRate()).setScale(4, RoundingMode.HALF_UP),
                hyear.multiply(valuta.getCurrentExchangeRate()).setScale(4, RoundingMode.HALF_UP),
                year.multiply(valuta.getCurrentExchangeRate()).setScale(4, RoundingMode.HALF_UP));
       return assetPriceIntervalDTO;
    }
}
