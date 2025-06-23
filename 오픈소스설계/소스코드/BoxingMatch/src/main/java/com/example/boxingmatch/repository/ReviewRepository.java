package com.example.boxingmatch.repository;


import com.example.boxingmatch.entity.BoxingMatch;
import com.example.boxingmatch.entity.Review;
import com.example.boxingmatch.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByMatchAndUser(BoxingMatch match, User user);

    List<Review> findByUser(User loginUser);
}
