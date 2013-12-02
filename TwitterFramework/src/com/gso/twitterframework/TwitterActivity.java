package com.gso.twitterframework;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.twitterframework.R;
import com.gso.twitterframework.util.SharedPreferencesManager;

public class TwitterActivity extends Activity {
	// Define constain
	public static final String TWITTER_KEY_LOGIN = "twitter_key_login";
	public static final String REQUEST_URL = "https://api.twitter.com/oauth/request_token";
	public static final String ACCESS_TOKEN_URL = "https://twitter.com/oauth/access_token";
	public static final String AUTH_URL = "https://twitter.com/oauth/authorize";

	public static final String CALLBACK_URL = Config.OAUTH_CALLBACK_URL;
	// Property
	public static WebView twitterWebView;
	public ProgressBar progressBar;

	private int TWITTER_AUTH;
	private Twitter mTwitter;
	private RequestToken mRequestToken;
	private SharedPreferencesManager shareManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_intent);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			setFinishOnTouchOutside(true);
		}
		twitterWebView = (WebView) findViewById(R.id.twitter_web);
		progressBar = (ProgressBar) findViewById(R.id.twProgressBar);
		twitterWebView.clearCache(true);
		shareManager = new SharedPreferencesManager(this);

	}

	protected boolean flag = false;

	@Override
	protected void onResume() {
		mTwitter = shareManager.loadTwitter();
		if (mTwitter == null) {
			mTwitter = new TwitterFactory().getInstance();
			new OAuthRequestTokenTask(TwitterActivity.this).execute();

		}
		super.onResume();

	}

	public class OAuthRequestTokenTask extends AsyncTask<Void, Void, Void> {

		final String TAG = getClass().getName();
		private Context context;
		private String url;

		public OAuthRequestTokenTask(Context context) {
			this.context = context;

		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				// TODO Auto-generated method stub

				mRequestToken = null;
				mTwitter.setOAuthConsumer(Config.CONSUMER_KEY,
						Config.CONSUMER_SECRET);
				String callbackURL = CALLBACK_URL;
				try {
					mRequestToken = mTwitter.getOAuthRequestToken(callbackURL);
					url = mRequestToken.getAuthenticationURL();
				} catch (TwitterException e) {
					e.printStackTrace();
				}

				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						twitterWebView.setWebViewClient(new TWWebviewClient());
						twitterWebView.getSettings().setJavaScriptEnabled(true);
						twitterWebView.getSettings().setSavePassword(false);
						twitterWebView.loadUrl(url);
						
					}
				});
			} catch (Exception e) {
				progressBar.setVisibility(View.GONE);
				e.printStackTrace();

			}
			return null;
		}

	}

	public class AsyGetData extends AsyncTask<Void, Void, String> {

		final String TAG = getClass().getName();
		private Context context;
		private String veryfy;

		public AsyGetData(Context context, String veryfy) {
			this.context = context;
			this.veryfy = veryfy;
		}

		@Override
		protected String doInBackground(Void... params) {

			try {
				// TODO Auto-generated method stub

				at = mTwitter.getOAuthAccessToken(veryfy);

				shareManager.saveTwitterToken(at);

				return at.getScreenName();
			} catch (Exception e) {
				Log.e("a", "Error during OAUth retrieve request token", e);
				progressBar.setVisibility(View.GONE);
				return "Failer";
			}
		}

		@Override
		protected void onPostExecute(String sResponse) {
			if (!sResponse.equals("Failer")) {
				Intent result = new Intent();
				result.putExtra("LOGIN_RESULT", "" + sResponse);
				setResult(RESULT_OK, result);
				finish();
			} else {

			}
		}

	}

	private Twitter twitter = null;
	private String ScreenName = null;
	AccessToken at;
	String _verifier;

	private void end() {
		finish();
	}

	public class TWWebviewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			progressBar.setVisibility(View.GONE);
			if (url.contains(CALLBACK_URL)) {
				Uri uri = Uri.parse(url);
				String oauthVerifier = uri.getQueryParameter("oauth_verifier");
				try {
					new AsyGetData(getApplicationContext(), oauthVerifier).execute();

				} catch (Exception e) {
					// TODO: handle exception
				}
				return true;
			}
			return false;
		}
	}

	public void onCloseClicked(View v) {
		finish();
	}
}
