package com.configuration;

import com.dao.UserModelDAO;
import com.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

/**
 * Created by piotrek on 17.12.15.
 */
public class IdNumberGenerator {

    @Autowired
     UserModelDAO userModelDAO;

    public int getRandomNumberInRange(int min, int max) {

        int idNumber;
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }


        Random r = new Random();
        idNumber =  r.nextInt((max - min) + 1) + min;

        while(userModelDAO.getByIdNumber(idNumber)!=null){
            r = new Random();
            idNumber =  r.nextInt((max - min) + 1) + min;
        }
        return idNumber;
    }
}
