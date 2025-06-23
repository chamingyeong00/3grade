package com.example.boxingmatch.controller;

import com.example.boxingmatch.entity.BoxingMatch;
import com.example.boxingmatch.entity.Review;
import com.example.boxingmatch.dto.ReviewDto;
import com.example.boxingmatch.entity.User;
import com.example.boxingmatch.enums.MatchRequestStatus;
import com.example.boxingmatch.enums.MatchStatus;
import com.example.boxingmatch.repository.BoxingMatchRepository;
import com.example.boxingmatch.repository.ReviewRepository;
import com.example.boxingmatch.repository.UserRepository;
import com.example.boxingmatch.service.BoxingMatchService;
import com.example.boxingmatch.service.ReviewService;
import com.example.boxingmatch.service.UserService;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final BoxingMatchService boxingMatchService;
    private final BoxingMatchRepository boxingMatchRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final UserService userService; // ✅ 추가됨



    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody ReviewDto dto) {
        reviewService.updateReview(id, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> get(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReview(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public String createReview(
            @RequestParam Long matchId,
            @RequestParam Long userId,
            @RequestParam int rating,
            @RequestParam String comment
    ) {
        BoxingMatch match = boxingMatchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("매칭 없음"));

        if (match.getMatchstatus() != MatchStatus.CLOSED) {
            throw new IllegalStateException("리뷰는 매칭 완료된 경우에만 작성 가능합니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        // ✅ 중복 리뷰 방지
        boolean alreadyExists = reviewRepository.existsByMatchAndUser(match, user);
        if (alreadyExists) {
            throw new IllegalStateException("이미 이 매칭에 대한 리뷰를 작성하셨습니다.");
        }

        Review review = Review.builder()
                .rating(rating)
                .comment(comment)
                .user(user)
                .match(match)
                .build();

        reviewRepository.save(review);
        return "redirect:/";
    }

    @GetMapping("/new")
    public String showReviewForm(@RequestParam("matchId") Long matchId, HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        // 매칭 정보 불러오기 (서비스 코드 필요)
        BoxingMatch match = boxingMatchService.findMatchForReview(matchId, loginUser.getUserId());
        if (match == null) return "redirect:/";

        model.addAttribute("match", match);
        return "review-form"; // templates/review-form.html
    }

//    @GetMapping("/write")
//    public String showReviewForm(@RequestParam("matchId") Long matchId, HttpSession session, Model model) {
//        User user = (User) session.getAttribute("user");
//        if (user == null) return "redirect:/login";
//
//        model.addAttribute("matchId", matchId); // match 객체 자체가 아니라 matchId만 넘김
//        model.addAttribute("userId", user.getUserId());
//        return "review";
//    }



    boolean canWriteReview(BoxingMatch match) {
        long acceptedCount = match.getRequests().stream()
                .filter(req -> req.getStatus() == MatchRequestStatus.ACCEPTED)
                .count();


        return acceptedCount >= 2;
    }
}

