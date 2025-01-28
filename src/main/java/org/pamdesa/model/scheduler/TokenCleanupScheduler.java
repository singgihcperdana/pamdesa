package org.pamdesa.model.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pamdesa.service.ValidTokenService;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
@Slf4j
public class TokenCleanupScheduler {

    private final ValidTokenService validTokenService;

//    @Scheduled(fixedRate = 3600000) // Runs every hour
    @Scheduled(fixedRate = 60000) // Runs every minute
    public void cleanUpExpiredTokens() {
        log.info("cleanUpExpiredTokens");
        validTokenService.cleanUpExpiredTokens();
    }

}
