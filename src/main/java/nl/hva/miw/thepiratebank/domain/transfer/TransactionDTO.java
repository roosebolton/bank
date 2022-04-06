package nl.hva.miw.thepiratebank.domain.transfer;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.stream.Stream;

public class TransactionDTO {
    private int buyer;
    private int seller;
    private String assetname;
    private BigDecimal assetamount;


    public int getBuyer() {
        if (buyer != 0) {
            return buyer;
        } else {
            return 1000;
        }
    }

    public void setBuyer(int buyer) { this.buyer = buyer;
    }

    public int getSeller() {
        if (seller != 0) {
            return seller;
        } else {
            return 1000;
        }
    }

    public void setSeller(int seller) { this.seller = seller; }

    public String getAssetname() {
        return assetname;
    }

    public void setAssetname(String assetname) {
        this.assetname = assetname;
    }

    public BigDecimal getAssetamount() {
        return assetamount;
    }

    public void setAssetamount(BigDecimal assetamount) {
        this.assetamount = assetamount;
    }

    public boolean isNonNull() {
        return Stream.of(getBuyer(),getSeller(),assetname,assetamount).allMatch(Objects::nonNull);
    }

    @Override
    public String toString() {
        return "TransactionDTO{" +
                "buyer=" + buyer +
                ", seller=" + seller +
                ", assetname='" + assetname + '\'' +
                ", assetamount=" + assetamount +
                '}';
    }
}
