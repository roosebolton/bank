package nl.hva.miw.thepiratebank.repository.assetrate;

import nl.hva.miw.thepiratebank.domain.AssetRate;
import nl.hva.miw.thepiratebank.repository.GenericDAO;

import java.sql.Timestamp;
import java.util.List;

public abstract class AssetRateAbstractDAO implements GenericDAO<AssetRate,String> {

    abstract void deleteAssetRateByTimestamp(String asset_name, Timestamp timestamp);
    abstract List<AssetRate> getClosest(String asset_name, Timestamp timestamp);
    abstract AssetRate getAssetRateByTimestamp(String asset_name,Timestamp timestamp);
    abstract List<AssetRate> getFullHistory (String asset_name);
}
