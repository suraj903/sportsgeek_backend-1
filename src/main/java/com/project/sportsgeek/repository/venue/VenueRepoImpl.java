package com.project.sportsgeek.repository.venue;

import com.project.sportsgeek.mapper.VenueRowMapper;
import com.project.sportsgeek.model.Venue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "venueRepo")
public class VenueRepoImpl implements VenueRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Venue> findAllVenue() {
        String sql = "SELECT * FROM Venue";
        return jdbcTemplate.query(sql,new VenueRowMapper());
    }

    @Override
    public List<Venue> findVenueById(int id) throws Exception {
        String sql = "SELECT * FROM Venue WHERE VenueId=:venueId";
        Venue venue =  new Venue();
        venue.setVenueId(id);
        return jdbcTemplate.query(sql,new BeanPropertySqlParameterSource(venue),new VenueRowMapper());
    }

    @Override
    public int addVenue(Venue venue) throws Exception {
       String sql = "INSERT INTO Venue(Name) VALUES(:name)";
       return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(venue));
    }

    @Override
    public boolean updateVenue(int id, Venue venue) throws Exception {
        String sql = "UPDATE `" + "Venue" + "` set "
                + "`Name` = :name where `VenueId`=:venueId";
        venue.setVenueId(id);
        return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(venue)) > 0;
    }

    @Override
    public int deleteVenue(int id) throws Exception {
        String sql = "DELETE FROM Venue WHERE VenueId =:venueId";
        Venue venue = new Venue();
        venue.setVenueId(id);
        return  jdbcTemplate.update(sql,new BeanPropertySqlParameterSource(venue));
    }
}
