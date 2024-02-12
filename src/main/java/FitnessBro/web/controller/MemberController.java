package FitnessBro.web.controller;

import FitnessBro.apiPayload.ApiResponse;
import FitnessBro.converter.CoachConverter;
import FitnessBro.domain.Coach;
import FitnessBro.domain.Member;
import FitnessBro.service.LoginService.LoginService;
import FitnessBro.service.MemberService.MemberCommandService;
import FitnessBro.service.MemberService.MemberQueryService;
import FitnessBro.service.ReviewService.ReviewService;
import FitnessBro.web.dto.Coach.CoachResponseDTO;
import FitnessBro.web.dto.Member.MemberRequestDTO;
import FitnessBro.web.dto.review.ReviewRequestDTO;
import FitnessBro.web.dto.review.ReviewResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final ReviewService reviewService;
    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;
    private final LoginService loginService;


    @GetMapping("/favorites")
    @Operation(summary = "사용자가 찜한 동네형 목록 조회 API")
    public ResponseEntity<ApiResponse<List<CoachResponseDTO.favoriteCoachDTO>>> getFavoriteCoachList(@RequestHeader(value = "token") String token){
        String userEmail = loginService.decodeJwt(token);
        Long userId = loginService.getIdByEmail(userEmail);
        try{

            // 찜한 동네형 목록 조회
            List<Coach> coachList = memberQueryService.getFavoriteCoachList(userId);

            // 찜한 동네형 목록을 사용하여 DTO로 반환
            List<CoachResponseDTO.favoriteCoachDTO> favoriteCoachDTOList = coachList.stream()
                    .map(CoachConverter::toFavoriteCoachDTO)
                    .collect(Collectors.toList());

            // 성공 응답 생성
            ApiResponse<List<CoachResponseDTO.favoriteCoachDTO>> apiResponse = ApiResponse.onSuccess(favoriteCoachDTOList);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

        } catch (Exception e){
            ApiResponse<List<CoachResponseDTO.favoriteCoachDTO>> apiResponse = ApiResponse.onFailure(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);

        }
    }

    @PostMapping("/favorite/{coachId}")
    @Operation(summary = "사용자가 찜한 형 등록하기 API", description = "사용자가 찜하려는 동네형의 아이디를 입력해주세요.")
    public ResponseEntity<ApiResponse<String>> createFavoriteCoach(@RequestHeader(value = "token") String token,
                                                                   @PathVariable(value = "coachId") Long coachId) {

        String userEmail = loginService.decodeJwt(token);
        Long userId = loginService.getIdByEmail(userEmail);

            memberCommandService.createFavoriteCoach(userId, coachId);

            ApiResponse<String> apiResponse = ApiResponse.onSuccess("동네형 찜 등록을 성공했습니다.");
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

    }



    @GetMapping("/reviews")
    @Operation(summary = "사용자가 작성한 후기 리스트 조회 API")
    public ResponseEntity<ApiResponse<List<ReviewResponseDTO.ReviewByUserDTO>>> getReviewsByUser(@RequestHeader(value = "token") String token){

        String userEmail = loginService.decodeJwt(token);
        Long userId = loginService.getIdByEmail(userEmail);
        try {
            ApiResponse<List<ReviewResponseDTO.ReviewByUserDTO>> apiResponse = ApiResponse.onSuccess(reviewService.getReviews(userId));
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }catch (Exception e){
            ApiResponse<List<ReviewResponseDTO.ReviewByUserDTO>> apiResponse = ApiResponse.onFailure(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }
    }

    @PostMapping(value = "/reviews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "사용자가 동네형에게 리뷰를 작성하는 API")
    public ResponseEntity<ApiResponse<String>> createReviews(@RequestPart(value = "request") ReviewRequestDTO.CreateReviewDTO request,
            @RequestPart(value ="files", required = false) List<MultipartFile> files, @RequestHeader(value = "token") String token ){
        String userEmail = loginService.decodeJwt(token);
        Long userId = loginService.getIdByEmail(userEmail);
            if(files != null) { // 리뷰에 이미지가 포함되어 있는 경우
                reviewService.createReviewWithFiles(request, files, userId);
            } else {    // 리뷰에 미지가 포함되어 있지 않은 경우
                reviewService.createReview(request,userId);
            }
            ApiResponse<String> apiResponse = ApiResponse.onSuccess("성공적으로 리뷰 작성을 했습니다.");
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

    }

    @PatchMapping(value = "/{memberId}/sign-up", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "유저 회원가입 완료 후 첫 정보 입력 페이지")
    public ApiResponse<String> coachSignUp(@RequestPart(value = "request") MemberRequestDTO.MemberProfileRegisterDTO request,
                                           @RequestPart(value = "picture", required = false) MultipartFile file,
                                           @PathVariable(value = "memberId") Long memberId){
            if(file != null){   // 사용자가 본인의 이미지를 업로드 하는 경우
                memberCommandService.insertInfoWithImage(memberId, request, file);
            } else {    // 사용자가 본인의 이미지를 업로드 하지 않는 경우
                memberCommandService.insertMemberInfo(memberId, request);
            }
            return ApiResponse.onSuccess("회원의 정보가 성공적으로 입력되었습니다.");

    }
}
