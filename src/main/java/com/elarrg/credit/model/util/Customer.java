package com.elarrg.credit.model.util;

import com.elarrg.credit.model.api.CreditLineStatus;
import io.github.bucket4j.*;

import java.time.Duration;

public class Customer {
    public static final int APPLICATION_FAILURE_LIMIT = 3;

    private static final Long ACCEPTED_APPLICATIONS_LIMIT = 2L;
    private static final Long ACCEPTED_APPLICATIONS_PER_MINUTES_PERIOD = 2L;

    private static final Long REJECTED_APPLICATIONS_LIMIT = 1L;
    private static final Long REJECTED_APPLICATIONS_PER_SECONDS_PERIOD = 30L;

    private final String ipAddress;
    private Bucket bucket;
    private CreditLineStatus creditLineStatus;
    private int failureCount;

    public Customer(String ipAddress) {
        this.ipAddress = ipAddress;
        setApplicationsLimit(
                Bandwidth.simple(
                        REJECTED_APPLICATIONS_LIMIT,
                        Duration.ofSeconds(REJECTED_APPLICATIONS_PER_SECONDS_PERIOD)));
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Bucket getBucket() {
        return bucket;
    }

    public void setDefinitiveStatus(CreditLineStatus creditLineStatus) {
        if (this.creditLineStatus == null || CreditLineStatus.ACCEPTED.equals(creditLineStatus)) {
            setApplicationsLimit(
                    Bandwidth.classic(
                                    ACCEPTED_APPLICATIONS_LIMIT,
                                    Refill.intervally(
                                            ACCEPTED_APPLICATIONS_LIMIT,
                                            Duration.ofMinutes(ACCEPTED_APPLICATIONS_PER_MINUTES_PERIOD)))
                            // Since configuration replacement occurs after an application status change,
                            // it will be subtracted 1 to the initial limit to handle that trigger event.
                            .withInitialTokens(ACCEPTED_APPLICATIONS_LIMIT - 1L)
            );
        }

        this.creditLineStatus = creditLineStatus;
    }

    public int incrementFailureCount() {
        return ++failureCount;
    }

    private void setApplicationsLimit(Bandwidth limit) {

        if (bucket == null) {
            this.bucket = Bucket4j.builder()
                    .addLimit(limit)
                    .build();
        } else {
            this.bucket.replaceConfiguration(
                    Bucket4j.configurationBuilder()
                            .addLimit(limit)
                            .build(),
                    TokensInheritanceStrategy.RESET);
        }
    }

}
