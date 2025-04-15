package com.example.freelancermarketplace.Classes;

import java.util.ArrayList;
import java.util.List;

public class Bridge {
    public static List<User> listUsers = new ArrayList<>();
    public static List<Review> listReview = new ArrayList<>();

    public  static User getUserById(String id){
        for(User u :listUsers){
            if(u.getUserId().equals(id))
                return u;
        }
        return null;
    }
    public  static Review getReviewbyJobID(String id){
        for(Review u :listReview){
            if(u.getJobId().equals(id))
                return u;
        }
        return null;
    }
}
