package capstone.demo.domain.fileload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ObjectKeyInfo {
        private Integer objectKeyId;
        private String objectKey;

}
