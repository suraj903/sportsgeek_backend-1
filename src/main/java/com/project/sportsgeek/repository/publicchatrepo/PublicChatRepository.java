package com.project.sportsgeek.repository.publicchatrepo;

import com.project.sportsgeek.model.PublicChat;
import com.project.sportsgeek.model.PublicChatWithUser;
import com.project.sportsgeek.model.Venue;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "publicChatRepo")
public interface PublicChatRepository {

    List<PublicChatWithUser> findAllPublicChat();

    List<PublicChatWithUser> findPublicChatById(int id) throws Exception;

    int addPublicChat(PublicChat publicChat) throws Exception;

    boolean updatePublicChat(int id, PublicChat publicChat) throws Exception;

    int deletePublicChat(int id) throws Exception;

}
