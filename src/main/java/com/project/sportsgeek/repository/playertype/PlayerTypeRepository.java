package com.project.sportsgeek.repository.playertype;

import com.project.sportsgeek.model.PlayerType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "playerTypeRepo")
public interface PlayerTypeRepository {

    List<PlayerType> findAllPlayerType();

    PlayerType findPlayerTypeById(int i) throws Exception;

    int addPlayerType(PlayerType PlayerType) throws Exception;

    boolean updatePlayerType(int id, PlayerType PlayerType) throws Exception;

    int deletePlayerType(int id) throws Exception;

}
