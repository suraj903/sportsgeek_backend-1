package com.project.sportsgeek.repository.contestrepo;

import com.project.sportsgeek.model.Contest;
import com.project.sportsgeek.model.ContestWithResult;
import com.project.sportsgeek.model.ContestWithUser;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "betOnTeamsRepo")
public interface ContestRepository {

    List<Contest> findContestByUserAndMatch(int userid, int matchid) throws Exception;

    List<ContestWithUser> findAllContestByMatchId(int matchId) throws Exception;

    List<ContestWithResult> findContestResultByMatchId(int matchId) throws Exception;

    int addContest(Contest contest) throws Exception;

    boolean updateContest(int id, Contest contest) throws Exception;

    int getContestPoints(int betTeamId) throws Exception;

}