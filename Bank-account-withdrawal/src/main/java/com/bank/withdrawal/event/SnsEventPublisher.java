package com.bank.withdrawal.event;

import com.bank.withdrawal.config.WithdrawalProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;


//Production implementation of EventPublisher , this would publish to AWS SNS as in the original sample code provided.
//this will be only active when running with --spring.profiles.active=aws.
//Event failure is caught and logged rather than re-thrown because by the time
//this runs, the withdrawal has already been committed to the database.
//Re-throwing would not undo the deduction, i think it would only confuse the caller, could be argued i guess

@Component
@Profile("aws")
public class SnsEventPublisher implements EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(SnsEventPublisher.class);

    private final SnsClient snsClient;
    private final WithdrawalProperties props;
    private final ObjectMapper objectMapper;

    public SnsEventPublisher(SnsClient snsClient, WithdrawalProperties props, ObjectMapper objectMapper) {
        this.snsClient = snsClient;
        this.props = props;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishWithdrawalEvent(WithdrawalEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);

            PublishRequest request = PublishRequest.builder()
                    .topicArn(props.getSnsTopicArn())
                    .message(payload)
                    .build();

            snsClient.publish(request);

            log.info("Published withdrawal event to SNS eventId={} accountId={}", event.getEventId(), event.getAccountId());

        } catch (Exception ex) {
            // Log and move on — the DB transaction is already committed.

            log.error("Failed to publish withdrawal event to SNS eventId={}: {}", event.getEventId(), ex.getMessage(), ex);
        }
    }
}
