package arile.toy.stock_service.service;

import arile.toy.stock_service.domain.Chatroom;
import arile.toy.stock_service.domain.GithubUserChatroomMapping;
import arile.toy.stock_service.domain.GithubUserInfo;
import arile.toy.stock_service.domain.Message;
import arile.toy.stock_service.dto.ChatroomDto;
import arile.toy.stock_service.exception.chats.ChatroomNotFoundException;
import arile.toy.stock_service.exception.user.UserNotFoundException;
import arile.toy.stock_service.repository.GithubUserInfoRepository;
import arile.toy.stock_service.repository.chats.ChatroomRepository;
import arile.toy.stock_service.repository.chats.GithubUserChatroomMappingRepository;
import arile.toy.stock_service.repository.chats.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatService {

    private final ChatroomRepository chatroomRepository;
    private final GithubUserChatroomMappingRepository githubUserChatroomMappingRepository;
    private final MessageRepository messageRepository;

    private final GithubUserInfoRepository githubUserInfoRepository;

    // 채팅방 생성 기능
    public ChatroomDto createChatroom(String unchangeableId, String title) {

        // Dto로 변경 필요(사실은)
        GithubUserInfo githubUserInfo = githubUserInfoRepository.findById(unchangeableId)
                .orElseThrow(() -> new UserNotFoundException(unchangeableId));;

        Chatroom chatroom = Chatroom.of(title, LocalDateTime.now(ZoneId.of("Asia/Seoul")));

        // 부모 먼저 넣고
        chatroom = chatroomRepository.save(chatroom);

        // 채팅방 만든 사람은 바로 입장
        GithubUserChatroomMapping githubUserChatroomMapping = chatroom.addGithubUserInfo(githubUserInfo);

        // 자식 넣고
        githubUserChatroomMappingRepository.save(githubUserChatroomMapping);

        return ChatroomDto.fromEntity(chatroom);
    }


    // 다른 사람이 만든 채팅방에 참여
    public Boolean joinChatroom(String unchangeableId, Long chatroomId) {

        if(githubUserChatroomMappingRepository.existsByGithubUserInfoUnchangeableIdAndChatroomChatroomId(
                unchangeableId,chatroomId)) {
            // 이미 참여
            return false;
        }

        // Dto로 변경 필요(사실은)
        GithubUserInfo githubUserInfo = githubUserInfoRepository.findById(unchangeableId)
                .orElseThrow(() -> new UserNotFoundException(unchangeableId));;

        Chatroom chatroom = chatroomRepository.findById(chatroomId)
                .orElseThrow(() -> new ChatroomNotFoundException(chatroomId));

        GithubUserChatroomMapping githubUserChatroomMapping =
                GithubUserChatroomMapping.of(githubUserInfo, chatroom);

        githubUserChatroomMappingRepository.save(githubUserChatroomMapping);

        return true;
    }

    // 참여한 방에서 나오기
    public Boolean leaveChatroom(String unchangeableId, Long chatroomId) {

        if(!githubUserChatroomMappingRepository.existsByGithubUserInfoUnchangeableIdAndChatroomChatroomId(
                unchangeableId, chatroomId)) {
            // 참여하지 않은 방
            return false;
        }

        githubUserChatroomMappingRepository.deleteByGithubUserInfoUnchangeableIdAndChatroomChatroomId(unchangeableId, chatroomId);

        return true;
    }


    // 사용자 참여 채팅방 목록 조회
    public List<ChatroomDto> getChatroomList(String unchangeableId) {
        List<GithubUserChatroomMapping> githubUserChatroomMappings =
                githubUserChatroomMappingRepository.findAllByGithubUserInfoUnchangeableId(unchangeableId);

        return githubUserChatroomMappings.stream()
                .map(githubUserChatroomMapping -> {
                    Chatroom chatroom = githubUserChatroomMapping.getChatroom();
                    chatroom.setHasNewMessage(messageRepository.existsByChatroomChatIdAndCreatedAtAfter(
                            chatroom.getChatroomId(), githubUserChatroomMapping.getLastCheckedAt()));
                    return chatroom;
                })
                .map(ChatroomDto::fromEntity)
                .toList();
    }

    // 메시지 저장
    public Message saveMessage(String unchangeableId, Long chatroomId, String text) {
        Chatroom chatroom = chatroomRepository.findById(chatroomId)
                .orElseThrow(() -> new ChatroomNotFoundException(chatroomId));

        // Dto로 변경 필요(사실은)
        GithubUserInfo githubUserInfo = githubUserInfoRepository.findById(unchangeableId)
                .orElseThrow(() -> new UserNotFoundException(unchangeableId));;


        Message message = Message.of(text, githubUserInfo, chatroom, LocalDateTime.now(ZoneId.of("Asia/Seoul")));

        return messageRepository.save(message);
    }

    // 채팅방의 메시지(목록) 조회
    public List<Message> getMessageList(Long chatroomId) {
        return  messageRepository.findAllByChatroomChatroomId(chatroomId);
    }
}
