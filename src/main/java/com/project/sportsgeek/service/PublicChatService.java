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
            return new Result<>(200, publicChat);
        }
        throw new ResultException(new Result<>(404, "Public Chat with PublicChatId: " + publicChatId + " not found."));
    }

    public Result<PublicChat> addPublicChat(PublicChat publicChat) throws Exception {
        int id = publicChatRepository.addPublicChat(publicChat);
        if (id > 0) {
            publicChat.setPublicChatId(id);
            publicChat.setStatus(true);
            return new Result<>(201, publicChat);
        }
        throw new ResultException(new Result<>(404, "Unable to add Public Chat."));
    }

    public Result<PublicChat> updatePublicChat(int publicChatId, PublicChat publicChat) throws Exception {
        if (publicChatRepository.updatePublicChat(publicChatId,publicChat)) {
            return new Result<>(200, publicChat);
        }
        throw new ResultException(new Result<>(404, "Public Chat with PublicChatId: " + publicChatId + " not found."));
    }

    public Result<String> deletePublicChat(int publicChatId) throws Exception{
        if (publicChatRepository.deletePublicChat(publicChatId)) {
            return new Result<>(200,"Public Chat Deleted Successfully");
        }
        throw new ResultException(new Result<>(404, "Public Chat with PublicChatId: " + publicChatId + " not found."));
    }
}