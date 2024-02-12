package FitnessBro.web.dto.Chat;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;


public class ChatRoomResponseDTO {

    @Builder
    @Getter
    public static class ChatRoomCreateResponseDTO{
        Long roomId;
        String message;
    }


    @Builder
    @Getter
    public static class ChatRoomInfoDTO{

            private Long chatRoomId;
            private List<ChatMessageRequestDTO> latestChatMessages;
            private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    public static class ChatRoomSimpleDTO{
        private Long chatRoomId;
        private String lastChatMessage;
        private LocalDateTime updatedAt;
        private String partnerName;
        private String pictureUrl;
    }
}
