package nl.hva.miw.thepiratebank.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

public class Wallet {
    @JsonBackReference
    private Customer customer;
    private BigDecimal totalValue;
    private Map<Asset, BigDecimal> assetsInWallet;

    private Wallet(Customer customer, BigDecimal totalValue, Map<Asset, BigDecimal> assetsInWallet) {
        super();
        this.customer = customer;
        this.totalValue = totalValue;
        this.assetsInWallet = assetsInWallet;
    }

    public Wallet(BigDecimal totalValue, Map<Asset, BigDecimal> assetsInWallet) {
        this(null, totalValue,  assetsInWallet);
    }

    public Wallet() {
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public Map<Asset, BigDecimal> getAssetsInWallet() {
        return assetsInWallet;
    }

    public void setAssetsInWallet(Map<Asset, BigDecimal> assetsInWallet) {
        this.assetsInWallet = assetsInWallet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return Objects.equals(customer, wallet.customer) && totalValue.equals(wallet.totalValue) && assetsInWallet.equals(wallet.assetsInWallet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, totalValue, assetsInWallet);
    }

    @Override
    public String toString() {
        return "Wallet{" +
                ", totalValue=" + totalValue +
                ", assetsInWallet=" + assetsInWallet +
                '}';
    }
}
