package com.bank.withdrawal.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

//Binds bank.withdrawal.* from application.yml into a typed, validated POJO.
//If any @NotBlank field is missing at startup, the application refuses to start.

@ConfigurationProperties(prefix = "bank.withdrawal") //
@Validated
public class WithdrawalProperties {

    @NotBlank(message = "bank.withdrawal.sns-topic-arn must be configured")
    private String snsTopicArn;

    @NotBlank(message = "bank.withdrawal.aws-region must be configured")
    private String awsRegion;

    public String getSnsTopicArn() { return snsTopicArn; }
    public void setSnsTopicArn(String value) { this.snsTopicArn = value; }

    public String getAwsRegion() { return awsRegion; }
    public void setAwsRegion(String value)   { this.awsRegion = value; }
}
