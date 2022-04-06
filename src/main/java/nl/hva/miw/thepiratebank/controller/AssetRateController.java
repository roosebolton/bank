package nl.hva.miw.thepiratebank.controller;

import nl.hva.miw.thepiratebank.service.AssetRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AssetRateController {
    private AssetRateService assetRateService;

    @Autowired
    public AssetRateController(AssetRateService assetRateService) {
        super();
        this.assetRateService = assetRateService;
    }

    @GetMapping("/coins")
    @ResponseBody
    public ResponseEntity<?> getAssets(@RequestParam(defaultValue = "EUR") String valuta,
                                       @RequestParam(defaultValue = "all") List<String> id,
                                       @RequestParam(defaultValue = "delta") String type) {
        return ResponseEntity.ok().body(assetRateService.getInterval(valuta,id,type));
    }

    @GetMapping("/coins/{id}")
    @ResponseBody
    public ResponseEntity<?> getFullHistory(@PathVariable String id, @RequestParam(defaultValue = "EUR") String valuta) {
        return ResponseEntity.ok().body(assetRateService.getAssetHistory(valuta,id));
    }

    @GetMapping("/coins/list")
    @ResponseBody
    public ResponseEntity<?> getOverviewCoins() {
        return ResponseEntity.ok().body(assetRateService.getAvailableAssetIDs());
    }

    @GetMapping("/coins/fulllist")
    @ResponseBody
    public ResponseEntity<?> getFullOverviewCoins() {
        return ResponseEntity.ok().body(assetRateService.getAvailableAssets());

    }
}



