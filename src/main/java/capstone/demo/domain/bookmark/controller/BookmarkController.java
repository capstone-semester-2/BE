package capstone.demo.domain.bookmark.controller;

import capstone.demo.domain.bookmark.Bookmark;
import capstone.demo.domain.bookmark.dto.BookmarkResponse;
import capstone.demo.domain.bookmark.service.BookmarkService;
import capstone.demo.domain.dictionary.Dictionary;
import capstone.demo.global.apiPayload.ApiResponse;
import capstone.demo.global.security.AuthDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/bookmark")
    @Operation(summary = "북마크 저장 기능",
            description = "백과사전에서 원하는 항목을 북마크하는 API입니다.")
    public ResponseEntity<ApiResponse<BookmarkResponse>> addBookmark(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestParam Long dictionaryId
    ) {
        BookmarkResponse resp = bookmarkService.addBookmark(authDetails.user() , dictionaryId);
        return ResponseEntity.ok(ApiResponse.onSuccess(resp));
    }

    @GetMapping("/bookmark/list")
    @Operation(summary = "수화 사전 무한 스크롤 리스트 조회",
            description = "lastId 기반으로 무한 스크롤 형식으로 조회합니다.")
    public ResponseEntity<ApiResponse<List<Dictionary>>> getDictionaryList(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "20") int size
    ) {
        List<Dictionary> result;

        if (lastId == null) {
            result = bookmarkService.getFirstPage(authDetails.user(), size);
        } else {
            result = bookmarkService.getNextPage(authDetails.user(), lastId, size);
        }

        return ResponseEntity.ok(ApiResponse.onSuccess(result));
    }

}
