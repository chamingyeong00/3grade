package com.example.boxingmatch.controller;

import com.example.boxingmatch.entity.BoxingMatch;
import com.example.boxingmatch.entity.MatchRequest;
import com.example.boxingmatch.dto.MatchRequestDto;
import com.example.boxingmatch.entity.User;
import com.example.boxingmatch.repository.BoxingMatchRepository;
import com.example.boxingmatch.repository.MatchRequestRepository;
import com.example.boxingmatch.repository.UserRepository;
import com.example.boxingmatch.service.MatchRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/requests")
public class MatchRequestController {

    private final MatchRequestService requestService;
    private final MatchRequestService matchRequestService;
    private final UserRepository userRepository;
    private final BoxingMatchRepository boxingMatchRepository;

    @PostMapping
    public ResponseEntity<?> requestMatch(@RequestBody MatchRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        BoxingMatch match = boxingMatchRepository.findById(dto.getMatchId())
                .orElseThrow(() -> new RuntimeException("매칭을 찾을 수 없습니다."));

        requestService.requestMatch(user, match);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        requestService.cancelMatch(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/match/request/{requestId}/accept")
    public String acceptRequest(@PathVariable Long requestId) {
        matchRequestService.acceptRequest(requestId);
        return "redirect:/match/manage"; // 수락 후 관리 페이지로 이동
    }

    @PostMapping("/{requestId}/accept")
    public String accept(@PathVariable Long requestId) {
        matchRequestService.acceptRequest(requestId);
        return "redirect:/match/manage";
    }

    @PostMapping("/{requestId}/reject")
    public String reject(@PathVariable Long requestId) {
        matchRequestService.cancelMatch(requestId);
        return "redirect:/match/manage";
    }
}