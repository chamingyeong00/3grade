package com.example.boxingmatch.service;

import com.example.boxingmatch.entity.BoxingMatch;
import com.example.boxingmatch.dto.BoxingMatchDto;
import com.example.boxingmatch.entity.User;
import com.example.boxingmatch.enums.MatchStatus;
import com.example.boxingmatch.enums.Region;
import com.example.boxingmatch.repository.BoxingMatchRepository;
import com.example.boxingmatch.repository.MatchRequestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoxingMatchService {
    private final BoxingMatchRepository boxingMatchRepository;
    private final MatchRequestRepository matchRequestRepository;

    @Transactional
    public BoxingMatch uploadMatch(BoxingMatchDto dto, User user) {
        BoxingMatch match = BoxingMatch.builder()
                .title(dto.getTitle())
                .location(dto.getLocation())
                .scheduledTime(dto.getScheduledTime())  // ✅ 추가
                .region(dto.getRegion())                // ✅ 추가
                .description(dto.getDescription())      // ✅ 추가
                .genderCondition(dto.getGenderCondition())
                .skilllevelCondition(dto.getSkilllevelCondition())
                .matchstatus(dto.getMatchstatus())
                .user(user)
                .build();
        return boxingMatchRepository.save(match);
    }

    public List<BoxingMatch> findMatchesByUser(User user) {
        return boxingMatchRepository.findByUser(user);
    }

    @Transactional
    public void updateMatch(Long matchId, BoxingMatchDto boxingMatchDto) {
        BoxingMatch match = boxingMatchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("매칭을 찾을 수 없습니다."));
        match.setTitle(boxingMatchDto.getTitle());
        match.setLocation(boxingMatchDto.getLocation());
        match.setGenderCondition(boxingMatchDto.getGenderCondition());
        match.setSkilllevelCondition(boxingMatchDto.getSkilllevelCondition());
        match.setMatchstatus(boxingMatchDto.getMatchstatus());
    }

    @Transactional
    public List<BoxingMatch> searchLocationMatches(Region region) {
        return boxingMatchRepository.findByRegion(region);
    }

    public BoxingMatch getMatchDetails(Long matchId) {
        return boxingMatchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("매칭 정보를 찾을 수 없습니다."));
    }

    @Transactional
    public void confirmMatch(Long matchId) {
        BoxingMatch match = boxingMatchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("매칭이 존재하지 않습니다."));
        if (match.getMatchstatus() == MatchStatus.CLOSED) {
            throw new IllegalStateException("이미 확정된 매칭입니다.");
        }
        match.setMatchstatus(MatchStatus.CLOSED);
    }

    @Transactional
    public void deleteMatch(Long matchId) {
        boxingMatchRepository.deleteById(matchId);
    }

    public List<BoxingMatch> getAllMatches() {
        return boxingMatchRepository.findAll();
    }
}
