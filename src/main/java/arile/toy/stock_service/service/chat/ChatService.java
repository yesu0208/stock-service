package arile.toy.stock_service.service.chat;

import arile.toy.stock_service.domain.chat.Chatroom;
import arile.toy.stock_service.domain.chat.GithubUserChatroomMapping;
import arile.toy.stock_service.domain.GithubUserInfo;
import arile.toy.stock_service.domain.chat.Message;
import arile.toy.stock_service.domain.constant.MessageType;
import arile.toy.stock_service.dto.chatdto.ChatroomDto;
import arile.toy.stock_service.dto.chatdto.ChatroomWithCurrentStockDto;
import arile.toy.stock_service.exception.IllegalClientAccessException;
import arile.toy.stock_service.exception.chat.ChatroomNotFoundException;
import arile.toy.stock_service.exception.user.UserNotFoundException;
import arile.toy.stock_service.repository.GithubUserInfoRepository;
import arile.toy.stock_service.repository.chat.ChatroomRepository;
import arile.toy.stock_service.repository.chat.GithubUserChatroomMappingRepository;
import arile.toy.stock_service.repository.chat.MessageRepository;
import arile.toy.stock_service.service.CurrentStockInfoService;
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

    public ChatroomDto createChatroom(String unchangeableId, String title, String stockName, String createdBy) {

        GithubUserInfo githubUserInfo = githubUserInfoRepository.findById(unchangeableId)
                .orElseThrow(() -> new UserNotFoundException(unchangeableId));

        String newTitle = "[" + stockName + "] " + title;

        Chatroom chatroom = Chatroom.of(newTitle, LocalDateTime.now(ZoneId.of("Asia/Seoul")), stockName, createdBy, unchangeableId);

        chatroom = chatroomRepository.save(chatroom);

        // 채팅방 만든 사람은 바로 입장
        GithubUserChatroomMapping githubUserChatroomMapping = chatroom.addGithubUserInfo(githubUserInfo);

        githubUserChatroomMappingRepository.save(githubUserChatroomMapping);

        return ChatroomDto.fromEntity(chatroom);
    }


    public Boolean joinChatroom(String unchangeableId, Long newChatroomId, Long currentChatroomId) {

        GithubUserInfo githubUserInfo = githubUserInfoRepository.findById(unchangeableId)
                .orElseThrow(() -> new UserNotFoundException(unchangeableId));

        if (currentChatroomId != null) {
            updateLastCheckedAt(githubUserInfo, currentChatroomId);
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


    public void deleteChatroom(String unchangeableId, Long chatroomId) {
        var chatroomDto = chatroomRepository.findById(chatroomId)
                .map(ChatroomDto::fromEntity)
                .orElseThrow(() -> new ChatroomNotFoundException(chatroomId));

        if (!chatroomDto.unchangeableId().equals(unchangeableId)) {
            throw new IllegalClientAccessException();
        }

        chatroomRepository.deleteById(chatroomId);

    }


    public Boolean leaveChatroom(String unchangeableId, Long chatroomId) {

        if(!githubUserChatroomMappingRepository.existsByGithubUserInfoUnchangeableIdAndChatroomChatroomId(
                unchangeableId, chatroomId)) {
            // 참여하지 않은 방
            return false;
        }

        githubUserChatroomMappingRepository.deleteByGithubUserInfoUnchangeableIdAndChatroomChatroomId(unchangeableId, chatroomId);

        return true;
    }


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


    public List<ChatroomDto> getAllChatroomListExceptJoinedChatroom(String unchangeableId) {

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


    public Message saveMessage(String unchangeableId, Long chatroomId, String text, MessageType messageType) {
        Chatroom chatroom = chatroomRepository.findById(chatroomId)
                .orElseThrow(() -> new ChatroomNotFoundException(chatroomId));

        GithubUserInfo githubUserInfo = githubUserInfoRepository.findById(unchangeableId)
                .orElseThrow(() -> new UserNotFoundException(unchangeableId));;

        Message message = Message.of(text, githubUserInfo, chatroom, LocalDateTime.now(ZoneId.of("Asia/Seoul")), messageType);

        return messageRepository.save(message);
    }


    public List<Message> getMessageList(Long chatroomId) {
        return  messageRepository.findAllByChatroomChatroomId(chatroomId);
    }
}
