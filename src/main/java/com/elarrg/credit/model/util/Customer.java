package com.elarrg.credit.model.util;

import com.elarrg.credit.model.api.CreditLineStatus;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;

import java.time.Duration;

public class Customer {
    public static final int APPLICATION_FAILURE_LIMIT = 3;

    private static final Long ACCEPTED_APPLICATIONS_LIMIT = 2L;
    private static final Long ACCEPTED_APPLICATIONS_PER_SECONDS_PERIOD = 120L;
    private static final Long REJECTED_APPLICATIONS_LIMIT = 1L;
    private static final Long REJECTED_APPLICATIONS_PER_SECONDS_PERIOD = 30L;

    private final String ipAddress;
    private Bucket bucket;
    private CreditLineStatus creditLineStatus;
    private int failureCount;

    public Customer(String ipAddress) {
        this.ipAddress = ipAddress;
        setApplicationsLimit(REJECTED_APPLICATIONS_LIMIT, REJECTED_APPLICATIONS_PER_SECONDS_PERIOD);
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Bucket getBucket() {
        return bucket;
    }

    public void setDefinitiveStatus(CreditLineStatus creditLineStatus) {
        if (this.creditLineStatus == null || CreditLineStatus.ACCEPTED.equals(creditLineStatus)) {
            setApplicationsLimit(ACCEPTED_APPLICATIONS_LIMIT, ACCEPTED_APPLICATIONS_PER_SECONDS_PERIOD);
        }

        this.creditLineStatus = creditLineStatus;
    }

    public int incrementFailureCount() {
        return ++failureCount;
    }

    private void setApplicationsLimit(long requestsLimit, long requestsSecondsLimit) {
        this.bucket = Bucket4j.builder()
                .addLimit(Bandwidth.simple(requestsLimit, Duration.ofSeconds(requestsSecondsLimit)))
                .build();
    }

}
