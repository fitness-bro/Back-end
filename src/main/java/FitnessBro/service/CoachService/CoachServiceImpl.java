package FitnessBro.service.CoachService;


import FitnessBro.domain.coach.Entity.Coach;
import FitnessBro.respository.CoachRepository;
import FitnessBro.respository.GymRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoachServiceImpl implements CoachService{

    private final CoachRepository coachRepository;
    private final GymRepository gymRepository;
    public Coach getCoachById(Long coachId){
        return coachRepository.getById(coachId);
    }



    @Override
    @Transactional
    public List<Coach> getCoachList(){

        List<Coach> coaches = coachRepository.findAll();
        return coaches;
    }

}
