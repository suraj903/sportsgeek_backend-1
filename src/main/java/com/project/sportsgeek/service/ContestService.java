package com.project.sportsgeek.service;

import com.project.sportsgeek.exception.ResultException;
import com.project.sportsgeek.model.Contest;
import com.project.sportsgeek.model.ContestWithResult;
import com.project.sportsgeek.model.ContestWithUser;
import com.project.sportsgeek.model.Recharge;
import com.project.sportsgeek.repository.contestrepo.ContestRepository;
import com.project.sportsgeek.repository.userrepo.UserRepository;
import com.project.sportsgeek.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContestService {
    @Autowired
    @Qualifier("contestRepo")
    ContestRepository contestRepository;
    @Autowired
    @Qualifier("userRepo")
    UserRepository userRepository;

    public Result<Contest> addContest(Contest contest) throws Exception {
        int contestId = contestRepository.addContest(contest);
        if (contestId > 0) {
            contest.setContestId(contestId);
            boolean result = userRepository.deductAvailablePoints(contest.getUserId(), contest.getContestPoints());
            if(result){
                return new Result<>(201, contest);
            }
            throw new ResultException(new Result<>(500, "Unable to update user available points."));
        }
        throw new ResultException(new Result<>(500, "Unable to add Contest"));
    }

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
        throw new ResultException((new Result<>(404,"Contest for userId: "+ userId +" for matchId: " + matchId + " not found")));
    }

    public Result<Contest> updateContest(int contestId, Contest contest) throws Exception {
        contest.setContestId(contestId);
        // Get old Contest Points
        int oldContestPoints = contestRepository.getContestPoints(contestId);
        if (contestRepository.updateContest(contestId, contest)) {
            // Update User Available Points
            boolean result = userRepository.addAvailablePoints(contest.getUserId(), oldContestPoints- contest.getContestPoints());
            if(result){
                return new Result<>(200, contest);
            }
            throw new ResultException(new Result<>(500, "Unable to update user available points."));
        }
        throw new ResultException(new Result(404, "Contest with ContestId: " + contestId + " not found."));
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
        throw new ResultException(new Result<>(404, "Contest with ContestId: " + contestId + " not found."));
    }
}