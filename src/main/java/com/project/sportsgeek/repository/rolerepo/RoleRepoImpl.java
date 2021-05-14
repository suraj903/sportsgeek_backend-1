package com.project.sportsgeek.repository.rolerepo;

import com.project.sportsgeek.mapper.RoleRowMapper;
import com.project.sportsgeek.model.profile.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "roleRepo")
public class RoleRepoImpl implements RoleRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Role> findAllRole() {
        String sql = "SELECT * FROM Role";
        return jdbcTemplate.query(sql, new RoleRowMapper());
    }

    @Override
    public List<Role> findRoleById(int id) throws Exception {
        String sql = "SELECT * FROM Role WHERE RoleId=" + id;
        return jdbcTemplate.query(sql, new RoleRowMapper());
    }

    @Override
    public int addRole(Role role) throws Exception {
        String insert_query = "INSERT INTO Role (Name) values('" + role.getName() + ")";
        jdbcTemplate.update(insert_query, new BeanPropertySqlParameterSource(role));
        return 1;
    }

    @Override
    public boolean updateRole(int id, Role role) throws Exception {
        String update_query = "UPDATE Role SET Name = '" + role.getName() + "' WHERE RoleId =" + id;
        role.setRoleId(id);
        return jdbcTemplate.update(update_query, new BeanPropertySqlParameterSource(role)) > 0;
    }

    @Override
    public int deleteRole(int id) throws Exception {
        String delete_query = "DELETE FROM Role WHERE RoleId =" + id;
        return jdbcTemplate.update(delete_query, new BeanPropertySqlParameterSource(id));
    }
}
