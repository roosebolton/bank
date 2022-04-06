package nl.hva.miw.thepiratebank.domain.transfer;
import nl.hva.miw.thepiratebank.domain.Wallet;
import nl.hva.miw.thepiratebank.service.dtomapper.WalletToWalletDTOMapper;

import java.math.BigDecimal;
import java.util.Map;


public class WalletDTO {
    BigDecimal total_value;
    Map<String, BigDecimal> assets;


    public WalletDTO(Wallet wallet) {
        this.total_value = wallet.getTotalValue();
        this.assets = WalletToWalletDTOMapper.mapWalletToWalletDTO(wallet.getAssetsInWallet());
    }

    public WalletDTO() {
        this.total_value = new BigDecimal(0);
        this.assets = null;
    }

    public BigDecimal getTotal_value() {
        return total_value;
    }

    public Map<String, BigDecimal> getAssets_in_wallet() {
        return assets;
    }


}
