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

    public Result<List<PrivateChat>> findPrivateChatByUserId(int userid1, int userid2) throws Exception {
        List<PrivateChat> privateChatList = privateChatRepository.findPrivateChatByUserId(userid1, userid2);
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

    public Result<PrivateChat> updatePrivateChat(int id, PrivateChat privateChat) throws Exception {
        if (privateChatRepository.updatePrivateChat(id,privateChat)) {
            return new Result<>(201,"Private Chat Details Updated Successfully", privateChat);
        }
        throw new ResultException(new Result<>(400, "Unable to update the given Private Chat details! Please try again!", new ArrayList<>(Arrays
                .asList(new Result.SportsGeekSystemError(privateChat.hashCode(), "given PrivateChatId('"+id+"') does not exists")))));
    }

    public Result<Integer> deletePrivateChat(int id) throws Exception{
        int data = privateChatRepository.deletePrivateChat(id);
        if (data > 0) {
            return new Result<>(200,"Private Chat Deleted Successfully",data);
        }
        else {
            throw new ResultException((new Result<>(404,"No Private Chat found to delete,please try again","PrivateChat with id=('"+ id +"') not found")));
        }
    }
}