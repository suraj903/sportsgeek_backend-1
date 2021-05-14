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

    public Result<List<MyMatches>> findUpcomingMatchesByUserId(int id) throws Exception {
        List<MyMatches> matchesList = myMatchesRepository.findUpcomingContestByUserId(id);
        if (matchesList.size() > 0) {
            return new Result<>(200,"Upcoming Matches Details Retrieved Successfully", matchesList);
        }
        else {
//            throw new ResultException((new Result<>(404,"No Matches's found,please try again","Matches with id=('"+ id +"') not found")));
            return new Result<>(404, "Matches Not Found");
        }
    }

    public Result<List<MyMatches>> findLiveMatchesByUserId(int id) throws Exception {
        List<MyMatches> matchesList = myMatchesRepository.findLiveContestByUserId(id);
        if (matchesList.size() > 0) {
            return new Result<>(200,"Live Matches Details Retrieved Successfully", matchesList);
        }
        else {
//            throw new ResultException((new Result<>(404,"No Matches's found,please try again","Matches with id=('"+ id +"') not found")));
            return new Result<>(404, "Matches Not Found");
        }
    }

    public Result<List<MyMatches>> findResultMatchesByUserId(int id) throws Exception {
        List<MyMatches> matchesList = myMatchesRepository.findResultContestByUserId(id);
        if (matchesList.size() > 0) {
            return new Result<>(200,"Old Matches Result Retrieved Successfully", matchesList);
        }
        else {
//            throw new ResultException((new Result<>(404,"No Matches's found,please try again","Matches with id=('"+ id +"') not found")));
            return new Result<>(404, "Matches Not Found");
        }
    }
}
