package com.example.boxingmatch.repository;


import com.example.boxingmatch.entity.BoxingMatch;
import com.example.boxingmatch.entity.MatchRequest;
import com.example.boxingmatch.entity.User;
import com.example.boxingmatch.enums.MatchRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRequestRepository extends JpaRepository<MatchRequest, Long> {
    List<MatchRequest> findByUser(User user);
    List<MatchRequest> findByMatch(BoxingMatch match);
    List<MatchRequest> findAllByMatch(BoxingMatch match);
    boolean existsByMatch_MatchIdAndUser_UserIdAndStatus(Long match_matchId, Long user_userId, MatchRequestStatus status);
}
