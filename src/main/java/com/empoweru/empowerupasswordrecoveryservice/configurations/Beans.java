package com.empoweru.empowerupasswordrecoveryservice.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Configuration
public class Beans {

    @Bean
    public ZonedDateTime zonedDateTime() {
        return ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
    }

}
