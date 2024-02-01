package FitnessBro.service.ReviewService;

import FitnessBro.converter.ReviewConverter;
import FitnessBro.domain.coach.Entity.Coach;
import FitnessBro.domain.review.Entity.Review;
import FitnessBro.domain.member.Entity.Member;
import FitnessBro.respository.CoachRepository;
import FitnessBro.respository.MemberRepository;
import FitnessBro.respository.ReviewRepository;
import FitnessBro.web.dto.ReviewRequestDTO;
import FitnessBro.web.dto.ReviewResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Console;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private  final MemberRepository memberRepository;
    private final CoachRepository coachRepository;


    public List<Review> getByCoachId(Long coachId) {
        List<Review> result = reviewRepository.findByCoachId(coachId);
        return result;
    }

    public List<ReviewResponseDTO.ReviewByUserDTO> getReviews(Long userId) {
        List<Review> reviews = reviewRepository.findAllByMemberId(userId);
        return reviews.stream()
                .map(ReviewResponseDTO.ReviewByUserDTO::from)
                .collect(Collectors.toList());

    }
    public void createReview(ReviewRequestDTO.CreateReviewDTO createReviewDTO, Long userId){

        Member member = memberRepository.getById(userId);
        Coach coach = coachRepository.getCoachByNickname(createReviewDTO.getCoachNickname());

        System.out.println(coach);

        Review review = Review.builder()
                .date(createReviewDTO.getCreatedAt())
                .contents(createReviewDTO.getContents())
                .rating(createReviewDTO.getRating())
                .member(member)
                .coach(coach)
                .build();

        reviewRepository.save(review);

    }

}