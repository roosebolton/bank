package nl.hva.miw.thepiratebank.repository;

import nl.hva.miw.thepiratebank.controller.AssetConsumerController;
import nl.hva.miw.thepiratebank.domain.Asset;
import nl.hva.miw.thepiratebank.domain.AssetRate;
import nl.hva.miw.thepiratebank.repository.assetrate.AssetRateDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AssetRateDAOTest {

    private AssetRateDAO assetRateDAOtest;

    //Necessary to prevent postconstruct annotation from running do not delete
    @MockBean
    private AssetConsumerController assetConsumerController;


    @Autowired
    public AssetRateDAOTest(AssetRateDAO assetRateDAOtest) {
        super();
        this.assetRateDAOtest = assetRateDAOtest;
    }

    @Test
    void getAll() {
        assetRateDAOtest.getAll();
        List<AssetRate> assetRates = assetRateDAOtest.getAll();
        assertThat(assetRates).isNotNull().isNotEmpty();
        assertThat(assetRates.size()).isEqualTo(10);
   }

    @Test
    void create() {
        List<AssetRate> assetrates = assetRateDAOtest.getAll();
        assertThat(assetrates.size()).isEqualTo(10);
        Asset binance = new Asset("Binance", null);
        AssetRate expected = new AssetRate(binance, Timestamp.valueOf("2022-03-10 02:00:00"), BigDecimal.valueOf(1000.5));
        assetRateDAOtest.create(expected);

        List<AssetRate> assetrates2 = assetRateDAOtest.getAll();
        assertThat(assetrates2.size()).isEqualTo(11);

        AssetRate actual = assetrates2.get(10);
        assertThat(actual).isNotNull().isEqualTo(expected);
        assetRateDAOtest.deleteAssetRateByTimestamp("Binance",Timestamp.valueOf("2022-03-10 02:00:00"));
        List<AssetRate> assetrates3 = assetRateDAOtest.getAll();
        assertThat(assetrates3.size()).isEqualTo(10);
    }

    @Test
    void get() {
        AssetRate actual = assetRateDAOtest.get("Ethereum");
        Asset asset = new Asset("Ethereum", null);
        AssetRate expected = new AssetRate(asset, Timestamp.valueOf("2022-03-10 02:00:00"), BigDecimal.valueOf(47.1));
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void getAssetRateByTimestamp() {
        AssetRate actual = assetRateDAOtest.getAssetRateByTimestamp("Bitcoin", Timestamp.valueOf("2022-02-10 02:00:00"));
        Asset asset = new Asset("Bitcoin", null);
        AssetRate expected = new AssetRate(asset, Timestamp.valueOf("2022-02-10 02:00:00"), BigDecimal.valueOf(1133.8));
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void getClosest() {
        List<AssetRate> actual = assetRateDAOtest.getClosest("Bitcoin", Timestamp.valueOf("2021-12-10 02:00:00"));
        System.out.println(actual);
        Asset asset = new Asset("Bitcoin", null);
        AssetRate assetRate = new AssetRate(asset, Timestamp.valueOf("2021-12-10 02:00:00"), BigDecimal.valueOf(1172.8));
        List<AssetRate> expected = new ArrayList<>();
        expected.add(assetRate);
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void update() {
        Asset asset = new Asset("Ethereum", null);
        AssetRate expected = new AssetRate(asset, Timestamp.valueOf("2022-03-09 02:00:00"), BigDecimal.valueOf(60.0));
        assetRateDAOtest.update(expected);
        System.out.println(expected);
        AssetRate actual = assetRateDAOtest.getAssetRateByTimestamp("Ethereum", Timestamp.valueOf("2022-03-09 02:00:00"));
        System.out.println(actual);
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void delete() {
        List<AssetRate> assetRates = assetRateDAOtest.getAll();
        assertThat(assetRates.size()).isEqualTo(10);
        assetRateDAOtest.delete("Bitcoin");

        List<AssetRate> assetRates2 = assetRateDAOtest.getAll();
        assertThat(assetRates2.size()).isEqualTo(5);

        Asset asset = new Asset("Bitcoin", null);
        assetRateDAOtest.create(new AssetRate(asset, Timestamp.valueOf("2022-03-10 02:00:00"), BigDecimal.valueOf(1146.5)));
        assetRateDAOtest.create(new AssetRate(asset, Timestamp.valueOf("2022-03-09 02:00:00"), BigDecimal.valueOf(1116.5)));
        assetRateDAOtest.create(new AssetRate(asset, Timestamp.valueOf("2022-03-03 02:00:00"), BigDecimal.valueOf(1029.1)));
        assetRateDAOtest.create(new AssetRate(asset, Timestamp.valueOf("2022-02-10 02:00:00"), BigDecimal.valueOf(1133.8)));
        assetRateDAOtest.create(new AssetRate(asset, Timestamp.valueOf("2021-12-10 02:00:00"), BigDecimal.valueOf(1172.8)));

    }

    @Test
    void deleteAssetRateByTimestamp() {
        List<AssetRate> assetRates = assetRateDAOtest.getAll();
        assertThat(assetRates.size()).isEqualTo(10);
        assetRateDAOtest.deleteAssetRateByTimestamp("Bitcoin", Timestamp.valueOf("2022-03-10 02:00:00"));

        List<AssetRate> assetRates2 = assetRateDAOtest.getAll();
        assertThat(assetRates2.size()).isEqualTo(9);
    }
}