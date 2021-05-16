package com.project.sportsgeek.service;

import com.project.sportsgeek.model.MyMatches;
import com.project.sportsgeek.repository.mymatchesrepo.MyMatchesRepository;
import com.project.sportsgeek.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyMatchesService {

    @Autowired
    @Qualifier("myMatchesRepo")
    MyMatchesRepository myMatchesRepository;

    public Result<List<MyMatches>> findUpcomingMatchesByUserId(int userId) throws Exception {
        List<MyMatches> matchesList = myMatchesRepository.findUpcomingContestByUserId(userId);
        return new Result<>(200,"Upcoming Matches Details Retrieved Successfully", matchesList);
//        if (matchesList.size() > 0) {
//            return new Result<>(200,"Upcoming Matches Details Retrieved Successfully", matchesList);
//        }
//        else {
////            throw new ResultException((new Result<>(404,"No Matches's found,please try again","Matches with id=('"+ id +"') not found")));
//            return new Result<>(404, "Matches Not Found");
//        }
    }

    public Result<List<MyMatches>> findLiveMatchesByUserId(int userId) throws Exception {
        List<MyMatches> matchesList = myMatchesRepository.findLiveContestByUserId(userId);
        return new Result<>(200,"Live Matches Details Retrieved Successfully", matchesList);
//        if (matchesList.size() > 0) {
//            return new Result<>(200,"Live Matches Details Retrieved Successfully", matchesList);
//        }
//        else {
////            throw new ResultException((new Result<>(404,"No Matches's found,please try again","Matches with id=('"+ id +"') not found")));
//            return new Result<>(404, "Matches Not Found");
//        }
    }

    public Result<List<MyMatches>> findResultMatchesByUserId(int userId) throws Exception {
        List<MyMatches> matchesList = myMatchesRepository.findResultContestByUserId(userId);
        return new Result<>(200,"Old Matches Result Retrieved Successfully", matchesList);
//        if (matchesList.size() > 0) {
//            return new Result<>(200,"Old Matches Result Retrieved Successfully", matchesList);
//        }
//        else {
////            throw new ResultException((new Result<>(404,"No Matches's found,please try again","Matches with id=('"+ id +"') not found")));
//            return new Result<>(404, "Matches Not Found");
//        }
    }
}
