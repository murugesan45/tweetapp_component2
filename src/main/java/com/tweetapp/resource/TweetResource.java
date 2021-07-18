package com.tweetapp.resource;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.tweetapp.entity.Tweets;
import com.tweetapp.model.ReplyTweet;
import com.tweetapp.model.TweetsDTO;
import com.tweetapp.service.TweetsService;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SSMClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;
import software.amazon.awssdk.services.ssm.model.SSMException;



@RestController
@CrossOrigin("*")
public class TweetResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(TweetResource.class);
	@Autowired
	KafkaTemplate<String, TweetsDTO> kafkaTemplate;

	@Autowired
	TweetsService tweetsService;

	//private static final String TOPIC = "Tweets";

	@GetMapping("/api/v1.0/tweets/all")
	public ResponseEntity<List<TweetsDTO>> getAllUserTweets() throws Exception {

		LOGGER.info("Get all tweets invoked successfully");
		return new ResponseEntity<>(tweetsService.getTweetsOfAllUser(), HttpStatus.OK);

	}

	@GetMapping("/api/v1.0/tweets/{username}")
	public ResponseEntity<List<TweetsDTO>> getAllTweetsOfTheUser(@PathVariable("username") String userName) {

		LOGGER.info(userName + "invoked get all tweets api successful");
		return new ResponseEntity<>(tweetsService.findTweetsByUserName(userName), HttpStatus.OK);

	}

	@PostMapping("/api/v1.0/tweets/{username}/add")
	public ResponseEntity<Tweets> postUserTweet(@PathVariable String username, @RequestBody TweetsDTO tweet)
			throws Exception {

		tweet.setLoginId(username);
		//kafkaTemplate.send(TOPIC, "Message", tweet);
		tweetsService.postUserTweets(tweet);
		LOGGER.info("The tweet by " + username + " is posted successfully");
		return new ResponseEntity<>(tweetsService.postUserTweets(tweet),HttpStatus.OK);

	}

	@PutMapping("/api/v1.0/tweets/{username}/update/{id}")
	public ResponseEntity<?> updateUserTweet(@PathVariable String username, @PathVariable String id,
			@RequestBody TweetsDTO data) throws Exception {

	    LOGGER.info(username + " " + id + "tweet update success");
		tweetsService.updateUserTweet(username, id, data);
		return new ResponseEntity<>(HttpStatus.OK);

	}

	@DeleteMapping("/api/v1.0/tweets/{username}/delete/{id}")
	public ResponseEntity<?> deleteUserTweet(@PathVariable String username, @PathVariable String id) {
		LOGGER.info(username + "deleted the tweet " + id);
		tweetsService.deleteUserTweet(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping("/api/v1.0/tweets/{username}/like/{id}")
	public ResponseEntity<?> likeUserTweet(@PathVariable String username, @PathVariable String id) {
        tweetsService.likeUserTweet(id, username);
		return new ResponseEntity<>(HttpStatus.OK);

	}

	@PostMapping("/api/v1.0/tweets/{username}/reply/{id}")
	public ResponseEntity<?> userReplyTweet(@PathVariable String username, @PathVariable String id,
			@RequestBody ReplyTweet replyTweet) {
      System.out.println(username + replyTweet.getReply());
		tweetsService.replyUserTweet(username, id, replyTweet);
		LOGGER.info(username+ "replied to the tweet " + id);
		return new ResponseEntity<>(HttpStatus.OK);

	}
	
	@GetMapping("/ssm")
	public void getSSMParam(){
		 Region region = Region.US_EAST_1;
	        SSMClient ssmClient = SSMClient.builder()
	                .region(region)
	                .build();

	        try {
	            GetParameterRequest parameterRequest = GetParameterRequest.builder()
	                .name("ACCESS_KEY")
	                .build();

	            GetParameterResponse parameterResponse = ssmClient.getParameter(parameterRequest);
	            System.out.println("The parameter value is "+parameterResponse.parameter().value());

	        } catch (SSMException e) {
	        System.err.println(e.getMessage());
	        System.exit(1);
	        }
	}

}
