package com.example.boxingmatch.controller;

import com.example.boxingmatch.entity.BoxingMatch;
import com.example.boxingmatch.entity.MatchRequest;
import com.example.boxingmatch.entity.Review;
import com.example.boxingmatch.entity.User;
import com.example.boxingmatch.repository.BoxingMatchRepository;
import com.example.boxingmatch.repository.MatchRequestRepository;
import com.example.boxingmatch.repository.ReviewRepository;
import com.example.boxingmatch.service.BoxingMatchService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MatchViewController {

    private final BoxingMatchService boxingMatchService;
    private final MatchRequestRepository matchRequestRepository;
    private final BoxingMatchRepository boxingMatchRepository;
    private final ReviewRepository reviewRepository;

    @GetMapping("/match/manage")
    public String manageMatches(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        // 내가 등록한 매칭
        List<BoxingMatch> myPostedMatches = boxingMatchRepository.findByUser(loginUser);

        // 내가 신청한 매칭 (ACCEPTED만 필터링해도 OK)
        List<MatchRequest> myRequests = matchRequestRepository.findByUser(loginUser);
        List<BoxingMatch> myRequestedMatches = myRequests.stream()
                .map(MatchRequest::getMatch)
                .distinct()
                .toList();

        // 작성한 리뷰 정보 조회
        List<Review> myReviews = reviewRepository.findByUser(loginUser);
        Set<Long> reviewedMatchIds = myReviews.stream()
                .map(r -> r.getMatch().getMatchId())
                .collect(Collectors.toSet());
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("myPostedMatches", myPostedMatches);
        model.addAttribute("myRequestedMatches", myRequestedMatches);
        model.addAttribute("reviewedMatches", reviewedMatchIds); // ✅ 추가
        return "match-manage"; // 템플릿 이름
    }


    @GetMapping("/match/list")
    public String showMatchList(Model model) {
        List<BoxingMatch> matches = boxingMatchService.findAll(); // DB에서 매칭 목록 가져오기
        model.addAttribute("matches", matches); // thymeleaf에 넘김
        return "match-list"; // match-list.html 보여주기
    }

}