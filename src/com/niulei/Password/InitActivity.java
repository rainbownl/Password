package com.niulei.Password;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

public class InitActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ContentManager contentManager = ContentManager.getInstance();
		contentManager.init(this);
		contentManager.readData();
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
			Intent intent = new Intent(this, PasswordActivity.class);
			startActivity(intent);
		}
		finish();
	}
	private void updateBackgroundDrawable(){
		String strPath = ContentManager.getInstance().getBgImagePath();
		int iResult = 1;
		if(strPath == null){
			Drawable drawable = this.getResources().getDrawable(R.drawable.background_img);
			if(drawable != null){
				getWindow().setBackgroundDrawable(drawable);
				iResult = 0;
			}
		}
		if(iResult != 0){
			Drawable drawable = Drawable.createFromPath(strPath);
			if(drawable != null){
				getWindow().setBackgroundDrawable(drawable);
			}
		}
	}
}
