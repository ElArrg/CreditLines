package com.elarrg.credit.throttle.interceptor;

import com.elarrg.credit.model.util.Customer;
import com.elarrg.credit.repository.ICustomerRepository;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Component
public class ThrottleInterceptor implements HandlerInterceptor {

    private static final String HEADER_LIMIT_REMAINING = "X-Rate-Limit-Remaining";
    private static final String HEADER_RETRY_AFTER = "X-Rate-Limit-Retry-After-Seconds";

    private final ICustomerRepository customerRepository;

    public ThrottleInterceptor(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ipAddress = request.getRemoteAddr();

        if (ipAddress.isBlank()) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid IP address");
            return false;
        }

        Customer currentCustomer = customerRepository
                .findByIp(ipAddress)
                .orElse(customerRepository.save(ipAddress));

        request.setAttribute("customer", currentCustomer);

        Bucket tokenBucket = currentCustomer.getBucket();
        ConsumptionProbe probe = tokenBucket.tryConsumeAndReturnRemaining(1L);
        boolean isConsumable = probe.isConsumed();

        if (isConsumable) {
            response.addHeader(HEADER_LIMIT_REMAINING, String.valueOf(probe.getRemainingTokens()));

        } else {
            long waitTillRefill = TimeUnit.NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill());

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.addHeader(HEADER_RETRY_AFTER, String.valueOf(waitTillRefill));
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),
                    String.format("Too many requests, please wait %d seconds to try again", waitTillRefill));
        }

        return isConsumable;
    }
}
