package capstone.demo.domain.check;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/healthCheck")
public class HealthCheckController {

    @GetMapping
    @Operation(summary = "배포 서버 연결 확인하기",
            description = "배포 서버 연결 확인 API 입니다.")
    public String healthCheck() {
        return "Server is running!";
    }
}
