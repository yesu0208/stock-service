package arile.toy.stock_service.service;

import arile.toy.stock_service.domain.Chatroom;
import arile.toy.stock_service.domain.GithubUserChatroomMapping;
import arile.toy.stock_service.domain.GithubUserInfo;
import arile.toy.stock_service.domain.Message;
import arile.toy.stock_service.domain.constant.MessageType;
import arile.toy.stock_service.dto.ChatroomDto;
import arile.toy.stock_service.dto.ChatroomWithCurrentStockDto;
import arile.toy.stock_service.exception.IllegalClientAccessException;
import arile.toy.stock_service.exception.chats.ChatroomNotFoundException;
import arile.toy.stock_service.exception.user.UserNotFoundException;
import arile.toy.stock_service.repository.GithubUserInfoRepository;
import arile.toy.stock_service.repository.chats.ChatroomRepository;
import arile.toy.stock_service.repository.chats.GithubUserChatroomMappingRepository;
import arile.toy.stock_service.repository.chats.MessageRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private final CurrentStockInfoService currentStockInfoService;

    // 채팅방 생성 기능
    public ChatroomDto createChatroom(String unchangeableId, String title, String stockName, String createdBy) {

        GithubUserInfo githubUserInfo = githubUserInfoRepository.findById(unchangeableId)
                .orElseThrow(() -> new UserNotFoundException(unchangeableId));

        String newTitle = "[" + stockName + "] " + title;

        Chatroom chatroom = Chatroom.of(newTitle, LocalDateTime.now(ZoneId.of("Asia/Seoul")), stockName, createdBy, unchangeableId);

        // 부모 먼저 넣고
        chatroom = chatroomRepository.save(chatroom);

        // 채팅방 만든 사람은 바로 입장
        GithubUserChatroomMapping githubUserChatroomMapping = chatroom.addGithubUserInfo(githubUserInfo);

        // 자식 넣고
        githubUserChatroomMappingRepository.save(githubUserChatroomMapping);

        return ChatroomDto.fromEntity(chatroom);
    }


    // 다른 사람이 만든 채팅방에 참여
    public Boolean joinChatroom(String unchangeableId, Long newChatroomId, Long currentChatroomId) {

        GithubUserInfo githubUserInfo = githubUserInfoRepository.findById(unchangeableId)
                .orElseThrow(() -> new UserNotFoundException(unchangeableId));

        if (currentChatroomId != null) { // 기존 채팅방(current)에서 새로운 채팅방(new)으로 이동
            updateLastCheckedAt(githubUserInfo, currentChatroomId); // 기존 채팅방에 LastCheckedAt 정보 setting
        }

        if(githubUserChatroomMappingRepository.existsByGithubUserInfoUnchangeableIdAndChatroomChatroomId(
                unchangeableId,newChatroomId)) {
            // 이미 참여
            return false;
        }


        Chatroom chatroom = chatroomRepository.findById(newChatroomId)
                .orElseThrow(() -> new ChatroomNotFoundException(newChatroomId));

        GithubUserChatroomMapping githubUserChatroomMapping =
                GithubUserChatroomMapping.of(githubUserInfo, chatroom);

        githubUserChatroomMappingRepository.save(githubUserChatroomMapping);

        return true;
    }

    private void updateLastCheckedAt(GithubUserInfo githubUserInfo, Long currentChatroomId) {
        GithubUserChatroomMapping githubUserChatroomMapping =
                githubUserChatroomMappingRepository.findByGithubUserInfoUnchangeableIdAndChatroomChatroomId(
                        githubUserInfo.getUnchangeableId(), currentChatroomId)
                        .orElseThrow(() -> new EntityNotFoundException("githubUserInfo-chatroom mapping not exists."));

        githubUserChatroomMapping.setLastCheckedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));

        githubUserChatroomMappingRepository.save(githubUserChatroomMapping);
    }

    // 단일 채팅방 정보 가져오기
    public ChatroomWithCurrentStockDto getChatroomWithCurrentStock(Long chatroomId) {
        var chatroomDto = chatroomRepository.findById(chatroomId)
                .map(ChatroomDto::fromEntity)
                .orElseThrow(() -> new ChatroomNotFoundException(chatroomId));

        var currentStockInfoDto = currentStockInfoService.getCurrentStockInfo(chatroomDto.stockName());

        return ChatroomWithCurrentStockDto.of(chatroomId, chatroomDto.title(), chatroomDto.memberCount(),
                chatroomDto.createdAt(), chatroomDto.stockName(), chatroomDto.createdBy(), chatroomDto.hasNewMessage(),
                currentStockInfoDto.marketState(), currentStockInfoDto.nowValue(), currentStockInfoDto.changeValue(),
                currentStockInfoDto.changeRate());
    }


    // 단일 채팅방 삭제하기 (생성한 유저만 가능)
    public void deleteChatroom(String unchangeableId, Long chatroomId) {
        var chatroomDto = chatroomRepository.findById(chatroomId)
                .map(ChatroomDto::fromEntity)
                .orElseThrow(() -> new ChatroomNotFoundException(chatroomId));

        if (!chatroomDto.unchangeableId().equals(unchangeableId)) {
            throw new IllegalClientAccessException();
        }

        chatroomRepository.deleteById(chatroomId);

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
                    chatroom.setHasNewMessage(messageRepository.existsByChatroomChatroomIdAndCreatedAtAfter(
                            chatroom.getChatroomId(), githubUserChatroomMapping.getLastCheckedAt()));
                    return chatroom;
                })
                .map(ChatroomDto::fromEntity)
                .toList();
    }


    // 모든 채팅방 목록 조회 (내가 참여한 채팅방 제외)
    public List<ChatroomDto> getAllChatroomListExceptJoined(String unchangeableId) {

        List<Long> joinedChatroomIds = githubUserChatroomMappingRepository
                .findAllByGithubUserInfoUnchangeableId(unchangeableId).stream()
                .map(mapping -> mapping.getChatroom().getChatroomId())
                .toList();

        List<Chatroom> chatrooms = chatroomRepository.findAll().stream()
                .filter(chatroom -> !joinedChatroomIds.contains(chatroom.getChatroomId()))
                .toList();

        return chatrooms.stream()
                .map(ChatroomDto::fromEntity)
                .toList();
    }

    // 메시지 저장
    public Message saveMessage(String unchangeableId, Long chatroomId, String text, MessageType messageType) {
        Chatroom chatroom = chatroomRepository.findById(chatroomId)
                .orElseThrow(() -> new ChatroomNotFoundException(chatroomId));

        // Dto로 변경 필요(사실은)
        GithubUserInfo githubUserInfo = githubUserInfoRepository.findById(unchangeableId)
                .orElseThrow(() -> new UserNotFoundException(unchangeableId));;


        Message message = Message.of(text, githubUserInfo, chatroom, LocalDateTime.now(ZoneId.of("Asia/Seoul")), messageType);

        return messageRepository.save(message);
    }

    // 채팅방의 메시지(목록) 조회
    public List<Message> getMessageList(Long chatroomId) {
        return  messageRepository.findAllByChatroomChatroomId(chatroomId);
    }
}
