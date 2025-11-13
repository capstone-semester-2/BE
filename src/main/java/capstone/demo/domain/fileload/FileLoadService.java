package capstone.demo.domain.fileload;

import capstone.demo.domain.fileload.dto.PreSignedUrlResponseDto;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class FileLoadService {

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

}
