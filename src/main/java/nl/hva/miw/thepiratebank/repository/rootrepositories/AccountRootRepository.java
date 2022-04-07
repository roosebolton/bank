package nl.hva.miw.thepiratebank.repository.rootrepositories;

import nl.hva.miw.thepiratebank.domain.Account;
import nl.hva.miw.thepiratebank.repository.AccountDAO;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRootRepository {

    AccountDAO accountDAO;

    public AccountRootRepository(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    //Account
    public Account getAccountById(int id){
        return accountDAO.get(id);
    }

    public void updateAccount(Account account) {
        accountDAO.update(account);
    }

    public void createAccount(Account acount){accountDAO.create(acount);}


}
