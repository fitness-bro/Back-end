package FitnessBro.web.controller;

import FitnessBro.apiPayload.ApiResponse;
import FitnessBro.converter.GymConverter;
import FitnessBro.domain.Gym;
import FitnessBro.service.GymService.GymService;
import FitnessBro.web.dto.Gym.GymResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gym")
@RequiredArgsConstructor
public class GymController {

    private final GymService gymService;

    @GetMapping("/search")
    @Operation(summary = "헬스장 검색어 넣었을 때 반환해주는 API")
    public ResponseEntity<ApiResponse<List<GymResponseDTO.GymListDTO>>> search(@RequestParam(value = "keyword") String keyword){

        try {
            List<Gym> gyms = gymService.getGymListByKeyWord(keyword);

            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.onSuccess(GymConverter.toGymListDTOS(gyms)));

        } catch (Exception e){
            ApiResponse<List<GymResponseDTO.GymListDTO>> apiResponse = ApiResponse.onFailure(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }

    }

}
