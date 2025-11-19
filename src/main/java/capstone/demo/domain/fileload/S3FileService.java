package capstone.demo.domain.fileload;

import capstone.demo.domain.fileload.dto.PreSignedUrlResponseDto;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class S3FileService {

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


}
