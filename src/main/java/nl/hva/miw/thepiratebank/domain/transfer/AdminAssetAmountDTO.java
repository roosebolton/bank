package nl.hva.miw.thepiratebank.domain.transfer;

import java.math.BigDecimal;

public class AdminAssetAmountDTO {

        private String userName;
        private String assetName;
        private BigDecimal assetAmount;

        public AdminAssetAmountDTO() {
            this.userName = "";
            this.assetName = "";
            this.assetAmount = BigDecimal.ZERO;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getAssetName() {
            return assetName;
        }

        public void setAssetName(String assetName) {
            this.assetName = assetName;
        }

        public BigDecimal getAssetAmount() {
            return assetAmount;
        }

        public void setAssetAmount(BigDecimal assetAmount) {
            this.assetAmount = assetAmount;
        }
    }


