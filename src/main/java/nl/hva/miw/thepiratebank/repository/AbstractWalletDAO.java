package nl.hva.miw.thepiratebank.repository;

import com.fasterxml.jackson.core.PrettyPrinter;
import nl.hva.miw.thepiratebank.domain.Wallet;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public abstract class AbstractWalletDAO implements GenericDAO<Wallet, Integer> {

    public abstract void updateWalletAfterTransaction(int userId, String nameAsset, BigDecimal amount);

}
