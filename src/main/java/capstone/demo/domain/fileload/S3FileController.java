package capstone.demo.domain.fileload;

import capstone.demo.domain.fileload.dto.PreSignedUrlResponseDto;
import capstone.demo.domain.voice.entity.VoiceModel;
import capstone.demo.global.apiPayload.ApiResponse;
import capstone.demo.global.security.AuthDetails;
import capstone.demo.global.util.GlobalAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@Slf4j
public class S3FileController {
    private final S3FileService s3FileService;

    @Value("${amazon.aws.bucket}")
    private String bucketName;

    @GetMapping("/generate-put-presigned-url")
    @Operation(summary = "음성 업로드용 presigned url 발급", description = "음성 업로드용 presigned url을 발급합니다.")
    public ResponseEntity<ApiResponse<PreSignedUrlResponseDto>> generatePresignedUrl(@AuthenticationPrincipal AuthDetails authDetails, @RequestParam String extension){

        Long userId = GlobalAuthUtil.extractUserId(authDetails);

        log.info("pre-signed url 발급");

        return ResponseEntity.ok(
                ApiResponse.onSuccess(s3FileService.generatePreSignPutUrl(userId, extension, bucketName)));
    }

    @GetMapping("/generate-get-presigned-url")
    @Operation(summary = "조회용 presigned url 발급", description = "조회용 presigned url을 발급합니다.")
    public ResponseEntity<ApiResponse<PreSignedUrlResponseDto>> generateGetPresignedUrl(@AuthenticationPrincipal AuthDetails authDetails, @RequestParam String objectKey){

        return ResponseEntity.ok(
                ApiResponse.onSuccess(s3FileService.sendPreSignGetUrl(authDetails.user(), objectKey, bucketName)));
    }

    @PostMapping("/presigned/put/multiple")
    @Operation(summary = "AI 학습용 데이터 관련 presigned url 발급", description = "AI 학습용 데이터 관련 presigned url을 발급합니다.")
    public List<PreSignedUrlResponseDto> createMultiplePresignedPutUrls(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestParam VoiceModel voiceModel
            ) {
        return s3FileService.generateMultiplePreSignPutUrls(authDetails.user(), bucketName, voiceModel);
    }


}
