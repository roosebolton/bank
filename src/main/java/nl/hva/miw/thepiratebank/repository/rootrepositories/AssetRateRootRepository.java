package nl.hva.miw.thepiratebank.repository.rootrepositories;

import nl.hva.miw.thepiratebank.domain.Asset;
import nl.hva.miw.thepiratebank.domain.AssetRate;
import nl.hva.miw.thepiratebank.repository.AssetDAO;
import org.springframework.stereotype.Repository;

@Repository
public class AssetRateRootRepository {

    AssetDAO assetDAO;

    public AssetRateRootRepository(AssetDAO assetDAO) {
        this.assetDAO = assetDAO;
    }

    //Assetrate
    public AssetRate insertAssetByName (AssetRate assetRate) {
        Asset asset = assetDAO.get(assetRate.getAsset().getName());
        assetRate.setAsset(asset);
        return assetRate;
    }


}
