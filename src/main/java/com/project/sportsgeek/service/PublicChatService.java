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

    public Result<PublicChatWithUser> findPublicChatById(int publicChatId) throws Exception {
        PublicChatWithUser publicChat = publicChatRepository.findPublicChatById(publicChatId);
        if (publicChat != null) {
            return new Result<>(200,"PublicChat Details Retrieved Successfully" ,publicChat);
        }
        else {
            throw new ResultException((new Result<>(404,"No Public Chat's found,please try again","Public Chat with id=('"+ publicChatId +"') not found")));
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

    public Result<PublicChat> updatePublicChat(int publicChatId, PublicChat publicChat) throws Exception {
        if (publicChatRepository.updatePublicChat(publicChatId,publicChat)) {
            return new Result<>(200,"Public Chat Details Updated Successfully",publicChat);
        }
        throw new ResultException(new Result<>(400, "Unable to update the given Public Chat details! Please try again!", new ArrayList<>(Arrays
                .asList(new Result.SportsGeekSystemError(publicChat.hashCode(), "given PublicChatId('"+publicChatId+"') does not exists")))));
    }

    public Result<Integer> deletePublicChat(int publicChatId) throws Exception{
        if (publicChatRepository.deletePublicChat(publicChatId)) {
            return new Result<>(200,"Public Chat Deleted Successfully");
        }
        else {
            throw new ResultException((new Result<>(404,"No Public Chat found to delete,please try again","PublicChat with id=('"+ publicChatId +"') not found")));
        }
    }
}