package nl.hva.miw.thepiratebank.repository;

import nl.hva.miw.thepiratebank.domain.Asset;
import nl.hva.miw.thepiratebank.domain.AssetRate;
import nl.hva.miw.thepiratebank.repository.rootrepositories.AssetRateRootRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AssetRateRootRepositoryTest {

    private CustomerDAO customerDAO;
    private UserDAO userDAO;
    private AssetDAO assetDAO;

    @Autowired
    private AssetRateRootRepository assetRateRootRepository;

    @Test
    void insertAssetByName() {
        Asset asset = new Asset("bitcoin", null);
        AssetRate assetRate = new AssetRate(asset, Timestamp.valueOf("2022-03-10 02:00:00"), BigDecimal.valueOf(20000));
        System.out.println(assetRate);
        AssetRate assetRate1 = assetRateRootRepository.insertAssetByName(assetRate);
        System.out.println(assetRate1);
    }
}