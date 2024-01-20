package Respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import model.S3DiscoveryResult;

public interface S3DiscoveryResultRepository extends JpaRepository<S3DiscoveryResult, Long> {
 List<S3DiscoveryResult> findByService(String service);
 List<S3DiscoveryResult> findByBucketName(String bucketName);
}
