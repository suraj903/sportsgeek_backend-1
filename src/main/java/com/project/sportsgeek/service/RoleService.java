package com.project.sportsgeek.service;

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

    public Result<Role> findRoleById(int id) throws Exception {
        List<Role> roleList = roleRepository.findRoleById(id);
        if (roleList.size() > 0) {
            return new Result<>(200, roleList.get(0));
        } else {
            return new Result<>(400, "Role with Role Id=(" + id + ") not found");
        }
    }

    public Result<Role> addRole(Role role) throws Exception {
        int id = roleRepository.addRole(role);
        role.setRoleId(id);
        if (id > 0) {
            return new Result<>(201, role);
        }
        return new Result<>(400, "Error in adding the Role");
    }

    public Result<Role> updateRole(int id, Role role) throws Exception {
        if (roleRepository.updateRole(id, role)) {
            return new Result<>(201, role);
        }
        return new Result<>(400, "Error in updating the Role!!. Role with Role Id=(" + id + ") not found");
    }

    public Result<Integer> deleteRole(int id) throws Exception {
        int data = roleRepository.deleteRole(id);
        if (data > 0) {
            return new Result<>(200, data);
        } else {
            return new Result<>(400, "Error in deleting the Role!!. Role with Role Id=(" + id + ") not found");
        }
    }
}
