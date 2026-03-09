package com.bank.withdrawal.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;


 //Creates the SnsClient bean, this will only be active when running with --spring.profiles.active=aw, AWS credentials are needed
 //on the default local profile this com.bank.withdrawal.config class is skipped entirely,
 //and LocalEventPublisher is used instead since i have no AWS credentials

@Configuration
@Profile("aws")   //only loaded when the 'aws' Spring profile is active
@EnableConfigurationProperties(WithdrawalProperties.class)
public class SnsConfig {

    @Bean
    public SnsClient snsClient(WithdrawalProperties props) {
        return SnsClient.builder()
                .region(Region.of(props.getAwsRegion()))
                .build();
    }
}
