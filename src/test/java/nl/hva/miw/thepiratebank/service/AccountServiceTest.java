//ToDo fix unresolved methods
//
//package nl.hva.miw.thepiratebank.service;
//
//import nl.hva.miw.thepiratebank.domain.*;
//import nl.hva.miw.thepiratebank.domain.customerattributes.Address;
//import nl.hva.miw.thepiratebank.domain.customerattributes.IdentifyingInformation;
//import nl.hva.miw.thepiratebank.domain.customerattributes.PersonalDetails;
//import nl.hva.miw.thepiratebank.domain.transfer.TransactionDTO;
//import nl.hva.miw.thepiratebank.repository.AccountDAO;
//import nl.hva.miw.thepiratebank.repository.RootRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.mockito.Mockito;
//
//import java.math.BigDecimal;
//import java.sql.Timestamp;
//import java.time.LocalDate;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class AccountServiceTest {
//
//    private AccountService accountServiceTest;
//    private TransactionService transactionService;
//
//    private RootRepository rootRepositoryMock = Mockito.mock(RootRepository.class);
//    private AccountDAO accountDAOMock = Mockito.mock(AccountDAO.class);
//
//    public AccountServiceTest() {
//        this.accountServiceTest = new AccountService(rootRepositoryMock);
//    }
//
//    @BeforeEach
//    public void fillMocks(){
//        Customer bank = createBank();
//        Customer customer = createCustomer();
//
//        Account bankAccount = new Account(bank, new BigDecimal(1000000));
//        Account customerAccount = new Account(customer, new BigDecimal(80000));
//
//        Mockito.when(rootRepositoryMock.getAccountById(1000)).thenReturn(bankAccount);
//        Mockito.when(rootRepositoryMock.getAccountById(2)).thenReturn(customerAccount);
////        Mockito.when(rootRepositoryMock.updateAccount(any(Account.class))).thenCallRealMethod();
//
//    }
//
//    @Test
//    void getAccountById() {
//        Customer customer = createCustomer();
//        Account expected = new Account(customer, new BigDecimal(80000));
//        Account actual = accountServiceTest.getAccountById(2);
//        assertThat(actual).isNotNull().isEqualTo(expected);
//    }
//
//    @Test
//    void doDebitBalance() {
//        // 1. Een customer en bank maken
//        Customer bank = createBank();
//        Customer customer = createCustomer();
//        // 2. Een transactie maken;
//        Transaction buyTransaction = createTransaction1(customer, bank);
//        // 3. de methode die getest moet worden, maar nu niet getest wordt
//        // Het account moet hier in principe geupdate worden met een nieuwe balance
//        accountServiceTest.doDebitBalance(buyTransaction);
//        // 4. De expected objecten definieeren
//        Account expected1 =  new Account(customer, new BigDecimal(47750));
//        Account expected2 = new Account(bank, new BigDecimal(972250));
//
//        // 5. opvragen van de accounten die als het goed is geupdate zijn
//        // Geeft nu de eerder gemaakte accounten terug
//        // De update methode wordt mogelijk geblokt. Hoe dit te mocken?
//        Account actual1 = accountServiceTest.getAccountById(2);
//        Account actual2 = accountServiceTest.getAccountById(1000);
//
//        //6. vergelijken
//        assertThat(actual1).isNotNull().isEqualTo(expected1);
//        assertThat(actual2).isNotNull().isEqualTo(expected2);
//    }
//
//    @Test
//    void doCreditBalance() {
//        Customer bank = createBank();
//        Customer customer = createCustomer();
//        Transaction sellTransaction = createTransaction1(customer, bank);
//
//        accountServiceTest.doDebitBalance(sellTransaction);
//
//        Account expected1 =  new Account(customer, new BigDecimal(107750));
//        Account expected2 = new Account(bank, new BigDecimal(1032250));
//        Account actual1 = accountServiceTest.getAccountById(2);
//        Account actual2 = accountServiceTest.getAccountById(1000);
//
//        assertThat(actual1).isNotNull().isEqualTo(expected1);
//        assertThat(actual2).isNotNull().isEqualTo(expected2);
//
//    }
//
//    @Test
//    void doDebitBuyerBalance() {
//        BigDecimal expected = new BigDecimal(47750);
//        BigDecimal actual = accountServiceTest.doDebitBuyerBalance(new BigDecimal(80000), new BigDecimal(30000),
//                new BigDecimal(2250));
//        assertThat(actual).isNotNull().isEqualTo(expected);
//    }
//
//    @Test
//    void doDebitBankBalance() {
//        BigDecimal expected = new BigDecimal(972250);
//        BigDecimal actual = accountServiceTest.doDebitBankBalance(new BigDecimal(1000000), new BigDecimal(30000),
//                new BigDecimal(2250));
//        assertThat(actual).isNotNull().isEqualTo(expected);
//    }
//
//    @Test
//    void doCreditSellerBalance() {
//        BigDecimal expected = new BigDecimal(107750);
//        BigDecimal actual = accountServiceTest.doCreditSellerBalance(new BigDecimal(80000), new BigDecimal(30000),
//                new BigDecimal(2250));
//        assertThat(actual).isNotNull().isEqualTo(expected);
//    }
//
//    @Test
//    void doCreditBankBalance() {
//        BigDecimal expected = new BigDecimal(1032250);
//        BigDecimal actual = accountServiceTest.doCreditBankBalance(new BigDecimal(1000000), new BigDecimal(30000),
//                new BigDecimal(2250));
//        assertThat(actual).isNotNull().isEqualTo(expected);
//    }
//
//    public Transaction createTransaction1(Customer customer, Customer bank){
//        Transaction transaction1 = new Transaction();
//        transaction1.setBuyer(customer);
//        transaction1.setSeller(bank);
//        transaction1.setAsset(new Asset("bitcoin", "BTC"));
//        transaction1.setAssetAmount(new BigDecimal(1));
//        transaction1.setValue(new BigDecimal(30000));
//        transaction1.setTransactionCost(new BigDecimal(2250));
//        return transaction1;
//    }
//
//    public Customer createCustomer(){
//        LocalDate birthday2 = LocalDate.of(Integer.parseInt("1990"), Integer.parseInt("02"), Integer.parseInt("02"));
//        User user = new User("danielle@gmail.com", "Welkom01");
//        Customer customer = new Customer(2, user.getUserName(), user.getPassword(),
//                new PersonalDetails("Danielle", null, "Test"),
//                new Address("Straatnaam", "100", null, "1111AB", "Stad"),
//                new IdentifyingInformation(888888888, "NL20INGB0001234577", birthday2));
//        return customer;
//    }
//
//    public Customer createBank(){
//        LocalDate birthday1 = LocalDate.of(Integer.parseInt("1990"), Integer.parseInt("01"), Integer.parseInt("01"));
//        User bankUser = new User("thePirateBank@bank.nl", "WalkThePlank");
//        Customer bank = new Customer(1000, bankUser.getUserName(), bankUser.getPassword() ,
//                new PersonalDetails("Pirate", null, "Bank"),
//                new Address("Wallstreet", "200", null, "2222CD", "Amsterdam"),
//                new IdentifyingInformation(123456789, "NL20INGB0001234567", birthday1));
//        return bank;
//    }
//
//}