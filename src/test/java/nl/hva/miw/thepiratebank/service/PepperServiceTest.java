package nl.hva.miw.thepiratebank.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;
class PepperServiceTest {

    PepperService serviceUnderTest;

    public PepperServiceTest(){
      this.serviceUnderTest =  new PepperService();
    }

    @Test
    void getPepper() {
        String expected = "WalkThePlankMaggot";
        assertThat(serviceUnderTest.getPepper()).isNotNull().isNotEmpty().isEqualTo(expected);
    }

}