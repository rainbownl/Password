package com.niulei.Password;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetPasswordActivity extends Activity implements OnClickListener{
	public static final String OPT_FIRSTIN = "first_in";
	private byte bMode = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_password);
		
		Button btnSubmit = (Button)findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(this);
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		bMode = bundle.getByte(OPT_FIRSTIN);
		if(bMode == 1){
			Toast.makeText(this, R.string.SetPasswordTip, Toast.LENGTH_LONG).show();
		}
		//updateBackgroundDrawable();
	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId())
		{
		case R.id.btnSubmit:
			EditText edtSet1 = (EditText)findViewById(R.id.edtPassword);
			EditText edtSet2 = (EditText)findViewById(R.id.edtPassword2);
			String str1 = edtSet1.getText().toString();
			String str2 = edtSet2.getText().toString();
			if(str1 != null && str1.length() <= 0){
				Toast.makeText(this, R.string.InputPasswordEmpty, Toast.LENGTH_LONG).show();
			}
			else if(str1.equals(str2))
			{
				//SharedPreferences.Editor editor = getSharedPreferences("OPT_PASSWORD", MODE_PRIVATE).edit();
				//editor.putString("password", str1);
				//editor.commit();
				ContentManager contentmanager = ContentManager.getInstance();
				contentmanager.SetLicense(str1);
				contentmanager.writeData();
				if(bMode == 1 || bMode == 2){
					Intent intent = new Intent(this, ContentActivity.class);
					startActivity(intent);
				}
				this.finish();
			}
			else
			{
				Toast.makeText(this, R.string.RepeatPasswordErr, Toast.LENGTH_LONG).show();
			}
			break;
		}
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
