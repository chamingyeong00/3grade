package com.example.boxingmatch.service;

import com.example.boxingmatch.entity.BoxingMatch;
import com.example.boxingmatch.entity.MatchRequest;
import com.example.boxingmatch.dto.MatchRequestDto;
import com.example.boxingmatch.entity.User;
import com.example.boxingmatch.enums.Gender;
import com.example.boxingmatch.enums.GenderCondition;
import com.example.boxingmatch.enums.MatchRequestStatus;
import com.example.boxingmatch.enums.MatchStatus;
import com.example.boxingmatch.repository.BoxingMatchRepository;
import com.example.boxingmatch.repository.MatchRequestRepository;
import com.example.boxingmatch.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchRequestService {

    private final MatchRequestRepository matchRequestRepository;
    private final BoxingMatchRepository boxingMatchRepository;
    private final UserRepository userRepository;
    public void requestMatch(User user, BoxingMatch match) {
        if (!match.getGenderCondition().equals(GenderCondition.ANY)) {
            if (match.getGenderCondition() == GenderCondition.FEMALE_ONLY && user.getGender() != Gender.FEMALE) {
                throw new IllegalArgumentException("성별 조건이 맞지 않습니다.");
            }
            if (match.getGenderCondition() == GenderCondition.MALE_ONLY && user.getGender() != Gender.MALE) {
                throw new IllegalArgumentException("성별 조건이 맞지 않습니다.");
            }
        }

        if (!match.getSkilllevelCondition().equals(user.getSkilllevelCondition())) {
            throw new IllegalArgumentException("실력 조건이 맞지 않습니다.");
        }

        MatchRequest request = MatchRequest.builder()
                .status(MatchRequestStatus.PENDING)
                .user(user)
                .match(match)
                .build();

        matchRequestRepository.save(request);
    }

    public void cancelMatch(Long requestId) {
        MatchRequest req = matchRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("신청 내역이 없습니다."));

        if (req.getStatus() == MatchRequestStatus.CANCELED) {
            throw new IllegalStateException("이미 취소된 신청입니다.");
        }
        req.setStatus(MatchRequestStatus.CANCELED);
    }

    @Transactional
    public void acceptRequest(Long requestId) {
        MatchRequest request = matchRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("신청이 존재하지 않습니다."));

        if (request.getStatus() != MatchRequestStatus.PENDING) {
            throw new IllegalStateException("이미 처리된 신청입니다.");
        }

        request.setStatus(MatchRequestStatus.ACCEPTED);

        // 선택: 매칭 상태도 변경 (단일 매칭만 허용할 경우)
        BoxingMatch match = request.getMatch();
        match.setMatchstatus(MatchStatus.CLOSED);
    }

    public void deleteMatchRequest(Long requestId) {
        matchRequestRepository.deleteById(requestId);
    }
}
