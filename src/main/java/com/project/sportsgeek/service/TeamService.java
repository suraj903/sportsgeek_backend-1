package com.project.sportsgeek.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.project.sportsgeek.exception.ResultException;
import com.project.sportsgeek.model.EmailContact;
import com.project.sportsgeek.model.MobileContact;
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
        return new Result<>(200, teamList);
    }

    public Result<Team> findTeamById(int teamId) throws Exception {
        Team team = teamRepository.findTeamById(teamId);
        if (team != null) {
            return new Result<>(200, team);
        }
        throw new ResultException(new Result<>(404, "Team with TeamId: " + teamId + " not found."));
    }

    public Result<Team> addTeam(Team team, MultipartFile multipartFile) throws Exception {
        // Team Logo
        if(multipartFile != null){
            File file = imageUploadService.uploadImage(multipartFile);
            if (file.toString() != "") {
                String teamLogo = "https://firebasestorage.googleapis.com/v0/b/sportsgeek-74e1e.appspot.com/o/" +file+"?alt=media&token=e9924ea4-c2d9-4782-bc2d-0fe734431c86";
                team.setTeamLogo(teamLogo);
            }
            else
            {
                throw new ResultException(new Result<>(400, "Unable to upload Team Image."));
            }
        }
        else{
            team.setTeamLogo("");
        }
        // Add Team
        int teamId = teamRepository.addTeam(team);
        if (teamId > 0) {
            team.setTeamId(teamId);
            return new Result<>(201, team);
        }else{
            throw new ResultException(new Result<>(400, "Unable to add Team."));
        }
    }

    public Result<Team> updateTeam(int teamId, Team team,MultipartFile multipartFile) throws Exception {
        boolean result = false;
        // Team Logo
        if(multipartFile != null){
            File file = imageUploadService.uploadImage(multipartFile);
            if (file.toString() != "")
            {
                String teamlogo = "https://firebasestorage.googleapis.com/v0/b/sportsgeek-74e1e.appspot.com/o/" +file+"?alt=media&token=e9924ea4-c2d9-4782-bc2d-0fe734431c86";
                team.setTeamLogo(teamlogo);
                result = teamRepository.updateTeamWithLogo(teamId, team);
            }
            else
            {
                throw new ResultException(new Result<>(400, "Unable to upload Team Image."));
            }
        }
        else{
            result = teamRepository.updateTeam(teamId, team);
        }
        // Update Team
        if (result == true) {
            return new Result<>(200,"Team Updated Successfully",team);
        }
        else
        {
            throw new ResultException(new Result<>(404, "Team with TeamId: " + teamId + " not found."));
        }
    }

    public Result<Integer> deleteTeam(int teamId) throws Exception{
        if (teamRepository.deleteTeam(teamId)) {
            return new Result<>(200,"Team Deleted Successfully");
        }
        else {
            throw new ResultException(new Result<>(404, "Team with TeamId: " + teamId + " not found."));
        }
    }
}
