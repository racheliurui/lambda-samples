package example;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;



import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


// Handler value: example.HandlerInteger
public class HandlerIntegerJava17 implements RequestHandler<Object, String> {
    private static final Logger logger = LogManager.getLogger(HandlerIntegerJava17.class);

    @Override
    public String handleRequest(Object input, Context context) {
        LocalDateTime currentTime = LocalDateTime.now();
        String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("Current time using system out println: " + formattedTime);
        context.getLogger().log("Current Date and Time  using lambda log: " + formattedTime);
        //ERROR StatusLogger Log4j2 could not find a logging implementation. Please add log4j-core to the classpath. Using SimpleLogger to log to the console...
        logger.info("Current time using log4j: " + formattedTime);
        return "Lambda function executed successfully.";
    }
}
    


