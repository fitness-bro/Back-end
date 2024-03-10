package FitnessBro.converter;

import FitnessBro.web.dto.Login.Role;
import FitnessBro.web.dto.LoginDTO;

public class LoginConverter {

    public static LoginDTO loginDTO(String userToken, Long userId, Role role, Boolean isUser){
        return LoginDTO.builder()
                .userId(userId)
                .userToken(userToken)
                .role(role)
                .isUser(isUser)
                .build();
    }
}
