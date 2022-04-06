//ToDo fix broken references
//package nl.hva.miw.thepiratebank.service;
//
//
//import nl.hva.miw.thepiratebank.domain.Asset;
//import nl.hva.miw.thepiratebank.domain.AssetRate;
//import nl.hva.miw.thepiratebank.repository.AssetDAO;
//import nl.hva.miw.thepiratebank.repository.assetRate.AssetRateDAO;
//import nl.hva.miw.thepiratebank.repository.assetrate.AssetRateDAO;
//import nl.hva.miw.thepiratebank.service.persistence.AssetQueryDTO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.math.BigDecimal;
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//
//class AssetRateServiceTest {
//
//    private AssetRateService assetRateServiceUnderTest;
//    private AssetRateDAO assetRateDAOMock = Mockito.mock(AssetRateDAO.class);
//    private AssetDAO assetDAOMock = Mockito.mock(AssetDAO.class);
//    private final String selectedValuta = "EUR";
//    private final AssetQueryDTO expected = new AssetQueryDTO();
//    private final List<String> id = new ArrayList<>();
//    private static final Timestamp INDEX = Timestamp.valueOf(LocalDateTime.now());
//
//    public  AssetRateServiceTest(){
//      super();
//      this.assetRateServiceUnderTest = new AssetRateService(assetRateDAOMock);
//    }
//
//
//    @BeforeEach
//    void setup(){
//        expected.setValuta(selectedValuta);
//        String assetId = "bitcoin";
//        String assetId2 = "ethereum";
//        String assetId3 = "dogecoin";
//        id.add(assetId);
//        id.add(assetId2);
//        id.add(assetId3);
//        expected.coinsSet(id
//                .stream().map(e -> assetRateServiceUnderTest.getAssetHistoryPrice(e))
//                .collect(Collectors.toList()));
//
//        List<AssetRate> returnListNow = new ArrayList<>();
//
//        Asset testAsset = new Asset();
//        testAsset.setAbbreviation("BTC");
//        testAsset.setName("bitcoin");
//
//        AssetRate assetRate1now = new AssetRate();
//        AssetRate assetRate2now = new AssetRate();
//        AssetRate assetRate3now = new AssetRate();
//        assetRate1now.setAsset(testAsset);
//        assetRate2now.setAsset(testAsset);
//        assetRate3now.setAsset(testAsset);
//        //vroeg
//        assetRate1now.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusDays(0)));
//        //later
//        assetRate2now.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
//        //laatst
//        assetRate3now.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusDays(2)));
//        assetRate1now.setValue(BigDecimal.valueOf(11111));
//        assetRate2now.setValue(BigDecimal.valueOf(2));
//        assetRate3now.setValue(BigDecimal.valueOf(33));
//
//        returnListNow.add(assetRate1now);
//        returnListNow.add(assetRate2now);
//        returnListNow.add(assetRate3now);
//
//        Mockito.when(assetRateDAOMock.getClosest("bitcoin",Timestamp.valueOf(LocalDateTime.MAX))).thenReturn(returnListNow);
//
//    }
//
//
//    @Test
//    void getAssets() {
//        AssetQueryDTO actual = assetRateServiceUnderTest.getAssets(id);
//        assertThat(actual.getCoins().get(0).toString()).isEqualTo(expected.getCoins().get(0).toString());
//        assertThat(actual.getCoins().get(1).toString()).isEqualTo(expected.getCoins().get(1).toString());
//        assertThat(actual.getCoins().get(2).toString()).isEqualTo(expected.getCoins().get(2).toString());
//    }
//
////    @Test
////    void getAllAvailableAssets() {
////        AssetQueryDTO actual = assetRateServiceUnderTest.getAllAvailableAssets();
////        assertThat(actual.getCoins().size()).isEqualTo(20);
////        assertThat(actual.getCoins().get(0).getSymbol().equals("BTC"));
////        assertThat(actual.getCoins().get(3).getSymbol().equals("EOS"));
////        assertThat(actual.getCoins().get(19).getSymbol().equals("ALGO"));
////    }
//
//
//    @Test
//    void create() {
//        AssetRate assetRate = new AssetRate();
//        Asset testAsset = new Asset();
//        String assetId = "algorand";
//        testAsset.setName(assetId);
//        testAsset.setAbbreviation("ALGO");
//        assetRate.setAsset(testAsset);
//        assetRate.setValue(BigDecimal.valueOf(3.14159));
//        assetRate.setTimestamp(Timestamp.valueOf(LocalDateTime.MAX));
//        assetRateServiceUnderTest.create(assetRate);
//        id.add(assetId);
//        assertThat(assetRateServiceUnderTest.getAssets(id).getCoins().get(3).getSymbol()).isEqualTo("ALGO");
//    }
//
//
//    @Test
//    void getClosest1day() {
//        BigDecimal expected = new BigDecimal(11111);
//        BigDecimal actual = assetRateDAOMock.getClosest("bitcoin",Timestamp.valueOf(LocalDateTime.MAX))
//                .stream().findFirst().map(e->e.getValue()).orElse(BigDecimal.valueOf(0));
//        assertThat(actual).isEqualTo(expected);
//    }
//
//
//
//    @Test
//    void round2Decimal() {
//        BigDecimal expected = BigDecimal.valueOf(3.1416);
//        BigDecimal actual = assetRateServiceUnderTest.round4Decimal(BigDecimal.valueOf(3.1415923));
//        assertThat(actual).isEqualTo(expected);
//   }
//
//
//    @Test
//    void getValutaMultiplierMap() {
//
//        BigDecimal expected = BigDecimal.valueOf(1.1068);
//        BigDecimal actual = assetRateServiceUnderTest.getValutaMultiplier("USD");
//        assertThat(actual).isEqualTo(expected);
//    }
//
//
//
//
//}