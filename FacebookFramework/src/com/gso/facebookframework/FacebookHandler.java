package com.gso.facebookframework;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import com.gso.facebookframework.library.DialogError;
import com.gso.facebookframework.library.Facebook;
import com.gso.facebookframework.library.FacebookError;
import com.gso.facebookframework.library.Facebook.DialogListener;
import com.gso.facebookframework.share.ShareData;
import com.gso.facebookframework.share.ShareHandler;

public class FacebookHandler {

	private Context context;
	private Activity activity;

	public FacebookHandler(Context context, Activity activity, String appId) {
		this.context = context;
		Config.appId = appId;
		this.activity = activity;
	}

	public boolean saveFacebookToken(String key, String value, Context activity) {
		Editor editor = activity.getSharedPreferences("facebook",
				Context.MODE_PRIVATE).edit();
		if (value != null)
			editor.putString("fb_token", value);

		return editor.commit();
	}

	public String getFacebookToken() {
		SharedPreferences savedSession = context.getSharedPreferences(
				"facebook", Context.MODE_PRIVATE);
		String key = savedSession.getString("fb_token", "");
		if (key == "") {
			return null;
		}
		return key;
	}
	public String getFacebookToken(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(
				"facebook", Context.MODE_PRIVATE);
		String key = savedSession.getString("fb_token", "");
		if (key == "") {
			return null;
		}
		return key;
	}
	public boolean checkLoginFacebook() {
		// TODO Auto-generated method stub

		String accessToken = getFacebookToken();
		if (accessToken != null) {
			Facebook fb = new Facebook(Config.appId);
			fb.setAccessToken(accessToken);
			return true;

		} else {
			final Facebook mFacebook;
			mFacebook = new Facebook(Config.appId);
			mFacebook.authorize(activity, new String[] { "email",
					"status_update", "user_birthday" }, new DialogListener() {
				@Override
				public void onComplete(Bundle values) {

					String accessToken = values.getString(Facebook.TOKEN);
					saveFacebookToken("oauth_token", accessToken, context);
					mFacebook.setAccessExpiresIn("0");
					mFacebook.setAccessToken(accessToken);
					mFacebook.setAccessExpires(mFacebook.getAccessExpires());
				}

				@Override
				public void onFacebookError(FacebookError error) {

				}

				@Override
				public void onError(DialogError e) {

				}

				@Override
				public void onCancel() {
					// cancel press or back press
				}
			});
			return false;
		}

	}
	
	private boolean share(ShareData shareData){
		ShareHandler shareHandler = new ShareHandler(context);
//		ShareData shareData = new ShareData();
//		shareData.setCaption("");
//		shareData.setDescription("");
//		shareData.setLink(data.getLink());
//		shareData.setMessage(etMessage.getText().toString());
//		shareData.setName("WHY Q");
//		shareData.setPicture(data.getImage());
//		shareData.setThumb("");
		shareHandler.postFacebook(getFacebookToken(context),shareData, Config.appId);
		
		return true;
	}

}
