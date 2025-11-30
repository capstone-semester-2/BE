package capstone.demo.domain.voice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

public class FileUploadCompleteDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UploadCompleteRequest {
        private String emitterId;
        private String objectKey;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UploadCompleteAndLearningRequest {
        private String emitterId;
        private List<String> objectKeys;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UploadCompleteResponse {
        private String preSignedUrl;
        private String objectKey;
        private Date expiresAt;

        public static UploadCompleteResponse of(String preSignedUrl, String objectKey, Date expiresAt) {
            return UploadCompleteResponse.builder()
                    .preSignedUrl(preSignedUrl)
                    .objectKey(objectKey)
                    .expiresAt(expiresAt)
                    .build();
        }
    }

}
