package nl.hva.miw.thepiratebank.service;

import org.springframework.stereotype.Component;

@Component
public class PepperService {
    private static final String PEPPER = "WalkThePlankMaggot";
    public String getPepper() {
        return PEPPER;
    }
}
