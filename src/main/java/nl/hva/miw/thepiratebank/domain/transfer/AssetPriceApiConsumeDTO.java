package nl.hva.miw.thepiratebank.domain.transfer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetPriceApiConsumeDTO {
        @JsonProperty("id")
        String id;
        @JsonProperty("current_price")
        String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ApiConsumeDTO{" +
                "id='" + id + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}







