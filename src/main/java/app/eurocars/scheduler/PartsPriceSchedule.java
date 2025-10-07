package app.eurocars.scheduler;

import app.eurocars.part.service.PartService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PartsPriceSchedule {

    private final PartService partService;

    public PartsPriceSchedule(PartService partService) {
        this.partService = partService;
    }

    //Scheduled for every 20th date of the month at 12 PM
    @Scheduled(cron = "0 0 12 20 * ?")
    public void updatePrices() {
        partService.inflatePrices();
    }
}
