package com.example.training.reservation;

import org.springframework.web.bind.annotation.RestController;

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

    // TODO: POST /api/reservations
    // ヒント: @Valid @RequestBody、LocalDate.now() を Service に渡す、
    //        ResponseEntity.created(URI)... で 201 を返す

    // TODO: GET /api/members/{id}/reservations
    // ヒント: stream().map(ReservationResponse::new).toList()
}
