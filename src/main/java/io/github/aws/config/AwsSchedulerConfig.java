package io.github.aws.config;

import com.amazonaws.services.scheduler.AmazonScheduler;
import com.amazonaws.services.scheduler.AmazonSchedulerClientBuilder;
import com.amazonaws.services.scheduler.model.*;

/**
 * Permission policy for execution role
 * <code>
 * {
 *     "Version": "2012-10-17",
 *     "Statement": [
 *         {
 *             "Action": [
 *                 "sqs:SendMessage"</code>
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

    public void test(AmazonScheduler client) {
        String data = """
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
        CreateScheduleRequest request = new CreateScheduleRequest()
                .withName("test")
                .withFlexibleTimeWindow(new FlexibleTimeWindow()
                        .withMode(FlexibleTimeWindowMode.FLEXIBLE)
                        .withMaximumWindowInMinutes(15))
                .withScheduleExpression("at(2024-01-01T10:00:00)")
                .withTarget(new Target()
                        .withArn("")
                        .withRoleArn("")
                        .withInput(data)
                        .withSqsParameters(new SqsParameters()
                                .withMessageGroupId("")));


        client.createSchedule(request);
    }
}
