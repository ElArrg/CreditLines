package com.elarrg.credit;

import com.elarrg.credit.throttle.interceptor.ThrottleInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(scanBasePackages = "com.elarrg.credit")
public class CreditLinerApplication implements WebMvcConfigurer {

    @Autowired
    @Lazy
    private ThrottleInterceptor throttleInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(throttleInterceptor)
                .addPathPatterns("/credits/applications");
    }

    public static void main(String[] args) {
        SpringApplication.run(CreditLinerApplication.class, args);
    }

}
