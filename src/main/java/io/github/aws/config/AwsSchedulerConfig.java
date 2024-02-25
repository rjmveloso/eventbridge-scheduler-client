package io.github.aws.config;

import com.amazonaws.services.scheduler.AmazonScheduler;
import com.amazonaws.services.scheduler.AmazonSchedulerClientBuilder;
import com.amazonaws.services.scheduler.model.CreateScheduleRequest;
import com.amazonaws.services.scheduler.model.FlexibleTimeWindow;
import com.amazonaws.services.scheduler.model.FlexibleTimeWindowMode;
import com.amazonaws.services.scheduler.model.Target;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Permission policy for execution role
 * <code>
 * {
 *     "Version": "2012-10-17",
 *     "Statement": [
 *         {
 *             "Action": [
 *                 "sqs:SendMessage"
 *             ],
 *            "Effect": "Allow",
 *            "Resource": "*"
 *        }
 *    ]
 * }
 * </code>
 */
public class AwsSchedulerConfig {

    public AmazonScheduler schedulerClient() {
        return AmazonSchedulerClientBuilder.defaultClient();
    }

    public static void main(String[] args) {
        var data = """
                {
                  "QueryUrl": "https://sqs.eu-central-1.amazonaws.com/<ACCOUNT_ID>/<queue_name>",
                  "MessageBody": "<payload>",
                  "MessageAttributes": {
                     "bypass_status_check": {
                       "DataType": "String",
                       "StringValue": "true"
                     }
                  }
                }
                """;

        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        var timestamp = Instant.now().plus(24, ChronoUnit.HOURS);
        var expression = "at(" + formatter.format(timestamp) + ")";

        var request = new CreateScheduleRequest()
                .withName("test")
                .withFlexibleTimeWindow(new FlexibleTimeWindow()
                        .withMode(FlexibleTimeWindowMode.FLEXIBLE)
                        .withMaximumWindowInMinutes(15))
                .withScheduleExpression(expression)
                .withTarget(new Target()
                        .withArn("")
                        .withRoleArn("")
                        .withInput(data));
        // SQS templated target do not allow message attributes
        // for that universal target must be used.
                        //.withSqsParameters(new SqsParameters()
                        //        .withMessageGroupId("")));

        var client = new AwsSchedulerConfig().schedulerClient();
        var result = client.createSchedule(request);
        System.out.println(result);
    }
}
