package nl.hva.miw.thepiratebank.repository;

import nl.hva.miw.thepiratebank.controller.AssetConsumerController;
import nl.hva.miw.thepiratebank.domain.Asset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AssetDAOTest {

    private AssetDAO assetDaotest;

    //Necessary to prevent postconstruct annotation from running do not delete
    @MockBean
    private AssetConsumerController assetConsumerController;

    @Autowired
    public AssetDAOTest(AssetDAO assetDaotest) {
        super();
        this.assetDaotest = assetDaotest;
    }

    @Test
    void getAll() {
        List<Asset> assets = assetDaotest.getAll();
        assertThat(assets).isNotNull().isNotEmpty();
        assertThat(assets.size()).isEqualTo(3);
    }

    @Test
    void get() {
        Asset actual = assetDaotest.get("Ethereum");
        Asset expected = new Asset("Ethereum", "ETH");
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void create() {
        List<Asset> assets = assetDaotest.getAll();
        assertThat(assets.size()).isEqualTo(3);
        Asset expected = new Asset("Binance", "BNB");
        assetDaotest.create(expected);
        List<Asset> assets2 = assetDaotest.getAll();
        assertThat(assets2.size()).isEqualTo(4);
        Asset actual = assetDaotest.get("Binance");
        assertThat(actual).isNotNull().isEqualTo(expected);
        assetDaotest.delete("Binance");
        assertThat(assets.size()).isEqualTo(3);
    }

    @Test
    void update() {
        Asset expected = new Asset("Bitcoin", "BIT");
        assetDaotest.update(expected);
        Asset actual = assetDaotest.get("Bitcoin");
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void delete() {
        List<Asset> assets = assetDaotest.getAll();
        assertThat(assets.size()).isEqualTo(3);

        assetDaotest.delete("Bitcoin");

        List<Asset> assets2 = assetDaotest.getAll();
        assertThat(assets2.size()).isEqualTo(2);

        assetDaotest.create(new Asset("Bitcoin", "BIT"));
        List<Asset> assetsNew = assetDaotest.getAll();
        assertThat(assetsNew.size()).isEqualTo(3);
    }
}