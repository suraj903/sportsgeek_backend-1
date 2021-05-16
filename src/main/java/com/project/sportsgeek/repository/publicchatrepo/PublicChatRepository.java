package com.project.sportsgeek.repository.publicchatrepo;

import com.project.sportsgeek.model.PublicChat;
import com.project.sportsgeek.model.PublicChatWithUser;
import com.project.sportsgeek.model.Venue;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "publicChatRepo")
public interface PublicChatRepository {

    List<PublicChatWithUser> findAllPublicChat();

    PublicChatWithUser findPublicChatById(int publicChatId) throws Exception;

    int addPublicChat(PublicChat publicChat) throws Exception;

    boolean updatePublicChat(int publicChatId, PublicChat publicChat) throws Exception;

    boolean deletePublicChat(int publicChatId) throws Exception;

}
