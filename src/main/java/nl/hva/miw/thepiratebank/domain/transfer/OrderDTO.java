package nl.hva.miw.thepiratebank.domain.transfer;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.stream.Stream;


public class OrderDTO {
    // boolean needs to be object instead of primary, otherwise it will be mapped to false when null
    private Boolean buy;
    private int userId;
    private String asset;
    private BigDecimal amount;
    private BigDecimal price;
    private String valutaType;

    public boolean isBuy() {
        return buy;
    }

    public void setBuy(boolean buy) {
        this.buy = buy;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getValutaType() {
        return valutaType;
    }

    public void setValutaType(String valutaType) {
        this.valutaType = valutaType;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "buy=" + buy +
                ", userId=" + userId +
                ", asset='" + asset + '\'' +
                ", amount=" + amount +
                ", price=" + price +
                ", valutaType='" + valutaType + '\'' +
                '}';
    }

    public boolean isNonNull() {
        return Stream.of(buy,userId,asset,amount,price).allMatch(Objects::nonNull);
    }
}
