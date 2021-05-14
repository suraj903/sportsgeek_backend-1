package com.project.sportsgeek.repository.privatechatrepo;

import com.project.sportsgeek.model.PrivateChat;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "privateChatRepo")
public interface PrivateChatRepository {

    List<PrivateChat> findPrivateChatByUserId(int userid1, int userid2) throws Exception;

    int addPrivateChat(PrivateChat privateChat) throws Exception;

    boolean updatePrivateChat(int id, PrivateChat privateChat) throws Exception;

    int deletePrivateChat(int id) throws Exception;

}