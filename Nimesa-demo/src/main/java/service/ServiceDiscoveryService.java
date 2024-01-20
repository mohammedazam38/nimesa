package service;


import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.CloseableThreadContext.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.sun.jdi.connect.spi.Connection;

import Respository.ServiceResultRepository;
import model.ServiceResult;
import software.amazon.awssdk.regions.Region;

@Service
public class ServiceDiscoveryService {

 @Autowired
 private ServiceResultRepository serviceResultRepository;

 @Async
 public CompletableFuture<String> discoverEC2() {
	   Region region = Region.AP_SOUTH_1;
	   Ec2AsyncClient ec2AsyncClient = Ec2AsyncClient.builder()
               .region(region)
               .build();

       // Create a DescribeInstancesRequest
       DescribeInstancesRequest describeInstancesRequest = DescribeInstancesRequest.builder().build();

       // Asynchronously send the request and process the response using CompletableFuture
       CompletableFuture<DescribeInstancesResponse> futureResponse = ec2AsyncClient.describeInstances(describeInstancesRequest);

       // Attach a callback function to handle the completion of the request
       futureResponse.whenComplete((response, exception) -> {
           if (exception != null) {
               // Handle the exception
               System.err.println("Error discovering EC2 instances: " + exception.getMessage());
           } else {
               // Process the response and print instance details
               List<Instance> instances = response.reservations().stream()
                       .flatMap(reservation -> reservation.instances().stream())
                       .toList();

               System.out.println("Discovered EC2 instances:");
               for (Instance instance : instances) {
                   System.out.println("Instance ID: " + instance.instanceId());
                   System.out.println("Instance Type: " + instance.instanceType());
                   System.out.println("State: " + instance.state().name());
                   System.out.println("-------------------------------------------");
               }
           }

          
           ec2AsyncClient.close();
       });

       try {
          
           futureResponse.get();
       } catch (InterruptedException | ExecutionException e) {
           e.printStackTrace();
       }
     String ec2Result = "List of EC2 instances in Mumbai Region";
     String jobId = saveResult("EC2", ec2Result);
     return CompletableFuture.completedFuture(jobId);
 }

 @Async
 public CompletableFuture<String> discoverS3() {
     // Simulate S3 discovery logic (replace with actual logic)
     String s3Result = "List of S3 buckets";
     String jobId = saveResult("S3", s3Result);
     return CompletableFuture.completedFuture(jobId);
 }

 public String getJobResult(String jobId) {
     List<ServiceResult> results = serviceResultRepository.findByJobId(jobId);
     return results.isEmpty() ? "Job not found" : results.get(0).getResult();
 }

 public List<ServiceResult> getDiscoveryResult(String service) {
     return serviceResultRepository.findByService(service);
 }

 @Async
 public CompletableFuture<String> getS3BucketObjects(String bucketName) {
     
     String objectsResult = "List of S3 bucket objects";
     String jobId = saveResult("S3 Objects", objectsResult);
     return CompletableFuture.completedFuture(jobId);
 }

 public int getS3BucketObjectCount(String bucketName) {
	  S3AsyncClient s3AsyncClient = S3AsyncClient.create();

     
      try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
          String insertSql = "INSERT INTO file_table (file_name) VALUES (?)";
          try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {

              ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder()
                      .bucket(bucketName)
                      .build();

              CompletableFuture<ListObjectsV2Response> futureResponse = s3AsyncClient.listObjectsV2(listObjectsRequest);
              futureResponse.thenAcceptAsync(response -> {
                  ListObjectsV2Publisher publisher = ListObjectsV2Publisher.create(s3AsyncClient, listObjectsRequest);
                  publisher.items().forEach(s3Object -> {
                      String fileName = s3Object.key();
                      
                      persistFileNameInDB(fileName, preparedStatement);
                  });
              }).join();

          } catch (SQLException e) {
              e.printStackTrace();
          }

      } catch (SQLException e) {
          e.printStackTrace();
      }

   
      s3AsyncClient.close();
     List<ServiceResult> results = serviceResultRepository.findByServiceAndResultLike("S3 Objects", "%" + bucketName + "%");
     return results.size();
 }

 public List<ServiceResult> getS3BucketObjectLike(String bucketName, String pattern) {
     return serviceResultRepository.findByServiceAndResultLike("S3 Objects", "%" + pattern + "%");
 }

 private String saveResult(String service, String result) {
     ServiceResult serviceResult = new ServiceResult();
     serviceResult.setJobId(UUID.randomUUID().toString());
     serviceResult.setService(service);
     serviceResult.setResult(result);
     serviceResultRepository.save(serviceResult);
     return serviceResult.getJobId();
 }
}
