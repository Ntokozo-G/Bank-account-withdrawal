package com.bank.withdrawal.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


//this is the local implementation of EventPublisher, this is used when running without the 'aws' profile,
//this is what i'm currently running since i do not have an aws account for SNS.
//So instead of calling SNS it simply logs the event as JSON.
//this means that we can run and test the full withdrawal flow locally without needing SNS and
//with zero AWS credentials or configuration.
//the @Profile("!aws") means: "activate this bean on any profile EXCEPT aws"
//not part of the scope but helps with the testing and seeing how the whole flow works

@Component
@Profile("!aws")
public class LocalEventPublisher implements EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(LocalEventPublisher.class);

    private final ObjectMapper objectMapper;

    public LocalEventPublisher(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishWithdrawalEvent(WithdrawalEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            log.info("[LOCAL] Withdrawal event (would publish to SNS in production or the original code that was sent or sampled ): {}", json);
        } catch (Exception ex) {
            log.error("[LOCAL] Failed to serialise withdrawal event", ex);
        }
    }
}
