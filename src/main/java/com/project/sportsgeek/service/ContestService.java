package com.project.sportsgeek.service;

import com.project.sportsgeek.exception.ResultException;
import com.project.sportsgeek.model.Contest;
import com.project.sportsgeek.model.ContestWithResult;
import com.project.sportsgeek.model.ContestWithUser;
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
        int id = contestRepository.addContest(contest);
        contest.setContestId(id);
        if (id > 0) {
            int n = userRepository.deductAvailablePoints(contest.getUserId(), contest.getContestPoints());
            if(n > 0){
                return new Result<>(201, contest);
            }
            else{
                return new Result<>(500, "Unable to update user available points.");
            }
        }
        return new Result<>(500, "Unable to add Contest");
//        throw new ResultException(new Result<>(400, "Error!, please try again!", new ArrayList<>(Arrays.asList(new Result.SportsGeekSystemError(contest.hashCode(), "unable to add the given Contest")))));
    }

    public Result<List<ContestWithUser>> findContestByMatchId(int matchId) throws Exception {
//	    System.out.println("Service : " + id);
        List<ContestWithUser> contestList = contestRepository.findAllContestByMatchId(matchId);
        return new Result<>(200, contestList);
//        if (contestList.size() > 0) {
//            return new Result<>(200, contestList);
//        }
//        else {
//            return new Result(404,"No Contest's found,please try again","Match with id=('"+ contestId +"') not found");
//        }
    }

    public Result<List<ContestWithResult>> findContestResultByMatchId(int matchId) throws Exception {
//	    System.out.println("Service : " + id);
        List<ContestWithResult> contestList = contestRepository.findContestResultByMatchId(matchId);
        return new Result<>(200, contestList);
//        if (contestList.size() > 0) {
//            return new Result<>(200, contestList);
//        }
//        else {
//            return new Result(404,"No Contest's found,please try again","Match with id=('"+ matchId +"') not found");
//        }
    }
    public Result<Contest> findContestByUserAndMatch(int userId, int matchId) throws Exception {
        Contest contest = contestRepository.findContestByUserAndMatch(userId, matchId);
        if (contest != null) {
            return new Result<>(200, contest);
        }
        else {
//            throw new ResultException((new Result<>(404,"No Contest's found,please try again","Contest with id=('"+ userid +"') not found")));
//            return new Result<>(404, "No Data Found!");
            throw new ResultException((new Result<>(404,"No Contest's found,please try again","Contest for userId='"+ userId +"' for matchId='" + matchId + "' not found")));
        }
    }
    public Result<Contest> updateContest(int contestId, Contest contest) throws Exception {
        contest.setContestId(contestId);
        // Get old Contest Points
        int oldContestPoints = contestRepository.getContestPoints(contestId);
        if (contestRepository.updateContest(contestId, contest)) {
            // Update User Available Points
            int n = userRepository.addAvailablePoints(contest.getUserId(), oldContestPoints- contest.getContestPoints());
            if(n > 0){
                return new Result<>(200, contest);
            }
            else{
                return new Result<>(500, "Unable to update user available points.");
            }
        }
//        throw new ResultException(new Result<>(400, "Unable to update the given gender details! Please try again!", new ArrayList<>(Arrays.asList(new Result.SportsGeekSystemError(contest.hashCode(), "given genderId('"+id+"') does not exists")))));
        return new Result<>(500,"Not Found");
    }
}