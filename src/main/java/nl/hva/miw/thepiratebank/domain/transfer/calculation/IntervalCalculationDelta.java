package nl.hva.miw.thepiratebank.domain.transfer.calculation;

import nl.hva.miw.thepiratebank.domain.Asset;
import nl.hva.miw.thepiratebank.domain.transfer.AssetPriceIntervalDTO;
import nl.hva.miw.thepiratebank.domain.Valuta;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class IntervalCalculationDelta implements IntervalCalculation {

    @Override
    public AssetPriceIntervalDTO calculate(Valuta valuta, Asset asset, BigDecimal current, BigDecimal day, BigDecimal week, BigDecimal month, BigDecimal quarter, BigDecimal hyear, BigDecimal year) {
       BigDecimal closest_current = current;
        return new AssetPriceIntervalDTO(asset.getName(),
              current.multiply(valuta.getCurrentExchangeRate()).setScale(4, RoundingMode.HALF_UP),
                (closest_current.subtract(day)).divide(day,4, RoundingMode.HALF_UP),
                (closest_current.subtract(week)).divide(week,4, RoundingMode.HALF_UP),
                (closest_current.subtract(month)).divide(month,4, RoundingMode.HALF_UP),
                (closest_current.subtract(quarter)).divide(quarter,4, RoundingMode.HALF_UP),
                (closest_current.subtract(hyear)).divide(hyear,4, RoundingMode.HALF_UP),
                (closest_current.subtract(year)).divide(year,4, RoundingMode.HALF_UP));

    }
}
