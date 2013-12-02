package com.gso.facebookframework.share;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.content.Context;
import android.net.wifi.WifiConfiguration.Status;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.widget.Toast;

import com.gso.facebookframework.BaseRequestListener;
import com.gso.facebookframework.library.AsyncFacebookRunner;
import com.gso.facebookframework.library.Facebook;
import com.gso.facebookframework.library.FacebookError;

public class ShareHandler implements ShareListener {
	private String shareMessage;
	private String linkshareFacebook = "";
	private String linkshareTwitter = null;
	public static int TWITTER = 1;
	public static int FACEBOOK = 2;
	public Context context;
	public static String errorMesage = "";
	private Context mContext;
	private Status status;

	public ShareHandler(Context context) {
		// TODO Auto-generated constructor stub

		this.context = context;

	}

	@Override
	public void success() {
		// TODO Auto-generated method stub

	}

	@Override
	public void fail() {
		// TODO Auto-generated method stub

	}


	public boolean postFacebook(String accessToken, ShareData data, String appId) {
		// message = IssueAdapter.currentDesription;

		Facebook facebook = new Facebook(appId);
		facebook.setAccessToken(accessToken);
		if (facebook != null) {
			String namePost = "";// = IssueAdapter.currentDesription;

			String msg = limitContent(data.getMessage(), " " + linkshareFacebook);

			AsyncFacebookRunner mAsyncFbRunner = new AsyncFacebookRunner(
					facebook);
			Bundle params = new Bundle();
			params.putString("message", msg);
			params.putString("name", data.getName());
			params.putString("caption", data.getLink());
			String disription = data.getDescription();
			String picture = data.getPicture();
			params.putString("description", data.getDescription());

			Log.d("WallPostListener", "picture is:" + picture);
			if (picture != null && !picture.equals("") && !picture.equals("null"))
				params.putString("picture", picture);

			mAsyncFbRunner.request("me/feed", params, "POST",
					new WallPostListener());
			return true;
		}
		return false;
	}

	private Handler mRunOnUi = new Handler();

	private final class WallPostListener extends BaseRequestListener {


		@Override
		public void onComplete(final String response) {

			// TODO Auto-generated method stub

			mRunOnUi.post(new Runnable() {
				@Override
				public void run() {
					Log.d("WallPostListener", "" + response);
					Toast.makeText(context, "Posted to Facebook",
							Toast.LENGTH_SHORT).show();
				}
			});

		
		}

		@Override
		public void onIOException(IOException e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFileNotFoundException(FileNotFoundException e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onMalformedURLException(MalformedURLException e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFacebookError(FacebookError e) {
			// TODO Auto-generated method stub
			
		}
	}

	static public String limitContent(String message, String extra) {
		String result = message;
		String mark = "\"";
		Boolean endIsLyric = false;
		if (result.endsWith(mark))
			endIsLyric = true;
		int limit = (200000 - extra.length());
		if (result.length() > limit) {
			if (endIsLyric) {
				result = result.substring(0, limit - (3 + mark.length()));
				result += ("..." + mark);
			} else {
				result = result.substring(0, limit - 3);
				result += ("...");
			}
		}
		result += "\n" + extra;
		Log.v("Post Tweet", result);
		return result;
	}
}
