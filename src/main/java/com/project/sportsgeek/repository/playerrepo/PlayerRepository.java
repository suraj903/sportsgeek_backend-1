package com.project.sportsgeek.repository.playerrepo;

import com.project.sportsgeek.model.Player;
import com.project.sportsgeek.model.PlayerResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "playerRepo")
public interface PlayerRepository {

    List<PlayerResponse> findAllPlayers();

    List<PlayerResponse> findPlayerByPlayerId(int id) throws Exception;

    List<PlayerResponse> findPlayerByPlayerType(int id) throws Exception;

    List<PlayerResponse> findPlayerByTeamId(int id) throws Exception;

    int addPlayer(Player player) throws Exception;

    boolean updatePlayer(int id, Player player) throws Exception;

    boolean updatePlayerType(int id,int playerTypeId) throws Exception;

    int deletePlayer(int id) throws Exception;

}
