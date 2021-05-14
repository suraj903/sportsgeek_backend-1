package com.project.sportsgeek.service;

import com.project.sportsgeek.exception.ResultException;
import com.project.sportsgeek.model.Recharge;
import com.project.sportsgeek.model.profile.User;
import com.project.sportsgeek.repository.rechargerepo.RechargeRepository;
import com.project.sportsgeek.repository.userrepo.UserRepository;
import com.project.sportsgeek.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RechargeService {
    @Autowired
    @Qualifier("rechargeRepo")
    RechargeRepository rechargeRepository;
    @Autowired
    @Qualifier("userRepo")
    UserRepository userRepository;

    public Result<List<Recharge>> findAllRecharge() {
        List<Recharge> rechargeList = rechargeRepository.findAllRecharge();
        return new Result<>(200,rechargeList);
    }

    public Result<Recharge> findRechargeByRechargeId(int id) throws Exception {
        List<Recharge> rechargeList = rechargeRepository.findRechargeByRechargeId(id);
        if (rechargeList.size() > 0) {
            return new Result<>(200, rechargeList.get(0));
        }
        else {
            return new Result(404,"No Recharge's found,please try again","Recharge with id=('"+ id +"') not found");
        }
    }

    public Result<List<Recharge>> findRechargeByUserId(int id) throws Exception {
        List<Recharge> rechargeList = rechargeRepository.findRechargeByUserId(id);
        if (rechargeList.size() > 0) {
            return new Result<>(200, rechargeList);
        }
        else {
            return new Result(404,"No Recharge's found,please try again","Recharge with id=('"+ id +"') not found");
        }
    }

    public Result<Recharge> addRecharge(Recharge recharge) throws Exception {
        int id = rechargeRepository.addRecharge(recharge);
        recharge.setRechargeId(id);
        if (id > 0) {
            int n = userRepository.addAvailablePoints(recharge.getUserId(), recharge.getPoints());
            if(n > 0){
                return new Result<>(201, recharge);
            }
            else{
                return new Result<>(500, "Unable to update user available points.");
            }
        }
        throw new ResultException(new Result<>(400, "Error!, please try again!", new ArrayList<>(Arrays
                .asList(new Result.SportsGeekSystemError(recharge.hashCode(), "unable to add the given Recharge")))));
    }
    public Result<Recharge> updateRecharge(int id, Recharge recharge) throws Exception {
        Recharge oldRecharge = rechargeRepository.findRechargeByRechargeId(id).get(0);
        if (rechargeRepository.updateRecharge(id,recharge)) {
            int n = userRepository.addAvailablePoints(oldRecharge.getUserId(), (recharge.getPoints() - oldRecharge.getPoints()));
            if(n > 0){
                return new Result<>(200, recharge);
            }
            else{
                return new Result<>(500, "Unable to update user available points.");
            }
        }
        throw new ResultException(new Result<>(400, "Unable to update the given Recharge details! Please try again!", new ArrayList<>(Arrays
                .asList(new Result.SportsGeekSystemError(recharge.hashCode(), "given RechargeId('"+id+"') does not exists")))));
    }
    public Result<Integer> deleteRecharge(int id) throws Exception{
        // Get Recharge details before deleting
        Recharge recharge = rechargeRepository.findRechargeByRechargeId(id).get(0);
        int data = rechargeRepository.deleteRecharge(id);
        if (data > 0) {
            int n = userRepository.deductAvailablePoints(recharge.getUserId(), recharge.getPoints());
            if(n > 0){
//                return new Result<>(200, recharge);
                return new Result<>(200, "Recharge deleted successfully.");
            }
            else{
                return new Result<>(500, "Unable to update user available points.");
            }
        }
        else {
            throw new ResultException((new Result<>(404,"No Recharge's found to delete,please try again","Recharge with id=('"+ id +"') not found")));
        }
    }
}