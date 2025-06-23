package com.example.boxingmatch.controller;

import com.example.boxingmatch.dto.MatchRequestDto;
import com.example.boxingmatch.entity.BoxingMatch;
import com.example.boxingmatch.entity.User;
import com.example.boxingmatch.repository.BoxingMatchRepository;
import com.example.boxingmatch.repository.UserRepository;
import com.example.boxingmatch.service.MatchRequestService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/match")
public class MatchRequestController {

    private final MatchRequestService matchRequestService;
    private final UserRepository userRepository;
    private final BoxingMatchRepository boxingMatchRepository;

    /**
     * 🔹 REST 방식 요청 매칭 (AJAX 등에서 사용)
     */
    @PostMapping("/{id}/request")
    public String requestMatch(@PathVariable Long id,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loginUser");
        if (user == null) return "redirect:/login";

        BoxingMatch match = boxingMatchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("매칭을 찾을 수 없습니다."));

        try {
            matchRequestService.requestMatch(user, match);
            redirectAttributes.addFlashAttribute("success", "매칭 신청이 완료되었습니다!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/match/" + id;
    }

    /**
     * 🔹 거절 기능
     */
    @PostMapping("/{id}/reject")
    public String reject(@PathVariable Long id) {
        matchRequestService.cancelMatch(id);
        return "redirect:/match/manage";
    }

    /**
     * 🔹 수락 기능
     */
    @PostMapping("/{id}/accept")
    public String accept(@PathVariable Long id) {
        matchRequestService.acceptRequest(id);
        return "redirect:/match/manage";
    }

    /**
     * 🔹 취소 (삭제) 기능
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        matchRequestService.cancelMatch(id);
        return ResponseEntity.ok().build();
    }
}