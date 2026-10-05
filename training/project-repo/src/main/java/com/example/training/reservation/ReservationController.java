package com.example.training.reservation;

import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;

/**
 * 予約APIの窓口（Controller）。
 *
 * <p>お手本: {@link com.example.training.lending.LendingController}
 * <ul>
 *   <li>POST /api/reservations → 201 Created</li>
 *   <li>GET /api/members/{id}/reservations → 200 OK + 一覧</li>
 * </ul>
 */
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/api/reservations")
    public ResponseEntity<ReservationResponse> reserve(@Valid @RequestBody ReservationRequest request) {
        Reservation created = reservationService.reserve(request, LocalDate.now());
        URI location = URI.create("/api/reservations/" + created.getId());
        return ResponseEntity.created(location).body(new ReservationResponse(created));
    }
    @GetMapping("/api/members/{id}/reservations")
    public List<ReservationResponse> listByMember(@PathVariable Long id) {
        return reservationService.findByMember(id).stream()
                .map(ReservationResponse::new)
                .toList();
    }
}
