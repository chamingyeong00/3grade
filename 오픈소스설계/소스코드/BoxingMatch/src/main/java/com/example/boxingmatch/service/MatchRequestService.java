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

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchRequestService {

    private final MatchRequestRepository matchRequestRepository;
    private final BoxingMatchRepository boxingMatchRepository;
    private final UserRepository userRepository;

    public void requestMatch(User user, BoxingMatch match) {

        // 🚨 에러 메시지를 누적할 변수
        StringBuilder errorMessages = new StringBuilder();

        // ✅ 성별 조건 검사
        if (match.getGenderCondition() != GenderCondition.ANY) {
            if (match.getGenderCondition() == GenderCondition.FEMALE_ONLY &&
                    (user.getGender() == null || user.getGender() != Gender.FEMALE)) {
                errorMessages.append("성별 조건이 맞지 않습니다. ");
            }
            if (match.getGenderCondition() == GenderCondition.MALE_ONLY &&
                    (user.getGender() == null || user.getGender() != Gender.MALE)) {
                errorMessages.append("성별 조건이 맞지 않습니다. ");
            }
        }

        // ✅ 실력 조건 검사
        if (!match.getSkilllevelCondition().equals(user.getSkilllevelCondition())) {
            errorMessages.append("실력 조건이 맞지 않습니다. ");
        }

        // 🚫 에러가 누적되었으면 예외 발생
        if (errorMessages.length() > 0) {
            throw new IllegalArgumentException(errorMessages.toString().trim());
        }

        // ✅ 조건 통과 → 신청 저장
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
        matchRequestRepository.save(req);
    }

    @Transactional
    public void acceptRequest(Long requestId) {
        MatchRequest request = matchRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("신청이 존재하지 않습니다."));

        if (request.getStatus() != MatchRequestStatus.PENDING) {
            throw new IllegalStateException("이미 처리된 신청입니다.");
        }

        request.setStatus(MatchRequestStatus.ACCEPTED);

        BoxingMatch match = request.getMatch();
        match.setMatchstatus(MatchStatus.CLOSED);

        List<MatchRequest> allRequests = matchRequestRepository.findAllByMatch(match); // 여기를 수정
        for (MatchRequest other : allRequests) {
            if (!other.getRequestId().equals(requestId)) {
                other.setStatus(MatchRequestStatus.CANCELED);
            }
        }
    }


    public void deleteMatchRequest(Long requestId) {
        matchRequestRepository.deleteById(requestId);
    }
}
