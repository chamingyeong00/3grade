package com.example.boxingmatch.controller;

import com.example.boxingmatch.entity.BoxingMatch;
import com.example.boxingmatch.dto.BoxingMatchDto;
import com.example.boxingmatch.entity.User;
import com.example.boxingmatch.enums.Gender;
import com.example.boxingmatch.enums.GenderCondition;
import com.example.boxingmatch.enums.Region;
import com.example.boxingmatch.enums.SkillLevelCondition;
import com.example.boxingmatch.repository.BoxingMatchRepository;
import com.example.boxingmatch.service.BoxingMatchService;
import com.example.boxingmatch.service.MatchRequestService;
import com.example.boxingmatch.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoxingMatchController {

    private final BoxingMatchService matchService;
    private final UserService userService;
    private final MatchRequestService matchRequestService;

    @PostMapping("/match/upload")
    public String uploadMatch(@ModelAttribute BoxingMatchDto dto, HttpSession session) {
        User sessionUser = (User) session.getAttribute("loginUser");
        if (sessionUser == null) return "redirect:/login";

        // 👉 반드시 영속된 사용자 엔티티를 다시 조회
        User user = userService.getUserById(sessionUser.getUserId());

        matchService.uploadMatch(dto, user);
        return "redirect:/match/upload/success";
    }

    // ✅ 매칭 등록 GET → 등록 폼 출력
    @GetMapping("/match/upload")
    public String uploadMatchPage(Model model) {

        model.addAttribute("matchDto", new BoxingMatchDto());
        model.addAttribute("regions", Region.values());
        model.addAttribute("genders", GenderCondition.values());
        model.addAttribute("levels", SkillLevelCondition.values());
        return "match-upload";
    }


    // ✅ 매칭 등록 성공 페이지 GET
    @GetMapping("/match/upload/success")
    public String uploadSuccessPage() {
        return "match-upload-success"; // 성공 메시지 페이지 렌더링
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody BoxingMatchDto dto) {
        matchService.updateMatch(id, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/match/{id:[0-9]+}")
    public String matchDetail(@PathVariable Long id, Model model, HttpSession session) {
        BoxingMatch match = matchService.getMatchDetails(id);
        User user = (User) session.getAttribute("loginUser");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("match", match);
        model.addAttribute("user", user);
        return "match-detail";
    }

    @PostMapping("/match/{id}/request")
    public String requestMatch(@PathVariable Long id,
                               HttpSession session,
                               Model model) {
        User user = (User) session.getAttribute("loginUser"); // ✅ 세션에서 사용자 꺼내기
        if (user == null) {
            return "redirect:/login";
        }

        BoxingMatch match = matchService.getMatchDetails(id);
        // 성별 조건 확인
        boolean genderOk = match.getGenderCondition() == GenderCondition.ANY ||
                match.getGenderCondition().name().equals(user.getGender().name() + "_ONLY");

        // 실력 조건 확인
        boolean skillOk = match.getSkilllevelCondition() == user.getSkilllevelCondition();

        if (!genderOk || !skillOk) {
            model.addAttribute("error", "매칭 조건과 맞지 않아 신청할 수 없습니다.");
            model.addAttribute("match", match);
            return "match-detail";
        }

        matchRequestService.requestMatch(user, match); // 신청 저장
        return "redirect:/match/requests";
    }

    @GetMapping("/favicon.ico")
    @ResponseBody
    void returnNoFavicon() {}

    @GetMapping("/search")
    public ResponseEntity<List<BoxingMatch>> search(@RequestParam String region) {
        return ResponseEntity.ok(matchService.searchLocationMatches(Region.valueOf(region)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoxingMatch> getDetails(@PathVariable Long id) {
        return ResponseEntity.ok(matchService.getMatchDetails(id));
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Void> confirm(@PathVariable Long id) {
        matchService.confirmMatch(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/match/{matchId}/requests")
    public String viewMatchRequests(@PathVariable Long matchId, HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        BoxingMatch match = matchService.getMatchDetails(matchId);

        if (!match.getUser().getUserId().equals(loginUser.getUserId())) {
            return "redirect:/"; // 등록한 유저만 볼 수 있음
        }

        model.addAttribute("match", match);
        model.addAttribute("requests", match.getRequests()); // ✅ 여기서 매칭된 신청 목록 불러오기
        return "match-request-manage";
    }
}