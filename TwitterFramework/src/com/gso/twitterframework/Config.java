package com.gso.twitterframework;

public class Config {
	
	public static final String REQUEST_URL = "https://api.twitter.com/oauth/request_token";
	public static final String ACCESS_URL = "https://api.twitter.com/oauth/access_token";
	public static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";
	
	public static final String	OAUTH_CALLBACK_SCHEME	= "perm";
	public static final String	OAUTH_CALLBACK_HOST		= "twitter";
	public static final String	OAUTH_CALLBACK_URL		=  "perm://twitter";
	public static String CONSUMER_KEY = ""; // set from implement project
	public static String CONSUMER_SECRET = "";// set from implement project
	public static String LINK_SHARE_TWITTER_GLOBAL;
}
