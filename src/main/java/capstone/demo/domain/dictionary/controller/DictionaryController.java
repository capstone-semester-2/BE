package capstone.demo.domain.dictionary.controller;

import capstone.demo.domain.dictionary.Dictionary;
import capstone.demo.domain.dictionary.dto.DictionaryResponse;
import capstone.demo.domain.dictionary.service.DictionaryService;
import capstone.demo.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @GetMapping("/dictionary/list")
    @Operation(summary = "수화 사전 무한 스크롤 리스트 조회",
            description = "lastId 기반으로 무한 스크롤 형식으로 조회합니다.")
    public ResponseEntity<ApiResponse<List<Dictionary>>> getDictionaryList(
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "20") int size
    ) {
        List<Dictionary> result;

        if (lastId == null) {
            result = dictionaryService.getFirstPage(size);
        } else {
            result = dictionaryService.getNextPage(lastId, size);
        }

        return ResponseEntity.ok(ApiResponse.onSuccess(result));
    }


    @GetMapping("/dictionary/search")
    @Operation(summary = "수화 사전 검색",
            description = "입력 키워드 기반으로 모든 검색 결과를 반환합니다.")
    public ResponseEntity<ApiResponse<List<Dictionary>>> searchDictionary(
            @RequestParam String keyword
    ) {
        List<Dictionary> result = dictionaryService.searchAll(keyword);

        return ResponseEntity.ok(ApiResponse.onSuccess(result));
    }

}
