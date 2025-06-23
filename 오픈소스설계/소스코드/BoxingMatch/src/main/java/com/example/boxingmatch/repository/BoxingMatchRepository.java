package com.example.boxingmatch.repository;

import com.example.boxingmatch.entity.BoxingMatch;
import com.example.boxingmatch.entity.User;
import com.example.boxingmatch.enums.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoxingMatchRepository extends JpaRepository<BoxingMatch, Long> {
    List<BoxingMatch> findByLocationContaining(String location);
    List<BoxingMatch> findByUser(User user);
    List<BoxingMatch> findByRegion(Region region);
}
