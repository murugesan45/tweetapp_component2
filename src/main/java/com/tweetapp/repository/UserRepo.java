package com.tweetapp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tweetapp.entity.Users;

@Repository
public interface UserRepo extends MongoRepository<Users, String> {

	Users findByEmail(String userName);

}
