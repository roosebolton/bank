package nl.hva.miw.thepiratebank.service.dtomapper;


import nl.hva.miw.thepiratebank.domain.Asset;
import nl.hva.miw.thepiratebank.domain.Valuta;
import nl.hva.miw.thepiratebank.domain.transfer.AssetPriceIntervalDTO;
import nl.hva.miw.thepiratebank.domain.transfer.calculation.IntervalCalculation;

import java.math.BigDecimal;

public  class AssetPriceIntervalDTOMaker {
    public static AssetPriceIntervalDTO makeDTO (Valuta valuta, Asset asset,
                                                 BigDecimal current, BigDecimal day,
                                                 BigDecimal week, BigDecimal month,
                                                 BigDecimal quarter, BigDecimal hyear, BigDecimal year,
                                                 IntervalCalculation intervalCalculation) {

        return intervalCalculation.calculate(valuta,asset,current,day,week,month,quarter,hyear,year);
    }
}
