package FitnessBro.service.CoachService;


import FitnessBro.domain.coach.Entity.Coach;
import FitnessBro.domain.gym.Entity.Gym;
import FitnessBro.respository.CoachRepository;
import FitnessBro.respository.GymRepository;
import FitnessBro.web.dto.CoachResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static FitnessBro.converter.CoachConverter.toCoachDTO;

@Service
@RequiredArgsConstructor
public class CoachServiceImpl implements CoachService{

    private final CoachRepository coachRepository;
    private final GymRepository gymRepository;
    public Coach getCoachById(Long coachId){
        return coachRepository.getById(coachId);
    }

//    public List<CoachResponseDTO.CoachDTO> getCoachList(Long gymId){
//
//        List<Coach> coaches = coachRepository.findAllByGym(gymId);
//
//        return coaches.stream()
//                .map(coach -> toCoachDTO(coach)) // toCoachDTO 메서드를 사용하여 Coach를 CoachDTO로 변환
//                .collect(Collectors.toList()); // collect를 사용하여 리스트로 반환.
//    }

    @Override
    @Transactional
    public List<Coach> getCoachList() {

        List<Coach> coaches = coachRepository.findAll();
        return coaches;
    }

    public void addCoach() {
        Coach coach = new Coach();
        coach.setId(1L);
        coach.setAge(3);
        coach.setEmail("awef");
        coach.setAddress("awef");
        coach.setName("awef");
        coach.setNickname("awef");
        coach.setRating(4L);
        coach.setIntroduction("awef");
        coach.setSchedule("awef");
        coach.setPassword("awef");
        coach.setComment("awef");
        coach.setPrice(10000);

        coachRepository.save(coach);
    }

}
