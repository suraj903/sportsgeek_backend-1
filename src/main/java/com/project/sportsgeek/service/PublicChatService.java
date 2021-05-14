package com.project.sportsgeek.service;

import com.project.sportsgeek.exception.ResultException;
import com.project.sportsgeek.model.PublicChat;
import com.project.sportsgeek.model.PublicChatWithUser;
import com.project.sportsgeek.repository.publicchatrepo.PublicChatRepository;
import com.project.sportsgeek.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PublicChatService {

    @Autowired
    @Qualifier(value = "publicChatRepo")
    PublicChatRepository publicChatRepository;

    public Result<List<PublicChatWithUser>> findAllPublicChat() {
        List<PublicChatWithUser> publicChatList = publicChatRepository.findAllPublicChat();
        return new Result<>(200,"Public Chat Details Retrieved Successfully",publicChatList);
    }

    public Result<PublicChatWithUser> findPublicChatById(int id) throws Exception {
        List<PublicChatWithUser> publicChatList = publicChatRepository.findPublicChatById(id);
        if (publicChatList.size() > 0) {
            return new Result<>(200,"PublicChat Details Retrieved Successfully" ,publicChatList.get(0));
        }
        else {
            throw new ResultException((new Result<>(404,"No Public Chat's found,please try again","Public Chat with id=('"+ id +"') not found")));
        }
    }

    public Result<PublicChat> addPublicChat(PublicChat publicChat) throws Exception {
        int id = publicChatRepository.addPublicChat(publicChat);
        publicChat.setPublicChatId(id);
        publicChat.setStatus(true);
        if (id > 0) {
            return new Result<>(201,"Public Chat Added Successfully", publicChat);
        }
        throw new ResultException(new Result<>(400, "Error!, please try again!", new ArrayList<>(Arrays
                .asList(new Result.SportsGeekSystemError(publicChat.hashCode(), "unable to add the given Public Chat")))));
    }

    public Result<PublicChat> updatePublicChat(int id, PublicChat publicChat) throws Exception {
        if (publicChatRepository.updatePublicChat(id,publicChat)) {
            return new Result<>(201,"Public Chat Details Updated Successfully",publicChat);
        }
        throw new ResultException(new Result<>(400, "Unable to update the given Public Chat details! Please try again!", new ArrayList<>(Arrays
                .asList(new Result.SportsGeekSystemError(publicChat.hashCode(), "given PublicChatId('"+id+"') does not exists")))));
    }

    public Result<Integer> deletePublicChat(int id) throws Exception{
        int data = publicChatRepository.deletePublicChat(id);
        if (data > 0) {
            return new Result<>(200,"Public Chat Deleted Successfully",data);
        }
        else {
            throw new ResultException((new Result<>(404,"No Public Chat found to delete,please try again","PublicChat with id=('"+ id +"') not found")));
        }
    }
}