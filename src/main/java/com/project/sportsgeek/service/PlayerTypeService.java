package com.project.sportsgeek.service;


import com.project.sportsgeek.exception.ResultException;
import com.project.sportsgeek.model.PlayerType;
import com.project.sportsgeek.repository.playertype.PlayerTypeRepository;
import com.project.sportsgeek.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PlayerTypeService {

    @Autowired
    @Qualifier("playerTypeRepo")
    PlayerTypeRepository playerTypeRepository;


    public Result<List<PlayerType>> findAllPlayerType() {
        List<PlayerType> playerTypeList = playerTypeRepository.findAllPlayerType();
        return new Result<>(200,playerTypeList);
    }

    public Result<PlayerType> findPlayerTypeById(int id) throws Exception {
        List<PlayerType> playerTypeList = playerTypeRepository.findPlayerTypeById(id);
        if (playerTypeList.size() > 0) {
            return new Result<>(200, playerTypeList.get(0));
        }
        else {
            throw new ResultException((new Result<>(404,"No PlayerType's found,please try again","PlayerType with id=('"+ id +"') not found")));
        }
    }

    public Result<PlayerType> addPlayerType(PlayerType playerType) throws Exception {
        int id = playerTypeRepository.addPlayerType(playerType);
        playerType.setPlayerTypeId(id);
        if (id > 0) {
            return new Result<>(201,playerType);
        }
        throw new ResultException(new Result<>(400, "Error!, please try again!", new ArrayList<>(Arrays
                .asList(new Result.SportsGeekSystemError(playerType.hashCode(), "unable to add the given PlayerType")))));
    }
    public Result<PlayerType> updatePlayerType(int id, PlayerType playerType) throws Exception {
        if (playerTypeRepository.updatePlayerType(id,playerType)) {
            return new Result<>(201,playerType);
        }
        throw new ResultException(new Result<>(400, "Unable to update the given PlayerType details! Please try again!", new ArrayList<>(Arrays
                .asList(new Result.SportsGeekSystemError(playerType.hashCode(), "given PlayerTypeId('"+id+"') does not exists")))));
    }
    public Result<Integer> deletePlayerType(int id) throws Exception{
        int data = playerTypeRepository.deletePlayerType(id);
        if (data > 0) {
            return new Result<>(200,data);
        }
        else {
            throw new ResultException((new Result<>(404,"No PlayerType's found to delete,please try again","PlayerType with id=('"+ id +"') not found")));
        }
    }
}
