package com.project.sportsgeek.repository.teamrepo;

import com.project.sportsgeek.model.Team;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "teamRepo")
public interface TeamRepository {

    List<Team> findAllTeam();

    List<Team> findTeamById(int i) throws Exception;

    int addTeam(Team team) throws Exception;

    boolean updateTeam(int id, Team team) throws Exception;

    int deleteTeam(int id) throws Exception;

}
