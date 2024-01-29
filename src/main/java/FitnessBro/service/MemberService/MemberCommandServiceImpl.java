package FitnessBro.service.MemberService;

import FitnessBro.converter.MemberConverter;
import FitnessBro.domain.member.Entity.Member;
import FitnessBro.respository.MemberRepository;
import FitnessBro.web.dto.MemberRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Member joinUser(MemberRequestDTO.JoinDTO request) {

        Member newUser = MemberConverter.toMember(request);

        return memberRepository.save(newUser);
    }
}
