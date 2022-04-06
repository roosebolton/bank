package nl.hva.miw.thepiratebank.config;


import nl.hva.miw.thepiratebank.repository.RootRepository;
import nl.hva.miw.thepiratebank.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * Class that provides config values for PirateBank
 */
@RestController
public final class AdminConfig {
    public static BigDecimal TRANSACTIONFEE;
    public static BigDecimal BANKSTARTINGCAPITAL;
    public static int BANKID;
    private AdminService adminService;

    @Autowired
    private AdminConfig(AdminService adminService) { // private constructor
        this.adminService = adminService;
        TRANSACTIONFEE = adminService.getTransactionFee();
        BANKSTARTINGCAPITAL = adminService.getBankStartingCaptial();
        BANKID = adminService.getBankId();
    }

    public static void setTRANSACTIONFEE(BigDecimal TRANSACTIONFEE) {
        AdminConfig.TRANSACTIONFEE = TRANSACTIONFEE;
    }

    public static void setBANKSTARTINGCAPITAL(BigDecimal BANKSTARTINGCAPITAL) {
        AdminConfig.BANKSTARTINGCAPITAL = BANKSTARTINGCAPITAL;
    }

    public static void setBANKID(int BANKID) {
        AdminConfig.BANKID = BANKID;
    }
}
