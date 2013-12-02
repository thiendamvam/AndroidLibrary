package com.gso.facebookframework.view;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.facebookframework.R;
import com.gso.facebookframework.Config;
import com.gso.facebookframework.FacebookHandler;
import com.gso.facebookframework.share.ShareData;
import com.gso.facebookframework.share.ShareHandler;

public class SharePopup extends DialogFragment {

	private Button btnSend;
	private EditText etContent;
	private Context context;
	private ImageButton btnClose;
	private ProgressBar prgress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE, DialogFragment.STYLE_NO_TITLE);
		setStyle(DialogFragment.STYLE_NO_FRAME, DialogFragment.STYLE_NORMAL);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		float desity = context.getResources().getDisplayMetrics().density;
		getDialog().getWindow().setLayout(LayoutParams.MATCH_PARENT, (int)(200*desity));
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		context = activity.getApplicationContext();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.share_view, container, false);
		btnSend = (Button) v.findViewById(R.id.btnSent);
		btnClose = (ImageButton)v.findViewById(R.id.btnClose);
		etContent = (EditText) v.findViewById(R.id.etContent);
		prgress = (ProgressBar)v.findViewById(R.id.prgress);
		btnSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				exeSendClicked();
			}
		});
		btnClose.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		
		return v;
	}

	protected void exeSendClicked() {
		// TODO Auto-generated method stub
		if (etContent.getText() != null) {
			setProgressBar(true);
			new ASynShare(etContent.getText().toString()).execute(null, null);

		} else {

		}
	}

	private void setProgressBar(boolean b) {
		// TODO Auto-generated method stub
		prgress.setVisibility(b?View.VISIBLE:View.GONE);
	}

	class ASynShare extends AsyncTask<Void, Boolean, Boolean> {

		
		private String message;
		private ShareHandler shareHdler;

		public ASynShare(String mes) {
			// TODO Auto-generated constructor stub
			this.message = mes;
			shareHdler = new ShareHandler(context);
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			
			String accessToken = new FacebookHandler(context, getActivity(), Config.appId).getFacebookToken();
			ShareData data = new ShareData();
			data.setMessage(message);
			return shareHdler.postFacebook(accessToken, data, Config.appId);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			setProgressBar(false);
//			if (result) {
//				Toast.makeText(getActivity(), "Sucessful", Toast.LENGTH_LONG)
//						.show();
//			} else {
//				Toast.makeText(getActivity(), "Failure"+Config.appId, Toast.LENGTH_LONG).show();
//			}
		}

	}
}
