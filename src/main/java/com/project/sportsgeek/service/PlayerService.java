package com.project.sportsgeek.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.project.sportsgeek.exception.ResultException;
import com.project.sportsgeek.model.Player;
import com.project.sportsgeek.model.PlayerResponse;
import com.project.sportsgeek.model.Venue;
import com.project.sportsgeek.repository.playerrepo.PlayerRepository;
import com.project.sportsgeek.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class PlayerService {

    @Autowired
    @Qualifier(value = "playerRepo")
    PlayerRepository playerRepository;
   @Autowired
   ImageUploadService imageUploadService;
    public Result<List<PlayerResponse>> findAllPlayer() {
        List<PlayerResponse> playerList = playerRepository.findAllPlayers();
        return new Result<>(200,"Venue Details Retrieved Successfully",playerList);
    }

    public Result<PlayerResponse> findPlayerById(int id) throws Exception {
        List<PlayerResponse> playerList = playerRepository.findPlayerByPlayerId(id);
        if (playerList.size() > 0) {
            return new Result<>(200,"Player Details Retrieved Successfully" ,playerList.get(0));
        }
        else {
            return new Result<>(404,"No Player's found with Player id=('"+id+"'),please try again");
        }
    }
    public Result<List<PlayerResponse>> findPlayerByPlayerType(int id) throws Exception {
        List<PlayerResponse> playerList = playerRepository.findPlayerByPlayerType(id);
        if (playerList.size() > 0) {
            return new Result<>(200,"Player's Details Retrieved Successfully" ,playerList);
        }
        else {
            return new Result<>(404,"No Player's found with PlayerType Id=('"+id+"'),please try again");
        }
    }
    public Result<List<PlayerResponse>> findPlayerByTeamId(int id) throws Exception {
        List<PlayerResponse> playerList = playerRepository.findPlayerByTeamId(id);
        if (playerList.size() > 0) {
            return new Result<>(200,"Player's Details Retrieved Successfully" ,playerList);
        }
        else {
            return new Result<>(404,"No Player's found with Team Id=('"+id+"'),please try again");
        }
    }
    public Result<Player> addPlayer(Player player, MultipartFile multipartFile) throws Exception {
        File file = imageUploadService.uploadImage(multipartFile);
        if (file.toString() != "") {
            String playerlogo = "https://firebasestorage.googleapis.com/v0/b/sportsgeek-74e1e.appspot.com/o/" +file+"?alt=media&token=e9924ea4-c2d9-4782-bc2d-0fe734431c86";
            player.setProfilePicture(playerlogo);
            int id = playerRepository.addPlayer(player);
            player.setPlayerId(id);
            if (id > 0) {
                return new Result<>(201,"Player Details Added Successfully",player);
            }
            else
            {
                throw new ResultException(new Result<>(400, "Error!, please try again!", new ArrayList<>(Arrays
                        .asList(new Result.SportsGeekSystemError(player.hashCode(), "unable to add the given Player")))));
            }
        }
        else {
            throw new ResultException(new Result<>(400, "Error!, please try again!", new ArrayList<>(Arrays
                    .asList(new Result.SportsGeekSystemError(player.hashCode(), "unable to upload the image")))));
        }
    }
    public Result<Player> updatePlayer(int id, Player player, MultipartFile multipartFile) throws Exception {
        File file = imageUploadService.uploadImage(multipartFile);
        if (file.toString() != "") {
            String playerlogo = "https://firebasestorage.googleapis.com/v0/b/sportsgeek-74e1e.appspot.com/o/" + file + "?alt=media&token=e9924ea4-c2d9-4782-bc2d-0fe734431c86";
            player.setProfilePicture(playerlogo);
            if (playerRepository.updatePlayer(id, player)) {
                return new Result<>(201, "Player Details Updated Successfully", player);
            } else {
                throw new ResultException(new Result<>(400, "Unable to update the given Player details! Please try again!", new ArrayList<>(Arrays
                        .asList(new Result.SportsGeekSystemError(player.hashCode(), "given PlayerId('" + id + "') does not exists")))));
            }
        }
        else {
            throw new ResultException(new Result<>(400, "Error!, please try again!", new ArrayList<>(Arrays
                    .asList(new Result.SportsGeekSystemError(player.hashCode(), "unable to upload the image")))));
        }
    }
    public Result<String> updatePlayerType(int id, int PlayerTypeId) throws Exception {
        if (playerRepository.updatePlayerType(id, PlayerTypeId)) {
            return new Result<>(201,"PlayerType Updated Successfully");
        }
            return new Result<>(400,"Couldn't Update PlayerType with id='"+id+"'");
    }
    public Result<Integer> deletePlayer(int id) throws Exception{
        int data = playerRepository.deletePlayer(id);
        if (data > 0) {
            return new Result<>(200,"Player Deleted Successfully",data);
        }
        else {
            throw new ResultException((new Result<>(404,"No Player found to delete,please try again","Player with id=('"+ id +"') not found")));
        }
    }
}
