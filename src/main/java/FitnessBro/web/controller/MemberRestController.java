package FitnessBro.web.controller;

import FitnessBro.service.MemberService.MemberCommandService;
import FitnessBro.web.dto.MemberRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberRestController {

    private final MemberCommandService memberCommandService;

    @PostMapping("/sign-up")
    public ResponseEntity<String> join(@RequestBody @Valid MemberRequestDTO.JoinDTO request){
        memberCommandService.joinMember(request);
        return ResponseEntity.ok().body("회원가입에 성공했습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid MemberRequestDTO.loginDTO request){
        String token = memberCommandService.login(request.getMemberId(), request.getPassword());

        return ResponseEntity.ok().body(token);
    }

}
