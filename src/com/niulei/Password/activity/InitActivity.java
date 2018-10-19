package com.niulei.password.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.niulei.password.content.DBContentManager;

public class InitActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DBContentManager contentManager = DBContentManager.Companion.getInstance(getApplicationContext());
		contentManager.updateData();
		//updateBackgroundDrawable();
		if(contentManager.getLicense() == null)
		{
			Intent intent = new Intent(this, SetPasswordActivity.class);
			Bundle bundle = new Bundle();
			bundle.putByte(SetPasswordActivity.OPT_FIRSTIN, (byte)1);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		else
		{
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		}
		finish();
	}
}
