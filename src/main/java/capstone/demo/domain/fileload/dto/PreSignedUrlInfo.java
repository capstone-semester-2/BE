package capstone.demo.domain.fileload.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PreSignedUrlInfo {
    private Integer audioId;
    private String audioUrl;
}
