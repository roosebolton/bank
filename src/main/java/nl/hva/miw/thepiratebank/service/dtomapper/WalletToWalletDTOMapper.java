package nl.hva.miw.thepiratebank.service.dtomapper;

import nl.hva.miw.thepiratebank.domain.Asset;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class WalletToWalletDTOMapper {

    public static Map<String, BigDecimal> mapWalletToWalletDTO(Map<Asset, BigDecimal> assetsInWallet) {
        Map<String, BigDecimal> cleanedMap = new HashMap<>();
        for (Map.Entry entry: assetsInWallet.entrySet()) {
            Asset asset = (Asset) entry.getKey();
            BigDecimal amount = (BigDecimal) entry.getValue();
            //afkorting erin gooien ipv heel asset object
            cleanedMap.put(asset.getAbbreviation(), amount);
        }
        return cleanedMap;
    }

}
