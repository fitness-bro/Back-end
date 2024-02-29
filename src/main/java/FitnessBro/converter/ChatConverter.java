package FitnessBro.converter;

import FitnessBro.domain.Chat.ChatMessage;
import FitnessBro.domain.Chat.ChatRoom;
import FitnessBro.web.dto.Chat.ChatMessageRequestDTO;
import FitnessBro.web.dto.Chat.ChatMessageResponseDTO;
import FitnessBro.web.dto.Chat.ChatRoomResponseDTO;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class ChatConverter {



    public static ChatRoomResponseDTO.ChatRoomCreateResponseDTO toChatRoomCreateResponseDTO(ChatRoom chatRoom){
        return ChatRoomResponseDTO.ChatRoomCreateResponseDTO.builder()
                .roomId(chatRoom.getId())
                .message(chatRoom.getId() + "번 방 생성이 완료 되었습니다.")
                .build();
    }

    public static ChatMessageRequestDTO toChatMessageDTO(ChatMessage chatMessage){

        return ChatMessageRequestDTO.builder()
                .message(chatMessage.getMessage())
                .sender(chatMessage.getSender())
                .build();
    }

    public static List<ChatMessageRequestDTO> toChatMessageListDTO(List<ChatMessage> chatMessageList){
        return chatMessageList.stream()
                .map(chatMessage -> toChatMessageDTO(chatMessage))
                .collect(Collectors.toList());
    }

    public static ChatRoomResponseDTO.ChatRoomInfoDTO toChatRoomInfoDTO(ChatRoom chatRoom){
        return ChatRoomResponseDTO.ChatRoomInfoDTO.builder()
                .chatRoomId(chatRoom.getId())
                .senderName(chatRoom.getMember().getNickname())
                .partnerName(chatRoom.getCoach().getNickname())
                .pictureUrl(chatRoom.getCoach().getPictureURL())
                .build();
    }

    public static ChatMessage toChatMessage(ChatMessageRequestDTO message, ChatRoom chatRoom){

        ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");

        // 현재 시간 가져오기
        LocalDateTime now = LocalDateTime.now(seoulZoneId);

        return ChatMessage.builder()
                .chatRoom(chatRoom)
                .message(message.getMessage())
                .createdAt(now)
                .sender(message.getSender())
                .build();
    }

    public static ChatRoomResponseDTO.ChatMessageDTO toSimpleChatMessageDTO(ChatMessage chatMessage){
        return ChatRoomResponseDTO.ChatMessageDTO.builder()
                .message(chatMessage.getMessage())
                .sender(chatMessage.getSender())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }

    public static List<ChatRoomResponseDTO.ChatMessageDTO> toSimpleChatMessageListDTO(List<ChatMessage> chatMessageList){
        return chatMessageList.stream()
                .map(chatMessage -> toSimpleChatMessageDTO(chatMessage))
                .collect(Collectors.toList());
    }

    public static ChatRoomResponseDTO.ChatRoomSimpleDTO toMemberChatRoomSimpleDTO(ChatRoom chatRoom){
        List<ChatMessage>chatMessageList = chatRoom.getChatMessage();
        ChatMessage lastChatMessage = chatMessageList.get(chatMessageList.size() - 1);
        LocalDateTime time = lastChatMessage.getCreatedAt();

        return ChatRoomResponseDTO.ChatRoomSimpleDTO.builder()
                .chatRoomId(chatRoom.getId())
                .userId(chatRoom.getMember().getId())
                .userName(chatRoom.getMember().getNickname())
                .partnerName(chatRoom.getCoach().getNickname())
                .chatMessageDTOList(ChatConverter.toSimpleChatMessageListDTO(chatRoom.getChatMessage()))
                .lastChatMessage(chatRoom.getLastChatMessage())
                .lastChatTime(time)
                .pictureUrl(chatRoom.getCoach().getPictureURL())
                .build();
    }

    public static List<ChatRoomResponseDTO.ChatRoomSimpleDTO> toMemberChatRoomSimpleListDTO(List<ChatRoom> chatRoomList){
        return chatRoomList.stream()
                .map(chatRoom -> toMemberChatRoomSimpleDTO(chatRoom))
                .collect(Collectors.toList());
    }

    public static ChatRoomResponseDTO.ChatRoomSimpleDTO toCoachChatRoomSimpleDTO(ChatRoom chatRoom){

        List<ChatMessage>chatMessageList = chatRoom.getChatMessage();
        ChatMessage lastChatMessage = chatMessageList.get(chatMessageList.size() - 1);
        LocalDateTime time = lastChatMessage.getCreatedAt();

        return ChatRoomResponseDTO.ChatRoomSimpleDTO.builder()
                .chatRoomId(chatRoom.getId())
                .userId(chatRoom.getCoach().getId())
                .userName(chatRoom.getCoach().getNickname())
                .partnerName(chatRoom.getMember().getNickname())
                .lastChatMessage(chatRoom.getLastChatMessage())
                .lastChatTime(time)
                .chatMessageDTOList(ChatConverter.toSimpleChatMessageListDTO(chatRoom.getChatMessage()))
                .pictureUrl(chatRoom.getMember().getPictureURL())
                .build();
    }

    public static List<ChatRoomResponseDTO.ChatRoomSimpleDTO> toCoachChatRoomSimpleListDTO(List<ChatRoom> chatRoomList){
        return chatRoomList.stream()
                .map(chatRoom -> toCoachChatRoomSimpleDTO(chatRoom))
                .collect(Collectors.toList());
    }

    public static ChatMessageResponseDTO toChatMessageResponseDTO(ChatMessage chatMessage){
        ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");

        // 현재 시간 가져오기
        LocalDateTime now = LocalDateTime.now(seoulZoneId);
        return ChatMessageResponseDTO.builder()
                .id(chatMessage.getId())
                .message(chatMessage.getMessage())
                .sender(chatMessage.getSender())
                .createdAt(now)
                .build();
    }
}
