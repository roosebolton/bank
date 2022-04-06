package nl.hva.miw.thepiratebank.domain;

import java.util.List;
import java.util.Objects;

public class Asset {
    private String name;
    private String abbreviation;
    private List<AssetRate> pricehistory;


    public Asset(String asset, String abbreviation) {
        this.name = asset;
        this.abbreviation = abbreviation;
    }

    public Asset(String name) {
        this.name = name;
    }

    public Asset(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public List<AssetRate> getPricehistory() {
        return pricehistory;
    }

    public void setPricehistory(List<AssetRate> pricehistory) {
        this.pricehistory = pricehistory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asset asset = (Asset) o;
        return Objects.equals(name, asset.name) && Objects.equals(abbreviation, asset.abbreviation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, abbreviation);
    }

    @Override
    public String toString() {
        return "Asset{" +
                "name='" + name + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", pricehistory=" + pricehistory +
                '}';
    }
}
