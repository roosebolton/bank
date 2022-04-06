package nl.hva.miw.thepiratebank.domain.transfer.calculation;

import nl.hva.miw.thepiratebank.domain.Asset;
import nl.hva.miw.thepiratebank.domain.transfer.AssetPriceIntervalDTO;
import nl.hva.miw.thepiratebank.domain.Valuta;

import java.math.BigDecimal;

public interface IntervalCalculation {
    AssetPriceIntervalDTO calculate(Valuta valuta, Asset asset,
                                    BigDecimal current,
                                    BigDecimal day,
                                    BigDecimal week,
                                    BigDecimal month,
                                    BigDecimal quarter,
                                    BigDecimal hyear,
                                    BigDecimal year);

}