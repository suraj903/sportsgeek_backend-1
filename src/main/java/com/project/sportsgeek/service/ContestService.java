package com.project.sportsgeek.service;

import com.project.sportsgeek.exception.ResultException;
import com.project.sportsgeek.model.*;
import com.project.sportsgeek.repository.contestlogrepo.ContestLogRepository;
import com.project.sportsgeek.repository.contestrepo.ContestRepository;
import com.project.sportsgeek.repository.matchesrepo.MatchesRepository;
import com.project.sportsgeek.repository.userrepo.UserRepository;
import com.project.sportsgeek.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ContestService {
    @Autowired
    @Qualifier("contestRepo")
    ContestRepository contestRepository;
    @Autowired
    @Qualifier("contestLogRepo")
    ContestLogRepository contestLogRepository;
    @Autowired
    @Qualifier("userRepo")
    UserRepository userRepository;
    @Autowired
    @Qualifier("matchesRepo")
    MatchesRepository matchesRepository;

    public Result<List<ContestWithUser>> findContestByMatchId(int matchId) throws Exception {
        List<ContestWithUser> contestList = contestRepository.findAllContestByMatchId(matchId);
        return new Result<>(200, contestList);
    }

    public Result<List<ContestWithResult>> findContestResultByMatchId(int matchId) throws Exception {
        List<ContestWithResult> contestList = contestRepository.findContestResultByMatchId(matchId);
        return new Result<>(200, contestList);
    }

    public Result<Contest> findContestByUserAndMatch(int userId, int matchId) throws Exception {
        Contest contest = contestRepository.findContestByUserAndMatch(userId, matchId);
        if (contest != null) {
            return new Result<>(200, contest);
        }
//        throw new ResultException((new Result<>(404,"Contest for userId: "+ userId +" for matchId: " + matchId + " not found")));
        throw new ResultException((new Result<>(404,"Contest not found. Please place contest")));
    }

    public Result<Contest> addContest(Contest contest) throws Exception {
        // Validation of Match StartTime
        Timestamp matchStartDatetime = matchesRepository.getMatchStartDatetimeById(contest.getMatchId());
        // Get Database Current Timestamp
        Timestamp currentTimestamp = matchesRepository.getCurrentTimestamp();
        if(matchStartDatetime.after(currentTimestamp)){
            int contestId = contestRepository.addContest(contest);
            if (contestId > 0) {
                contest.setContestId(contestId);
                // Deduct User Available Points
                boolean result = userRepository.deductAvailablePoints(contest.getUserId(), contest.getContestPoints());
                // Log Contest in ContestLog Table
                ContestLog contestLog = new ContestLog();
                contestLog.setUserId(contest.getUserId());
                contestLog.setMatchId(contest.getMatchId());
                contestLog.setOldTeamId(null);
                contestLog.setOldContestPoints(null);
                contestLog.setNewTeamId(contest.getTeamId());
                contestLog.setNewContestPoints(contest.getContestPoints());
                contestLog.setAction("INSERT");
                contestLogRepository.addContestLog(contestLog);
                if(result){
                    return new Result<>(201, contest);
                }
                throw new ResultException(new Result<>(500, "Unable to update user available points."));
            }
            throw new ResultException(new Result<>(500, "Unable to add Contest"));
        }
        else{
            throw new ResultException(new Result<>(400, "Contest cannot be placed as the Match has already started."));
        }
    }

    public Result<Contest> updateContest(int contestId, Contest contest) throws Exception {
        // Validation of Match StartTime
        Timestamp matchStartDatetime = matchesRepository.getMatchStartDatetimeById(contest.getMatchId());
        // Get Database Current Timestamp
        Timestamp currentTimestamp = matchesRepository.getCurrentTimestamp();
        if(matchStartDatetime.after(currentTimestamp)) {
            contest.setContestId(contestId);
            // Get old Contest Points
            Contest oldContest = contestRepository.findContestById(contestId);
            if (contestRepository.updateContest(contestId, contest)) {
                // Update User Available Points
                boolean result = userRepository.addAvailablePoints(contest.getUserId(), oldContest.getContestPoints() - contest.getContestPoints());
                // Log Contest in ContestLog Table
                ContestLog contestLog = new ContestLog();
                contestLog.setUserId(contest.getUserId());
                contestLog.setMatchId(contest.getMatchId());
                contestLog.setOldTeamId(oldContest.getTeamId());
                contestLog.setOldContestPoints(oldContest.getContestPoints());
                contestLog.setNewTeamId(contest.getTeamId());
                contestLog.setNewContestPoints(contest.getContestPoints());
                contestLog.setAction("UPDATE");
                contestLogRepository.addContestLog(contestLog);
                if(result){
                    return new Result<>(200, contest);
                }
                throw new ResultException(new Result<>(500, "Unable to update user available points."));
            }
            throw new ResultException(new Result(404, "Contest not found."));
        }else{
            throw new ResultException(new Result<>(400, "Contest cannot be updated as the Match has already started."));
        }
    }

    public Result<String> deleteContest(int contestId) throws Exception{
        // Get Contest details before deleting
        Contest contest = contestRepository.findContestById(contestId);
        if (contestRepository.deleteContestById(contestId)) {
            // Update User Available Points
            boolean result = userRepository.addAvailablePoints(contest.getUserId(), contest.getContestPoints());
            if(result){
                return new Result<>(200, "Contest deleted successfully.");
            }
            throw new ResultException(new Result<>(500, "Unable to update user available points."));
        }
//        throw new ResultException(new Result<>(404, "Contest with ContestId: " + contestId + " not found."));
        throw new ResultException(new Result(404, "Contest not found."));
    }
}