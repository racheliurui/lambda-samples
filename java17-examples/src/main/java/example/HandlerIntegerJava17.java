package example;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

// required when using vault
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;


// Handler value: example.HandlerInteger
public class HandlerIntegerJava17 implements RequestHandler<SQSEvent, Void> {
    @Override
    public Void handleRequest(SQSEvent sqsEvent, Context context) {

        // get env varaible called LAMBDA_SECRET and print it out
        String lambdaSecret = System.getenv("LAMBDA_SECRET");
        context.getLogger().log("read secret from env LAMBDA_SECRET:  " + lambdaSecret);


        // read secret by passing secret manager item and print it out
        String secretArn = System.getenv("LAMBDA_SECRET_ARN");
        String secretFromSecretManager = getsecret(secretArn);
        context.getLogger().log("read secret from reading from secret manager:  " + secretFromSecretManager);

        for (SQSMessage msg : sqsEvent.getRecords()) {
            processMessage(msg, context);
        }
        context.getLogger().log("done");
        return null;
    }

    private void processMessage(SQSMessage msg, Context context) {
        try {

            String msgBody = msg.getBody();
            context.getLogger().log("Processed message " + msgBody);

            // TODO: Do interesting work based on the new message

        } catch (Exception e) {
            context.getLogger().log("An error occurred");
            throw e;
        }

    }

    public String getsecret(String secretArn) {
        AWSSecretsManager client = AWSSecretsManagerClientBuilder.defaultClient();

        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                .withSecretId(secretArn);
        GetSecretValueResult getSecretValueResult = null;

        try {
            getSecretValueResult = client.getSecretValue(getSecretValueRequest);
        } catch (Exception e) {
            throw e;
        }

        String secret = getSecretValueResult.getSecretString();
        return secret;
    }


}