package FitnessBro.service.CoachService;


import FitnessBro.apiPayload.code.status.ErrorStatus;
import FitnessBro.apiPayload.exception.handler.TempHandler;
import FitnessBro.aws.s3.AmazonS3Manager;
import FitnessBro.converter.CoachConverter;
import FitnessBro.converter.ReviewConverter;
import FitnessBro.domain.*;
import FitnessBro.apiPayload.Utill.StringUtils;
import FitnessBro.domain.Coach;
import FitnessBro.domain.common.Uuid;
import FitnessBro.respository.*;

import FitnessBro.service.GymService.GymService;
import FitnessBro.service.RegisterService.RegisterService;
import FitnessBro.web.dto.Coach.CoachRequestDTO;
import FitnessBro.web.dto.Coach.CoachResponseDTO;
import FitnessBro.web.dto.Coach.CoachRequestDTO;
import FitnessBro.web.dto.Coach.CoachRequestDTO;
import lombok.RequiredArgsConstructor;
import org.hibernate.context.TenantIdentifierMismatchException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.*;
import java.util.Optional;
import java.util.stream.Collectors;

import static FitnessBro.converter.CoachConverter.toCoachDTO;
import static FitnessBro.converter.CoachConverter.toCoachMyPageDTO;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CoachServiceImpl implements CoachService{

    private final CoachRepository coachRepository;
    private final CoachImageRepository coachImageRepository;
    private final AmazonS3Manager s3Manager;
    private final UuidRepository uuidRepository;
    private final GymService gymService;

    @Override
    @Transactional
    public Coach getCoachById(Long coachId){
        Coach coach = coachRepository.findById(coachId).orElse(null);

        if(coach == null){
            throw new TempHandler(ErrorStatus.COACH_NOT_FOUND);
        }
        return coach;
    }

    @Override
    @Transactional
    public List<Coach> getCoachList(){

        List<Coach> coaches = coachRepository.findAll();

        return coaches;
    }


    @Override
    @Transactional
    public void insertCoachInfo(Long coachId, CoachRequestDTO.CoachProfileRegisterDTO request){

        if(request.getNickname() == null){
            throw new TempHandler(ErrorStatus.NICKNAME_NOT_EXIST);
        }
        if (request.getIntroduction() == null){
            throw new TempHandler(ErrorStatus.INTRODUCTION_NOT_EXIST);
        }
        if (request.getSchedule() == null){
            throw new TempHandler(ErrorStatus.SCHEDULE_NOT_EXIST);
        }
        if (request.getComment() == null){
            throw new TempHandler(ErrorStatus.COMMENT_NOT_EXIST);
        }

        Coach coach = coachRepository.findById(coachId).orElse(null);
        coach.setNickname(request.getNickname());
        coach.setAge(request.getAge());
        coach.setIntroduction(request.getIntroduction());   // 선생님 소개
        coach.setSchedule(request.getSchedule());   // 주 운동 시간
        coach.setComment(request.getComment());// 한 줄 인사말
        coach.setPrice(request.getPrice());

        String region = request.getRegion();
        String subAddress = request.getSubAddress();
        String detailAddress = request.getDetailAddress();

            if(region != null && subAddress != null && detailAddress != null)
        {
            Gym gym = gymService.getGymByAddress(region, subAddress, detailAddress);
            coach.setAddress(gym.getAddress());
            coach.setRegion(gym.getRegion());
            coach.setSubAddress(gym.getSubAddress());
            coach.setDetailAddress(gym.getDetailAddress());
            coach.setGym(gym);
        }


    }

    @Override
    @Transactional
    public void insertCoachPicture(Long coachId, MultipartFile picture) {   // 동네형 프로필 사진 삽입

        Coach coach = coachRepository.findById(coachId).orElse(null);

        if(coach.getPictureURL() != null){      // 이미 프로필 이미지가 존재하는 경우 AmazonS3에서 지우는 코드
            String coachPictureURL = coach.getPictureURL();
            String savedUuid = coachPictureURL.substring(coachPictureURL.lastIndexOf("/profile/") + "/profile/".length());
            Uuid uuid = uuidRepository.findByUuid(savedUuid);

            s3Manager.deleteFile(s3Manager.generateProfileKeyName(uuid));
            uuidRepository.deleteByUuid(savedUuid);
            coach.setPictureURL(null);
        }

        String uuid = UUID.randomUUID().toString();
        Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());
        String pictureUrl = s3Manager.uploadFile(s3Manager.generateProfileKeyName(savedUuid), picture);

        coach.setPictureURL(pictureUrl);
    }

    @Override
    @Transactional
    public void insertCoachAlbum(Long coachId, List<MultipartFile> pictureList) {   // 동네형 사진첩 삽입

        Coach coach = coachRepository.findById(coachId).orElse(null);

        for(MultipartFile picture : pictureList){   // picture마다 유일한 URL 값 생성
            String uuid = UUID.randomUUID().toString();
            Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());

            String pictureUrl = s3Manager.uploadFile(s3Manager.generateAlbumKeyName(savedUuid), picture);
            coachImageRepository.save(CoachConverter.toCoachAlbum(pictureUrl, coach));
        }
    }

    @Override
    @Transactional
    public void updateCoachAlbum(Long coachId, List<MultipartFile> pictureList, List<String> pictureUrlList) {

        Coach coach = coachRepository.findById(coachId).orElse(null);
        List<CoachImage> coachImageList = coachImageRepository.findByCoachId(coachId);

        if(pictureUrlList != null){
            for(CoachImage coachImage : coachImageList){
                String coachImageUrl = coachImage.getUrl();

                boolean isExist = pictureUrlList.contains(coachImageUrl);

                if(!isExist){   // 주어진 URL 리스트에 DB에 있던 coachImageURL이 없으면 지우는 코드
                    String savedUuid = coachImageUrl.substring(coachImageUrl.lastIndexOf("/album/") + "/album/".length());
                    Uuid uuid = uuidRepository.findByUuid(savedUuid);

                    s3Manager.deleteFile(s3Manager.generateAlbumKeyName(uuid));
                    uuidRepository.deleteByUuid(savedUuid);
                    coachImageRepository.deleteById(coachImage.getId());
                }
            }
        } else {
            for(CoachImage coachImage : coachImageList){
                String pictureUrl = coachImage.getUrl();
                String savedUuid = pictureUrl.substring(pictureUrl.lastIndexOf("/album/") + "/album/".length());
                Uuid uuid = uuidRepository.findByUuid(savedUuid);

                s3Manager.deleteFile(s3Manager.generateAlbumKeyName(uuid));
                uuidRepository.deleteByUuid(savedUuid);
                coachImageRepository.deleteById(coachImage.getId());
            }
        }

        if(pictureList != null){
            for(MultipartFile picture : pictureList){   // picture마다 유일한 URL 값 생성
                String uuid = UUID.randomUUID().toString();
                Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());

                String pictureUrl = s3Manager.uploadFile(s3Manager.generateAlbumKeyName(savedUuid), picture);
                coachImageRepository.save(CoachConverter.toCoachAlbum(pictureUrl, coach));
            }
        }

    }

    @Override
    @Transactional
    public void deleteCoachProfileImage(Long coachId) {

        Coach coach = coachRepository.findById(coachId).orElse(null);

        if(coach.getPictureURL() != null){      // 이미 프로필 이미지가 존재하는 경우 AmazonS3에서 지우는 코드
            String coachPictureURL = coach.getPictureURL();
            String savedUuid = coachPictureURL.substring(coachPictureURL.lastIndexOf("/profile/") + "/profile/".length());
            Uuid uuid = uuidRepository.findByUuid(savedUuid);

            s3Manager.deleteFile(s3Manager.generateProfileKeyName(uuid));
            uuidRepository.deleteByUuid(savedUuid);
            coach.setPictureURL(null);
        }

    }

    @Override
    @Transactional
    public Coach updateCoach(Long coachId, CoachRequestDTO.CoachUpdateRequestDTO coachUpdateRequestDTO){
        Coach coach = coachRepository.findById(coachId)
                .orElseThrow(() -> new RuntimeException("Coach not found"));

        coach.update(coachUpdateRequestDTO);
        coachRepository.save(coach);
        return coach;
    }


}
