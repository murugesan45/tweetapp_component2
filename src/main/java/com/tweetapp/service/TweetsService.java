package com.tweetapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.tweetapp.entity.Tweets;
import com.tweetapp.exception.TweetNotFoundException;
import com.tweetapp.model.ReplyTweet;
import com.tweetapp.model.TweetReply;
import com.tweetapp.model.TweetsDTO;
import com.tweetapp.repository.TweetsRepo;

@Service
public class TweetsService {

	@Autowired
	TweetsRepo tweetsRepo;

	@Autowired
	MongoTemplate mongoTemplate;

	public void updateUserTweet(String username, String id, TweetsDTO data) throws Exception {

		try {
			ModelMapper modelMapper = new ModelMapper();
			Tweets userTweet = modelMapper.map(data, Tweets.class);
			Query query = new Query();
			query.addCriteria(new Criteria().andOperator(Criteria.where("tweetId").is(id),
					Criteria.where("loginId").is(username)));
			Update update = new Update();
			update.set("tweet", userTweet.getTweet());
			mongoTemplate.updateFirst(query, update, Tweets.class);
		} catch (Exception e) {
			throw new TweetNotFoundException();
		}

	}

	public List<TweetsDTO> getTweetsOfAllUser() {

		List<Tweets> userTweet = tweetsRepo.findAll();
		if (userTweet == null)
			throw new TweetNotFoundException();
		ModelMapper modelMapper = new ModelMapper();
		List<TweetsDTO> userTweets = userTweet.stream().map(users -> modelMapper.map(users, TweetsDTO.class))
				.collect(Collectors.toList());
		return userTweets;
	}

	public void deleteUserTweet(String tweetId) {
		long result = -1;

		result = tweetsRepo.deleteByTweetId(tweetId);

		if (result == 0)
			throw new TweetNotFoundException("The requested tweet does not found");

	}

	public void likeUserTweet(String tweetId, String userName) {

		Tweets tweet = tweetsRepo.findByTweetId(tweetId);
		if (tweet == null)
			throw new TweetNotFoundException("The requested tweet does not found");
		List<String> likes = tweet.getLikes();
		likes.add(userName);
		int like  = tweet.getLikeCount();
		like = like + 1;
		tweet.setLikeCount(like);
		tweet.setLikes(likes);
		tweetsRepo.save(tweet);

	}

	public void replyUserTweet(String username, String tweetId, ReplyTweet replyTweet) {

		Tweets tweet = tweetsRepo.findByTweetId(tweetId);
		if (tweet == null)
			throw new TweetNotFoundException();
		TweetReply tweetReply = new TweetReply();
		tweetReply.setUsername(username);
		tweetReply.setReply(replyTweet.getReply());
		if(tweet.getReply()!=null)
		tweet.getReply().add(tweetReply);
		else{
			List<TweetReply> list = new ArrayList<>();
			tweet.setReply(list);
		}
		tweetsRepo.save(tweet);

	}

	public List<TweetsDTO> findTweetsByUserName(String userName) {
		ModelMapper modelMapper = new ModelMapper();
		List<Tweets> userTweet = tweetsRepo.findByLoginId(userName);
		if (userTweet == null)
			throw new TweetNotFoundException();
		List<TweetsDTO> userTweets = userTweet.stream().map(users -> modelMapper.map(users, TweetsDTO.class))
				.collect(Collectors.toList());
		return userTweets;
	}

	//@KafkaListener(topics = "Tweets", groupId = "tweetapp_id", containerFactory = "userKafkaListenerFactory")
	public Tweets postUserTweets(TweetsDTO tweet) {
		UUID uuid = UUID.randomUUID();
		ModelMapper modelMapper = new ModelMapper();
		Tweets userTweet = modelMapper.map(tweet, Tweets.class);
		userTweet.setTweetId(uuid.toString());
		return tweetsRepo.save(userTweet);
	}

}
