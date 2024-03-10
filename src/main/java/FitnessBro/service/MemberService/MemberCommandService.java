package FitnessBro.service.MemberService;


import FitnessBro.domain.Coach;
import FitnessBro.domain.Favorites;
import FitnessBro.domain.Member;
import FitnessBro.web.dto.Login.Role;
import FitnessBro.web.dto.Member.MemberRequestDTO;
import org.springframework.web.multipart.MultipartFile;

public interface MemberCommandService {
    Member getMemberById(Long memberId);

    String joinMember(MemberRequestDTO.JoinDTO request);

    String login(String email, String password);

    public Member updateMember(Long memberId, MemberRequestDTO.MemberUpdateRequestDTO memberUpdateRequestDTO);

    public String joinSocialMember(String email, String id);

    String createFavoriteCoach(Long userId, Long coachId);

    public Boolean favoritesByMember(Long userId, Coach coach);


    public String classifyUsers(String Email, Role role);

    public void insertMemberInfo(Long memberId, MemberRequestDTO.MemberProfileRegisterDTO request);

    void insertInfoWithImage(Long memberId, MemberRequestDTO.MemberProfileRegisterDTO request, MultipartFile file);

    void deleteMemberProfileImage(Long memberId);

    public boolean isUser(String email, String id);
}

