package com.project.sportsgeek.repository.contestlogrepo;

import com.project.sportsgeek.model.ContestLog;
import org.springframework.stereotype.Repository;

@Repository(value = "contestLogRepo")
public interface ContestLogRepository {

    int addContestLog(ContestLog contestLog) throws Exception;
}
