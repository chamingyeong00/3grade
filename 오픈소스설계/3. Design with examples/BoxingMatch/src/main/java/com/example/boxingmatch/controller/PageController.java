package com.example.boxingmatch.controller;

import com.example.boxingmatch.dto.UserDto;
import com.example.boxingmatch.entity.BoxingMatch;
import com.example.boxingmatch.entity.MatchRequest;
import com.example.boxingmatch.entity.User;
import com.example.boxingmatch.enums.Gender;
import com.example.boxingmatch.enums.GenderCondition;
import com.example.boxingmatch.enums.Region;
import com.example.boxingmatch.enums.SkillLevelCondition;
import com.example.boxingmatch.repository.MatchRequestRepository;
import com.example.boxingmatch.repository.UserRepository;
import com.example.boxingmatch.service.BoxingMatchService;
import com.example.boxingmatch.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final BoxingMatchService matchService;
    private final MatchRequestRepository matchRequestRepository;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {

        User user = userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);

        if (user == null) {
            model.addAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
            return "login";
        }

        // ✅ 세션에 로그인한 유저 정보 저장
        session.setAttribute("loginUser", user); // "loginUser"는 키 이름입니다.

        return "redirect:/"; // 로그인 성공 후 리디렉션
    }


    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("regions", Region.values());
        model.addAttribute("levels", SkillLevelCondition.values());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserDto dto) {
        userService.registerUser(dto);
        return "register-success";
    }

    @GetMapping("/match/search")
    public String searchMatch(@RequestParam(required = false) String region, Model model) {
        model.addAttribute("regions", Region.values());

        if (region != null && !region.isEmpty()) {
            model.addAttribute("matches", matchService.searchLocationMatches(Region.valueOf(region)));
        }

        return "match-search";
    }

    @GetMapping("/match/detail")
    public String matchDetail() {
        return "match-detail";
    }

    @GetMapping("/match/requests")
    public String myMatchRequests(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loginUser");
        if (user == null) return "redirect:/login";

        List<MatchRequest> requests = matchRequestRepository.findByUser(user);
        model.addAttribute("requests", requests);
        return "my-match-request";
    }

    @GetMapping("/reviews")
    public String reviewPage() {
        return "review";
    }

    @GetMapping("/settings")
    public String settingsPage() {
        return "settings";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 종료
        return "redirect:/login"; // 로그인 페이지로 리디렉션
    }

}