package com.project.sportsgeek.repository.tournamentrepo;

import com.project.sportsgeek.mapper.TournamentRowMapper;
import com.project.sportsgeek.model.Tournament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "tournamentRepo")
public class TournamentRepositoryImpl implements TournamentRepository {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Tournament> findAllTournament() {
        String sql = "SELECT * from Tournament";
        return jdbcTemplate.query(sql, new TournamentRowMapper());
    }

    @Override
    public List<Tournament> findTournamentById(int i) throws Exception {
            String sql = "SELECT * FROM Tournament WHERE TournamentId =:tournamentId";
            Tournament tournament = new Tournament();
            tournament.setTournamentId(i);
            return jdbcTemplate.query(sql,new BeanPropertySqlParameterSource(tournament),new TournamentRowMapper());
    }

    @Override
    public List<Tournament> findTournamentByActive() throws Exception {
        String tournament_sql = "SELECT * from Tournament WHERE active = true";
       return jdbcTemplate.query(tournament_sql,new TournamentRowMapper());
    }

    @Override
    public int addTournament(Tournament tournament) throws Exception {
       String sql = "INSERT INTO Tournament (Name,active) VALUES(:name,0)";
       return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(tournament));
    }

    @Override
    public boolean updateTournament(int id, Tournament tournament) throws Exception {
        String sql = "UPDATE `" + "Tournament" + "` set "
                + "`Name` = :name where `TournamentId`=:tournamentId";
        tournament.setTournamentId(id);
        return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(tournament)) > 0;
    }

    @Override
    public boolean updateActiveTournament(int id) throws Exception {
        String sql = "UPDATE Tournament SET active=1 WHERE TournamentId=:tournamentId";
        Tournament tournament = new Tournament();
        tournament.setTournamentId(id);
        return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(tournament)) > 0;
    }

    @Override
    public boolean deactivateTournament() throws Exception {
        String deactive_tournament = "UPDATE Tournament SET active=0 WHERE active=1";
        return jdbcTemplate.update(deactive_tournament,new BeanPropertySqlParameterSource(deactive_tournament)) > 0;
    }

    @Override
    public int deleteTournament(int id) throws Exception {
        String sql = "DELETE FROM Tournament WHERE TournamentId =:tournamentId";
        Tournament tournament = new Tournament();
        tournament.setTournamentId(id);
        return jdbcTemplate.update(sql,new BeanPropertySqlParameterSource(tournament));
    }
}
