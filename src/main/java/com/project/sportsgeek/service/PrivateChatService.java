package com.project.sportsgeek.service;

import com.project.sportsgeek.exception.ResultException;
import com.project.sportsgeek.model.PrivateChat;
import com.project.sportsgeek.repository.privatechatrepo.PrivateChatRepository;
import com.project.sportsgeek.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PrivateChatService {
    @Autowired
    @Qualifier(value = "privateChatRepo")
    PrivateChatRepository privateChatRepository;

    public Result<List<PrivateChat>> findPrivateChatByUserId(int userId1, int userId2) throws Exception {
        List<PrivateChat> privateChatList = privateChatRepository.findPrivateChatByUserId(userId1, userId2);
        return new Result<>(200, privateChatList);
    }

    public Result<PrivateChat> addPrivateChat(PrivateChat privateChat) throws Exception {
        int id = privateChatRepository.addPrivateChat(privateChat);
        if (id > 0) {
            privateChat.setPrivateChatId(id);
            return new Result<>(201, privateChat);
        }
        throw new ResultException(new Result<>(400, "Unable to add Private Chat."));
    }

    public Result<PrivateChat> updatePrivateChat(int privateChatId, PrivateChat privateChat) throws Exception {
        if (privateChatRepository.updatePrivateChat(privateChatId, privateChat)) {
            return new Result<>(200, privateChat);
        }
        throw new ResultException(new Result<>(404, "Private Chat with PrivateChatId: " + privateChatId + " not found."));
    }

    public Result<String> deletePrivateChat(int privateChatId) throws Exception{
        if (privateChatRepository.deletePrivateChat(privateChatId)) {
            return new Result<>(200,"Private Chat Deleted Successfully");
        }
        throw new ResultException(new Result<>(404, "Private Chat with PrivateChatId: " + privateChatId + " not found."));
    }
}