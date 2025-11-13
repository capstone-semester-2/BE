package capstone.demo.domain.bookmark.controller;

import capstone.demo.domain.bookmark.dto.BookmarkResponse;
import capstone.demo.domain.bookmark.service.BookmarkService;
import capstone.demo.global.apiPayload.ApiResponse;
import capstone.demo.global.security.AuthDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookmarkResponse>> addBookmark(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestParam Long userId,
            @RequestParam Long dictionaryId
    ) {
        BookmarkResponse resp = bookmarkService.addBookmark(authDetails.user() , dictionaryId);
        return ResponseEntity.ok(ApiResponse.onSuccess(resp));
    }

}
