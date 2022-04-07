package nl.hva.miw.thepiratebank.repository.rootrepositories;

import nl.hva.miw.thepiratebank.repository.ConfigDataDAO;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class ConfigDataRootRepository {

    ConfigDataDAO configDataDAO;

    public ConfigDataRootRepository(ConfigDataDAO configDataDAO) {
        this.configDataDAO = configDataDAO;
    }


    //ConfigData
    public BigDecimal getTransactionFee(){
        return configDataDAO.getTransActionFee();
    }

    public void setTransactionFee(BigDecimal newTransactionFee){
        configDataDAO.setTransactionFee(newTransactionFee);
    }

    public BigDecimal getBankStartingCapital(){
        return configDataDAO.getBankStartingCapital();
    }

    public void setBankStartingCapital(BigDecimal newBankStartingCapital){
        configDataDAO.setBankStartingCapital(newBankStartingCapital);
    }

    public int getBankId(){
        return configDataDAO.getBankId();
    }

    public void setBankId(int newBankId){
        configDataDAO.setBankId(newBankId);
    }


}
