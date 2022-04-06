package nl.hva.miw.thepiratebank.domain.transfer;

import nl.hva.miw.thepiratebank.domain.Asset;

import java.math.BigDecimal;

public class AssetRateDTO implements Comparable<AssetRateDTO> {
    private String asset;
    private Long timestamp;
    private BigDecimal value;

      public AssetRateDTO(Long timestamp, BigDecimal value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AssetRateDTO assetRate = (AssetRateDTO) o;

        if (!timestamp.equals(assetRate.timestamp)) return false;
        return asset.equals(assetRate.asset);
    }

    @Override
    public int hashCode() {
        int result = timestamp.hashCode();
        result = 31 * result + asset.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "AssetRate{" +
                "timestamp=" + timestamp +
                ", value=" + value +
                ", asset=" + asset +
                '}';
    }

    @Override
    public int compareTo(AssetRateDTO o) {
        int i = this.getValue().compareTo(o.getValue());
        return -i;
    }
}
