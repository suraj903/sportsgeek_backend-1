package com.project.sportsgeek.repository.genderrepo;

import com.project.sportsgeek.mapper.GenderRowMapper;
import com.project.sportsgeek.model.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "genderRepo")
public class GenderRepositoryImpl implements GenderRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Gender> findAllGender() {
        String sql = "SELECT * FROM Gender";
        try{
            return jdbcTemplate.query(sql, new GenderRowMapper());
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public List<Gender> findGenderById(int genderId) throws Exception {
        String sql = "SELECT * FROM Gender WHERE GenderId = :genderId";
        MapSqlParameterSource params = new MapSqlParameterSource("genderId", genderId);
        try{
            return jdbcTemplate.query(sql, params, new GenderRowMapper());
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public int addGender(Gender gender) throws Exception {
        KeyHolder holder = new GeneratedKeyHolder();
        String sql = "INSERT INTO Gender (Name) values(:name)";
        try {
            int n = jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(gender));
            if(n > 0) {
                return holder.getKey().intValue();
            }else {
                return 0;
            }
        }catch (Exception e) {
            return 0;
        }
    }

    @Override
    public boolean updateGender(int genderId, Gender gender) throws Exception {
        String sql = "UPDATE Gender SET Name = :name WHERE GenderId = :genderId";
        gender.setGenderId(genderId);
        try{
            return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(gender)) > 0;
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean deleteGender(int genderId) throws Exception {
        String sql = "DELETE FROM Gender WHERE GenderId = :genderId";
        MapSqlParameterSource params = new MapSqlParameterSource("genderId", genderId);
        try{
            return jdbcTemplate.update(sql, params) > 0;
        }catch(Exception e){
            return false;
        }
    }

}
