package nl.hva.miw.thepiratebank.config;

import java.util.HashMap;
import java.util.Map;

public class AssetList {
    public final static Map<String,String> AVAILABLE_ASSET_COINS_MAP = new HashMap<>() {{
        put("bitcoin","BTC");
        put("ethereum","ETH");
        put("ripple","XRP");
        put("eos","EOS");
        put("cardano","ADA");
        put("solana","SOL");
        put("avalanche-2","AVAX");
        put("polkadot","DOT");
        put("dogecoin","DOGE");
        put("monero","XMR");
        put("matic-network","MATIC");
        put("crypto-com-chain","CRO");
        put("cosmos","ATOM");
        put("litecoin","LTC");
        put("near","NEAR");
        put("chainlink","LINK");
        put("uniswap","UNI");
        put("tron","TRX");
        put("ftx-token","FTT");
        put("algorand","ALGO");
    }};
}
