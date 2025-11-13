package capstone.demo.domain.fileload.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class PreSignedUrlResponseDto {
    private String preSignedUrl;
    private String objectKey;
    private Date expiresAt;

    public static PreSignedUrlResponseDto of(String preSignedUrl, String objectKey, Date expiresAt) {
        return PreSignedUrlResponseDto.builder()
                .preSignedUrl(preSignedUrl)
                .objectKey(objectKey)
                .expiresAt(expiresAt)
                .build();
    }
}
