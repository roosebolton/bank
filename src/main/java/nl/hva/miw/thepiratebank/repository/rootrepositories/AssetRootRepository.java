package nl.hva.miw.thepiratebank.repository.rootrepositories;

import nl.hva.miw.thepiratebank.domain.Asset;
import nl.hva.miw.thepiratebank.domain.AssetRate;
import nl.hva.miw.thepiratebank.repository.AssetDAO;
import nl.hva.miw.thepiratebank.repository.assetrate.AssetRateDAO;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AssetRootRepository {

  AssetDAO assetDAO;
  AssetRateDAO assetRateDAO;

    public AssetRootRepository(AssetDAO assetDAO, AssetRateDAO assetRateDAO) {
        this.assetDAO = assetDAO;
        this.assetRateDAO = assetRateDAO;
    }

    public Optional<Asset> getAssetByName(String assetname) {
        Asset asset = assetDAO.get(assetname);
        return Optional.ofNullable(asset);
    }


    public Optional<Asset> getAssetWithFullHistory (String assetname) {
        Asset asset = assetDAO.get(assetname);
        Optional<Asset> assetOptional = Optional.ofNullable(asset);
        List<AssetRate> assetRatelist = assetRateDAO.getFullHistory(assetname);
        assetOptional.ifPresent(element -> element.setPricehistory(assetRatelist));
        return assetOptional;
    }

    public Optional<Asset> findAssetOfTransaction(Integer transactionid) {
        Optional<Asset>  assetOptional = assetDAO.findAssetOfTransaction(transactionid);
        return assetOptional;

    }

    public List<Asset> getAllAssets() {
        return assetDAO.getAll();
    }

    public Optional<Asset> findAssetOfOrder(Integer orderId) {
        return assetDAO.findAssetOfOrder(orderId);
    }


}
