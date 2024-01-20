package controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import model.ServiceResult;
import service.ServiceDiscoveryService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
public class ServiceDiscoveryController {

 @Autowired
 private ServiceDiscoveryService serviceDiscoveryService;

 @PostMapping("/discover")
 public CompletableFuture<String> discoverServices(@RequestBody List<String> services) {
     CompletableFuture<String> ec2JobId = serviceDiscoveryService.discoverEC2();
     CompletableFuture<String> s3JobId = serviceDiscoveryService.discoverS3();

     return CompletableFuture.allOf(ec2JobId, s3JobId)
             .thenApply(ignored -> "JobId: " + ec2JobId.join() + ", " + s3JobId.join());
 }

 @GetMapping("/job/{jobId}")
 public String getJobResult(@PathVariable String jobId) {
     return serviceDiscoveryService.getJobResult(jobId);
 }

 @GetMapping("/discovery/{service}")
 public List<ServiceResult> getDiscoveryResult(@PathVariable String service) {
     return serviceDiscoveryService.getDiscoveryResult(service);
 }

 @PostMapping("/s3/discover-objects/{bucketName}")
 public CompletableFuture<String> getS3BucketObjects(@PathVariable String bucketName) {
     return serviceDiscoveryService.getS3BucketObjects(bucketName);
 }

 @GetMapping("/s3/object-count/{bucketName}")
 public int getS3BucketObjectCount(@PathVariable String bucketName) {
     return serviceDiscoveryService.getS3BucketObjectCount(bucketName);
 }

 @GetMapping("/s3/object-like/{bucketName}/{pattern}")
 public List<ServiceResult> getS3BucketObjectLike(@PathVariable String bucketName, @PathVariable String pattern) {
     return serviceDiscoveryService.getS3BucketObjectLike(bucketName, pattern);
 }
}
