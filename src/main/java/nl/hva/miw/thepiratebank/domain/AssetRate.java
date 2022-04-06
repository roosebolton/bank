package nl.hva.miw.thepiratebank.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

public class AssetRate implements Comparable<AssetRate> {
    private Asset asset;
    private Timestamp timestamp;
    private BigDecimal value;

    public AssetRate() {
    }

    public AssetRate(Asset asset, Timestamp timestamp, BigDecimal value) {
        this.asset = asset;
        this.timestamp = timestamp;
        this.value = value;
    }

    public AssetRate(Timestamp timestamp, BigDecimal value) {
        this(null, timestamp, value);
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AssetRate assetRate = (AssetRate) o;

        if (!Objects.equals(asset, assetRate.asset)) return false;
        if (!Objects.equals(timestamp, assetRate.timestamp)) return false;
        return Objects.equals(value, assetRate.value);
    }

    @Override
    public int hashCode() {
        int result = asset != null ? asset.hashCode() : 0;
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(AssetRate o) {
        return         o.value.compareTo(this.value);

    }
}
