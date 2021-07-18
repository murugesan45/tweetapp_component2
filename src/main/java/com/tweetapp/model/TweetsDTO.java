package com.tweetapp.model;

import java.util.List;

import javax.validation.constraints.NotBlank;

public class TweetsDTO {

	@NotBlank
	private String loginId;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getTweetId() {
		return tweetId;
	}

	public void setTweetId(String tweetId) {
		this.tweetId = tweetId;
	}

	public String getTweet() {
		return tweet;
	}

	public void setTweet(String tweet) {
		this.tweet = tweet;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<String> getLikes() {
		return likes;
	}

	public void setLikes(List<String> likes) {
		this.likes = likes;
	}

	public List<TweetReply> getReply() {
		return reply;
	}

	public void setReply(List<TweetReply> reply) {
		this.reply = reply;
	}

	private String tweetId;

	@NotBlank
	private String tweet;

	@NotBlank
	private String date;
	
	private int likeCount;

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	private List<String> likes;

	private List<TweetReply> reply;

}
