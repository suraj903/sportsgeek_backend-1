package com.project.sportsgeek.repository.rolerepo;

import com.project.sportsgeek.model.profile.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "roleRepo")
public interface RoleRepository {

    List<Role> findAllRole();

    List<Role> findRoleById(int id) throws Exception;

    int addRole(Role role) throws Exception;

    boolean updateRole(int id, Role role) throws Exception;

    boolean deleteRole(int id) throws Exception;

}
