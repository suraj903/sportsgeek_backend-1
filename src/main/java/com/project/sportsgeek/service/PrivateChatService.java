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
        return new Result<>(200,"Private Chat Details Retrieved Successfully",privateChatList);
    }

    public Result<PrivateChat> addPrivateChat(PrivateChat privateChat) throws Exception {
        int id = privateChatRepository.addPrivateChat(privateChat);
        privateChat.setPrivateChatId(id);
        if (id > 0) {
            return new Result<>(201,"Private Chat Added Successfully", privateChat);
        }
        throw new ResultException(new Result<>(400, "Error!, please try again!", new ArrayList<>(Arrays
                .asList(new Result.SportsGeekSystemError(privateChat.hashCode(), "unable to add the given Private Chat")))));
    }

    public Result<PrivateChat> updatePrivateChat(int privateChatId, PrivateChat privateChat) throws Exception {
        if (privateChatRepository.updatePrivateChat(privateChatId, privateChat)) {
            return new Result<>(201,"Private Chat Details Updated Successfully", privateChat);
        }
        throw new ResultException(new Result<>(400, "Unable to update the given Private Chat details! Please try again!", new ArrayList<>(Arrays
                .asList(new Result.SportsGeekSystemError(privateChat.hashCode(), "given PrivateChatId('"+privateChatId+"') does not exists")))));
    }

    public Result<Integer> deletePrivateChat(int privateChatId) throws Exception{
        if (privateChatRepository.deletePrivateChat(privateChatId)) {
            return new Result<>(200,"Private Chat Deleted Successfully");
        }
        else {
            throw new ResultException((new Result<>(404,"No Private Chat found to delete,please try again","PrivateChat with id=('"+ privateChatId +"') not found")));
        }
    }
}