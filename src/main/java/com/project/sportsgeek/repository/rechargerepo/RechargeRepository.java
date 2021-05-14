package com.project.sportsgeek.repository.rechargerepo;

import com.project.sportsgeek.model.Recharge;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "rechargeRepo")
public interface RechargeRepository {

    List<Recharge> findAllRecharge();

    List<Recharge> findRechargeByRechargeId(int i) throws Exception;

    List<Recharge> findRechargeByUserId(int i) throws Exception;

    int addRecharge(Recharge Recharge) throws Exception;

    boolean updateRecharge(int id, Recharge Recharge) throws Exception;

    int deleteRecharge(int id) throws Exception;

}
