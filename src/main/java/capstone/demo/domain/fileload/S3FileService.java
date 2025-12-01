package capstone.demo.domain.fileload;

import capstone.demo.domain.fileload.dto.PreSignedUrlResponseDto;
import capstone.demo.domain.user.entity.User;
import capstone.demo.domain.voice.entity.VoiceModel;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class S3FileService {
    static final int fileCount = 20;
    @Autowired
    private AmazonS3 amazonS3;
    public PreSignedUrlResponseDto generatePreSignPutUrl(Long userId,
                                                      String extension,
                                                      String bucketName){

        String objectKey = "voice" + userId + UUID.randomUUID()+ "." + extension;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE,10); // 10 minutes

        String preSignedUrl = amazonS3.generatePresignedUrl(bucketName, objectKey, calendar.getTime(),HttpMethod.PUT).toString();

        return PreSignedUrlResponseDto.builder()
                .preSignedUrl(preSignedUrl)
                .objectKey(objectKey)
                .expiresAt(Date.from(calendar.getTime().toInstant()))
                .build();
    }


    public String generatePreSignGetUrl(String objectKey, String bucketName) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 10); // presigned URL 유효기간 10분

        return amazonS3
                .generatePresignedUrl(bucketName, objectKey, calendar.getTime(), HttpMethod.GET)
                .toString();
    }

    public void deleteObjectFromS3(String objectKey, String bucketName) {
        try {
            amazonS3.deleteObject(bucketName, objectKey);
        } catch (AmazonS3Exception e) {
            throw new RuntimeException("S3 객체 삭제 중 AmazonS3Exception 발생: " + e.getMessage(), e);

        } catch (SdkClientException e) {
            throw new RuntimeException("S3와 통신 중 네트워크 오류 발생: " + e.getMessage(), e);

        } catch (Exception e) {
            throw new RuntimeException("S3 삭제 중 알 수 없는 오류 발생: " + e.getMessage(), e);
        }
    }


    public PreSignedUrlResponseDto sendPreSignGetUrl(User user, String objectKey, String bucketName) {
        String preSignedUrl = generatePreSignGetUrl(objectKey, bucketName);

        return PreSignedUrlResponseDto.builder()
                .preSignedUrl(preSignedUrl)
                .objectKey(objectKey)
                .expiresAt(Date.from(new Date().toInstant()))
                .build();
    }

    public List<PreSignedUrlResponseDto> generateMultiplePreSignPutUrls(
            User user,
            String bucketName,
            VoiceModel voiceModel
    ) {

        List<PreSignedUrlResponseDto> result = new ArrayList<>();

        for (int i = 0; i < fileCount; i++) {
            String objectKey =  voiceModel.toString().toLowerCase() + "/training-voice/" + user.getId() + "/" + UUID.randomUUID() + ".wav";

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MINUTE, 10);

            String preSignedUrl = amazonS3.generatePresignedUrl(
                    bucketName,
                    objectKey,
                    calendar.getTime(),
                    HttpMethod.PUT
            ).toString();

            result.add(
                    PreSignedUrlResponseDto.of(
                            preSignedUrl,
                            objectKey,
                            calendar.getTime()
                    )
            );
        }

        return result;
    }

    public List<String> generatePreSignGetUrls(List<String> objectKeys, String bucketName) {
        List<String> preSignedGeturls = new ArrayList<>();

        for (String objectKey : objectKeys) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MINUTE, 10); // presigned URL 유효기간 10분

            String preSignedUrl = amazonS3.generatePresignedUrl(
                    bucketName,
                    objectKey,
                    calendar.getTime(),
                    HttpMethod.GET
            ).toString();

            preSignedGeturls.add(preSignedUrl);
        }

        return preSignedGeturls;
    }



//    public List<String> listObjectKeys(String prefix, String bucketName) {
//        ListObjectsV2Request req = new ListObjectsV2Request()
//                .withBucketName(bucketName)
//                .withPrefix(prefix);
//
//        ListObjectsV2Result result = amazonS3.listObjectsV2(req);
//
//        List<String> keys = new ArrayList<>();
//        for (S3ObjectSummary obj : result.getObjectSummaries()) {
//            keys.add(obj.getKey());
//        }
//
//        return keys;
//    }
}
