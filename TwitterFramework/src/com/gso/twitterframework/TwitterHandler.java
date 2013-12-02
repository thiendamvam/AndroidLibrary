package com.gso.twitterframework;

import java.io.ObjectInputStream.GetField;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.http.AccessToken;
import android.content.Context;
import android.content.Intent;

import com.gso.twitterframework.interfaces.ITwitterLoginListener;
import com.gso.twitterframework.util.SharedPreferencesManager;

public class TwitterHandler {

	private Context context;
	private SharedPreferencesManager shareManager;

	public TwitterHandler(Context context, String CONSUMER_KEY,
			String CONSUMER_SECRET) {
		// TODO Auto-generated constructor stub
		this.context = context;
		Config.CONSUMER_KEY = CONSUMER_KEY;
		Config.CONSUMER_SECRET = CONSUMER_SECRET;
		shareManager = new SharedPreferencesManager(context);
	}

	public boolean checkTwitterLogin() {

		AccessToken tw = shareManager.loadTwitterToken();
		if (tw != null) {
			return true;
		} else {
			return false;
		}
	}

	public void loginTwitter() {
		if (!checkTwitterLogin()) {
			Intent i = new Intent(context, TwitterActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		}
	}

	

}
