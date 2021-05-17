package com.project.sportsgeek.service;

import com.project.sportsgeek.exception.ResultException;
import com.project.sportsgeek.model.profile.Role;
import com.project.sportsgeek.repository.rolerepo.RoleRepository;
import com.project.sportsgeek.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    @Qualifier("roleRepo")
    RoleRepository roleRepository;

    public Result<List<Role>> findAllRole() {
        List<Role> roleList = roleRepository.findAllRole();
        return new Result<>(200, roleList);
    }

    public Result<Role> findRoleById(int roleId) throws Exception {
        Role role = roleRepository.findRoleById(roleId);
        if (role != null) {
            return new Result<>(200, role);
        }
        throw new ResultException((new Result<>(404, "No Role's found,please try again", "Role with id=('" + roleId + "') not found")));
    }

    public Result<Role> addRole(Role role) throws Exception {
        int id = roleRepository.addRole(role);
        role.setRoleId(id);
        if (id > 0) {
            return new Result<>(201, role);
        }
        throw new ResultException(new Result<>(400, "Error in adding the Role"));
    }

    public Result<Role> updateRole(int roleId, Role role) throws Exception {
        if (roleRepository.updateRole(roleId, role)) {
            return new Result<>(200, role);
        }
        throw new ResultException(new Result<>(400, "Error in updating the Role!!. Role with Role Id=(" + roleId + ") not found"));
    }

    public Result<String> deleteRole(int roleId) throws Exception {
        if (roleRepository.deleteRole(roleId)) {
            return new Result<>(200, "Role deleted successfully.");
        }
        throw new ResultException(new Result<>(404, "Error in deleting the Role!!. Role with Role Id=(" + roleId + ") not found"));
    }
}
