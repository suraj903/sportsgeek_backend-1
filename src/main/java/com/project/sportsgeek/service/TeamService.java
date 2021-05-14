package com.project.sportsgeek.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.project.sportsgeek.exception.ResultException;
import com.project.sportsgeek.model.Team;
import com.project.sportsgeek.repository.teamrepo.TeamRepository;
import com.project.sportsgeek.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.security.krb5.Credentials;

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
public class TeamService {

    @Autowired
    @Qualifier("teamRepo")
    TeamRepository teamRepository;

    @Autowired
    ImageUploadService imageUploadService;

    public Result<List<Team>> findAllTeam() {
        List<Team> teamList = teamRepository.findAllTeam();
        return new Result<>(200,"Team Details Retrieved Successfully",teamList);
    }

    public Result<Team> findTeamById(int id) throws Exception {
        List<Team> teamList = teamRepository.findTeamById(id);
        if (teamList.size() > 0) {
            return new Result<>(200,"Team Details Retrieved Successfully", teamList.get(0));
        }
        else {
            throw new ResultException((new Result<>(404,"No team's found,please try again","Team with id=('"+ id +"') not found")));
        }
    }

    public Result<Team> addTeam(Team team, MultipartFile multipartFile) throws Exception {
        File file = imageUploadService.uploadImage(multipartFile);
        if (file.toString() != "") {
            String teamlogo = "https://firebasestorage.googleapis.com/v0/b/sportsgeek-74e1e.appspot.com/o/" +file+"?alt=media&token=e9924ea4-c2d9-4782-bc2d-0fe734431c86";
            team.setTeamLogo(teamlogo);
            int id = teamRepository.addTeam(team);
            if (id > 0) {
                return new Result<>(201,"Team Added Successfully",team);
            }
            else {
                throw new ResultException(new Result<>(400, "Error!, please try again!", new ArrayList<>(Arrays
                        .asList(new Result.SportsGeekSystemError(team.hashCode(), "unable to add the given team")))));
            }
        }
        else
        {
            throw new ResultException(new Result<>(400, "Error!, please try again!", new ArrayList<>(Arrays
                    .asList(new Result.SportsGeekSystemError(team.hashCode(), "unable to upload the image")))));
        }

    }
    public Result<Team> updateTeam(int id, Team team,MultipartFile multipartFile) throws Exception {
        File file = imageUploadService.uploadImage(multipartFile);
        if (file.toString() != "")
        {
            String teamlogo = "https://firebasestorage.googleapis.com/v0/b/sportsgeek-74e1e.appspot.com/o/" +file+"?alt=media&token=e9924ea4-c2d9-4782-bc2d-0fe734431c86";
            team.setTeamLogo(teamlogo);
            if (teamRepository.updateTeam(id,team)) {
                return new Result<>(201,"Team Updated Successfully",team);
            }
            else
            {
                throw new ResultException(new Result<>(400, "Unable to update the given team details! Please try again!", new ArrayList<>(Arrays
                        .asList(new Result.SportsGeekSystemError(team.hashCode(), "given teamId('"+id+"') does not exists")))));
            }
        }
        else
        {
            throw new ResultException(new Result<>(400, "Error!, please try again!", new ArrayList<>(Arrays
                    .asList(new Result.SportsGeekSystemError(team.hashCode(), "unable to upload the image")))));
        }
    }
    public Result<Integer> deleteTeam(int id) throws Exception{
        int data = teamRepository.deleteTeam(id);
        if (data > 0) {
            return new Result<>(200,"Team Deleted Successfully",data);
        }
        else {
            throw new ResultException((new Result<>(404,"No Team's found to delete,please try again","Teams with id=('"+ id +"') not found")));
        }
    }
}
