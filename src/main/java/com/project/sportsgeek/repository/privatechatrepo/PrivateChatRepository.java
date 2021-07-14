package com.project.sportsgeek.repository.privatechatrepo;

import com.project.sportsgeek.model.PrivateChat;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "privateChatRepo")
public interface PrivateChatRepository {

    List<PrivateChat> findPrivateChatByUserId(int userId1, int userId2) throws Exception;

    int addPrivateChat(PrivateChat privateChat) throws Exception;

    boolean updatePrivateChat(int privateChatId, PrivateChat privateChat) throws Exception;

    boolean deletePrivateChat(int privateChatId) throws Exception;
    boolean deletePrivateChatByUserId(int userId) throws Exception;

}