package example;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.io.*;
import java.nio.file.*;

public class HandlerIntegerJava17 implements RequestHandler<Object, String> {
    private static final AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();

    @Override
    public String handleRequest(Object input, Context context) {
        String bucketName = "lambda-artifacts-a2fc6d0a966ac021";
        String fileKey = "sample/250mb.txt";
        String tmpFilePath = "/tmp/downloaded_file.txt";
        LambdaLogger logger=context.getLogger();
        try {
            downloadFile(bucketName, fileKey, tmpFilePath,logger);
            uploadFile(bucketName, fileKey+".bk", tmpFilePath,logger);

            return "done";
        } catch (IOException e) {
            logger.log("Error downloading file");
            return "Error downloading file: " + e.getMessage();
        }
    }

    private String downloadFile(String bucketName, String fileKey, String tmpFilePath,LambdaLogger logger) throws IOException {
        long startTime = System.currentTimeMillis();
        try (S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, fileKey));
        

            InputStream objectData = s3Object.getObjectContent();
            OutputStream outputStream = Files.newOutputStream(Paths.get(tmpFilePath))) {
            
            objectData.transferTo(outputStream);      
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;      
            logger.log("File downloaded successfully to " + tmpFilePath + " in " + duration + "  ms");
            return "File downloaded successfully to " + tmpFilePath;
        }
    }

    //add a function to upload file from local to s3 bucket
    private void uploadFile(String bucketName, String fileKey, String filePath, LambdaLogger logger) throws IOException {
        long startTime = System.currentTimeMillis();
        File file = new File(filePath);
        try {
            s3Client.putObject(bucketName, fileKey, file);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            logger.log("File uploaded successfully to " + bucketName + "/"+ fileKey+ " in " + duration + "  ms");
        } catch (Exception e) {
            logger.log("Error uploading file to S3: " + e.getMessage());
            throw e;
        }
    }
}
