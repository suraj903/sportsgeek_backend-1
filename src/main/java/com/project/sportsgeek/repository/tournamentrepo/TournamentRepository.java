package com.project.sportsgeek.repository.tournamentrepo;

import com.project.sportsgeek.model.Tournament;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "tournamentRepo")
public interface TournamentRepository {

    List<Tournament> findAllTournament();

    List<Tournament> findTournamentById(int i) throws  Exception;

    List<Tournament> findTournamentByActive() throws Exception;

    int addTournament(Tournament tournament) throws Exception;

    boolean updateTournament(int id, Tournament tournament) throws Exception;

    boolean updateActiveTournament(int id) throws Exception;

    boolean deactivateTournament() throws Exception;

    int deleteTournament(int id) throws Exception;

}
