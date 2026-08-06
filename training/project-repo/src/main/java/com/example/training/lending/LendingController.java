package com.example.training.lending;

import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 貸出・返却APIの窓口（Controller）。
 *
 * <p>業務ロジックは {@link LendingService} に任せる（3層構造）。
 * 「今日の日付」はここ（外側）で決めて Service に渡す。こうしておくと
 * Service は日付を引数で受け取るだけになり、テストで好きな日付を渡せる。
 */
@RestController
public class LendingController {

    private final LendingService lendingService;

    public LendingController(LendingService lendingService) {
        this.lendingService = lendingService;
    }

    /**
     * 貸出: POST /api/lendings → 201 Created + 作成した貸出。
     * 在庫が無ければ409、本や利用者がいなければ404、入力不備は400。
     */
    @PostMapping("/api/lendings")
    public ResponseEntity<LendingResponse> lend(@Valid @RequestBody LendingRequest request) {
        Lending created = lendingService.lend(request, LocalDate.now());
        URI location = URI.create("/api/lendings/" + created.getId());
        return ResponseEntity.created(location).body(new LendingResponse(created));
    }

    /**
     * 返却: POST /api/lendings/{id}/return → 200 OK + 返却後の貸出。
     * 貸出記録が無ければ404、すでに返却済みなら409。
     */
    @PostMapping("/api/lendings/{id}/return")
    public LendingResponse returnBook(@PathVariable Long id) {
        return new LendingResponse(lendingService.returnBook(id, LocalDate.now()));
    }

    /**
     * 貸出履歴: GET /api/members/{id}/lendings → 200 OK + 貸出配列（新しい順）。
     * 利用者が無ければ404。
     */
    @GetMapping("/api/members/{id}/lendings")
    public List<LendingResponse> historyByMember(@PathVariable Long id) {
        return lendingService.findByMember(id).stream()
                .map(LendingResponse::new)
                .toList();
    }

    @GetMapping("/api/lendings/overdue")
    public List<LendingResponse> findOverdue(){
        return lendingService.findOverdue(LocalDate.now()).stream()
                .map(LendingResponse::new)
                .toList();
    }
}
