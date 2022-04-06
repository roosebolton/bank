package nl.hva.miw.thepiratebank.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

public class Transaction implements Comparable<Transaction> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private int transactionId;

    @JsonManagedReference
    private Customer buyer;
    @JsonManagedReference
    private Customer seller;
    @JsonManagedReference
    private Asset asset;

    private BigDecimal assetAmount;
    private BigDecimal value;
    private BigDecimal transactionCost;
    private Timestamp transactionDate;

   public Transaction() {
       log.info("New Transaction using no arg constructor.");
    }

     private Transaction(TransactionBuilder builder) {
       log.info("New Transaction using all args constructor.");
        this.transactionId = builder.transactionID;
        this.buyer = builder.buyer;
        this.seller = builder.seller;
        this.asset = builder.asset;
        this.assetAmount = builder.assetAmount;
        this.value = builder.value;
        this.transactionCost = builder.transactionCost;
        this.transactionDate = builder.transactionDate;
    }

    @Override
    public int compareTo(Transaction o) {
        return this.transactionId-o.transactionId;
    }


    public static class TransactionBuilder {
        private int transactionID;
        private Customer buyer;
        private Customer seller;
        private Asset asset;
        private BigDecimal assetAmount;
        private BigDecimal value;
        private BigDecimal transactionCost;
        private Timestamp transactionDate;
        public TransactionBuilder() {

        }

        public TransactionBuilder transactionId (int transactionID) {
            this.transactionID = transactionID;
            return this;
        }

        public TransactionBuilder buyer (Customer buyer) {
            this.buyer = buyer;
            return this;
        }

        public TransactionBuilder seller (Customer seller) {
            this.seller = seller;
            return this;
        }

        public TransactionBuilder asset (Asset asset) {
            this.asset = asset;
            return this;
        }

        public TransactionBuilder assetAmount (BigDecimal amount) {
            this.assetAmount= amount;
            return this;
        }

        public TransactionBuilder value (BigDecimal value) {
            this.value= value;
            return this;
        }

        public TransactionBuilder transactionCost(BigDecimal transactionCost) {
            this.transactionCost= transactionCost;
            return this;
        }

        public TransactionBuilder transactionDate(Timestamp transactionDate) {
            this.transactionDate= transactionDate;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }


        public BigDecimal getValue() {
            return value;
        }
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public Customer getBuyer() {
        return buyer;
    }

    public Customer getSeller() {
        return seller;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public BigDecimal getAssetAmount() {
        return assetAmount;
    }

    public void setAssetAmount(BigDecimal amount) {
       assetAmount = amount;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getTransactionCost() {
        return transactionCost;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionCost(BigDecimal transactionCost) {
        this.transactionCost = transactionCost;
    }

    public void setBuyer(Customer buyer) {
        this.buyer = buyer;
    }

    public void setSeller(Customer seller) {
        this.seller = seller;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return transactionId == that.transactionId && Objects.equals(buyer, that.buyer) && Objects.equals(seller, that.seller) && Objects.equals(asset, that.asset) && Objects.equals(assetAmount, that.assetAmount) && Objects.equals(value, that.value) && Objects.equals(transactionCost, that.transactionCost) && Objects.equals(transactionDate, that.transactionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, buyer, seller, asset, assetAmount, value, transactionCost, transactionDate);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionID=" + transactionId +
                ", buyer=" + buyer +
                ", seller=" + seller +
                ", asset=" + asset +
                ", assetAmount=" + assetAmount +
                ", value=" + value +
                ", transactionCost=" + transactionCost +
                ", transactionDate=" + transactionDate +
                '}';
    }
}