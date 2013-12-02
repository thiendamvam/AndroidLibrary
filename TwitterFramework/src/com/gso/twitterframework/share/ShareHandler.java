package com.gso.twitterframework.share;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import android.content.Context;
import android.util.Log;

import com.gso.twitterframework.Config;
import com.gso.twitterframework.util.SharedPreferencesManager;

public class ShareHandler implements ShareListener {
	private String shareMessage;
	Twitter twitter;
	SharedPreferencesManager shareManager;
	public static int TWITTER = 1;
	public static int FACEBOOK = 2;
	public Context context;

	public ShareHandler(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		shareManager = new SharedPreferencesManager(this.context);
	}

	@Override
	public void success() {
		// TODO Auto-generated method stub

	}

	@Override
	public void fail() {
		// TODO Auto-generated method stub

	}

	public boolean postTwitter(String message) {
		twitter = shareManager.loadTwitter();
		if (twitter == null) {
			twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(Config.CONSUMER_KEY,
					Config.CONSUMER_SECRET);
		}
		AccessToken accessToken = shareManager.loadTwitterToken();
		if (accessToken != null) {
			twitter.setOAuthAccessToken(accessToken);
			String msg = limitContent(message, "");
			try {
				Log.d("postTwitter", "share actiion");
				twitter.updateStatus(msg);
				return true;
			} catch (twitter4j.TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	static public String limitContent(String message, String extra) {
		String result = message;
		String mark = "\"";
		Boolean endIsLyric = false;
		if (result.endsWith(mark))
			endIsLyric = true;
		int limit = (140 - extra.length());
		if (result.length() > limit) {
			if (endIsLyric) {
				result = result.substring(0, limit - (3 + mark.length()));
				result += ("..." + mark);
			} else {
				result = result.substring(0, limit - 3);
				result += ("...");
			}
		}
		result += extra;
		Log.v("Post Tweet", result);
		return result;
	}
}
