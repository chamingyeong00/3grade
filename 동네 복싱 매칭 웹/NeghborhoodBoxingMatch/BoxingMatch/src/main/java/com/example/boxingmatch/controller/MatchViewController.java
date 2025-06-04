package com.example.boxingmatch.controller;

import com.example.boxingmatch.entity.BoxingMatch;
import com.example.boxingmatch.entity.User;
import com.example.boxingmatch.service.BoxingMatchService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MatchViewController {

    private final BoxingMatchService boxingMatchService;

    @GetMapping("/match/manage")
    public String myMatches(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loginUser");
        if (user == null) return "redirect:/login";

        List<BoxingMatch> myMatches = boxingMatchService.findMatchesByUser(user);
        model.addAttribute("matches", myMatches);
        return "my-match-request";
    }
}