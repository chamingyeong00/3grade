package com.example.boxingmatch.service;
import com.example.boxingmatch.entity.MatchRequest;
import com.example.boxingmatch.enums.MatchRequestStatus;
import com.example.boxingmatch.entity.BoxingMatch;
import com.example.boxingmatch.entity.Review;
import com.example.boxingmatch.dto.ReviewDto;
import com.example.boxingmatch.entity.User;
import com.example.boxingmatch.enums.MatchStatus;
import com.example.boxingmatch.repository.BoxingMatchRepository;
import com.example.boxingmatch.repository.ReviewRepository;
import com.example.boxingmatch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BoxingMatchRepository matchRepository;
    private final UserRepository userRepository;

    public Review reviewMatch(ReviewDto reviewDto) {
        BoxingMatch match = matchRepository.findById(reviewDto.getMatchId())
                .orElseThrow(() -> new RuntimeException("매칭을 찾을 수 없습니다."));

        long acceptedCount = match.getRequests().stream()
                .filter(req -> req.getStatus() == MatchRequestStatus.ACCEPTED)
                .count();


        if (acceptedCount < 2) {
            throw new IllegalStateException("수락된 요청이 충분하지 않음 → 리뷰 불가");
        }

        User user = userRepository.findById(reviewDto.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Review review = Review.builder()
                .rating(reviewDto.getRating())
                .comment(reviewDto.getComment())
                .user(user)
                .match(match)
                .build();

        return reviewRepository.save(review);
    }

    boolean canWriteReview(BoxingMatch match) {
        long acceptedCount = match.getRequests().stream()
                .filter(req -> req.getStatus() == MatchRequestStatus.ACCEPTED)
                .count();


        return acceptedCount >= 2;
    }

    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    public Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));
    }

    @Transactional
    public void updateReview(Long reviewId, ReviewDto dto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
    }
    private boolean isReviewable(BoxingMatch match) {
        return match.getMatchstatus() == MatchStatus.CLOSED;
        // 또는 수락 상태 기반으로 판단할 수 있다면:
        // return match.getSenderRequest().getStatus() == ACCEPTED &&
        //        match.getReceiverRequest().getStatus() == ACCEPTED;
    }

}
