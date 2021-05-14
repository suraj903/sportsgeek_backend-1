package com.project.sportsgeek.service;

import com.project.sportsgeek.exception.ResultException;
import com.project.sportsgeek.model.Gender;
import com.project.sportsgeek.repository.genderrepo.GenderRepository;
import com.project.sportsgeek.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GenderService {

    @Autowired
    @Qualifier("genderRepo")
    GenderRepository genderRepository;

    public Result<List<Gender>> findAllGender() {
        List<Gender> genderList = genderRepository.findAllGender();
        return new Result<>(200, genderList);
    }

    public Result<Gender> findGenderById(int genderId) throws Exception {
        List<Gender> genderList = genderRepository.findGenderById(genderId);
        if (genderList.size() > 0) {
            return new Result<>(200, genderList.get(0));
        } else {
            throw new ResultException((new Result<>(404, "No Gender's found,please try again", "Gender with id=('" + genderId + "') not found")));
        }
    }

    public Result<Gender> addGender(Gender gender) throws Exception {
        int id = genderRepository.addGender(gender);
        gender.setGenderId(id);
        if (id > 0) {
            return new Result<>(201, gender);
        }
        throw new ResultException(new Result<>(400, "Error!, please try again!", new ArrayList<>(Arrays
                .asList(new Result.SportsGeekSystemError(gender.hashCode(), "unable to add the given gender")))));
    }

    public Result<Gender> updateGender(int genderId, Gender gender) throws Exception {
        if (genderRepository.updateGender(genderId, gender)) {
            return new Result<>(201, gender);
        }
        throw new ResultException(new Result<>(400, "Unable to update the given gender details! Please try again!", new ArrayList<>(Arrays
                .asList(new Result.SportsGeekSystemError(gender.hashCode(), "given genderId('" + genderId + "') does not exists")))));
    }

    public Result<Integer> deleteGender(int genderId) throws Exception {
        int data = genderRepository.deleteGender(genderId);
        if (data > 0) {
            return new Result<>(200, data);
        } else {
            throw new ResultException((new Result<>(404, "No Gender's found to delete,please try again", "Gender with id=('" + genderId + "') not found")));
        }
    }
}
