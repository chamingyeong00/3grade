package com.example.boxingmatch.repository;


import com.example.boxingmatch.entity.MatchRequest;
import com.example.boxingmatch.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRequestRepository extends JpaRepository<MatchRequest, Long> {
    List<MatchRequest> findByUser(User user);
}
