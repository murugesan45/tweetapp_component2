package com.tweetapp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tweetapp.entity.Tweets;


@Repository
public interface TweetsRepo extends MongoRepository<Tweets, String>{

	List<Tweets> findByLoginId(String string);

	Tweets findByTweetId(String tweetId);
	
	@DeleteQuery
    long deleteByTweetId(String tweetId);
 

}