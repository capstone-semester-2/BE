package capstone.demo.domain.fileload;

import capstone.demo.domain.fileload.dto.PreSignedUrlResponseDto;
import capstone.demo.global.apiPayload.ApiResponse;
import capstone.demo.global.security.AuthDetails;
import capstone.demo.global.util.GlobalAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class FileLoadController {
    private final FileLoadService fileLoadService;

    @Value("${amazon.aws.bucket}")
    private String bucketName;

    @GetMapping("/generate-presigned-url")
    @Operation(summary = "음성 업로드용 presigned url 발급", description = "음성 업로드용 presigned url을 발급합니다.")
    public ResponseEntity<ApiResponse<PreSignedUrlResponseDto>> generatePresignedUrl(@AuthenticationPrincipal AuthDetails authDetails, @RequestParam String extension){

        Long userId = GlobalAuthUtil.extractUserId(authDetails);

        return ResponseEntity.ok(
                ApiResponse.onSuccess(fileLoadService.generatePreSignPutUrl(userId, extension, bucketName)));

    }

}
