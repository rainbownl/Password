package com.nnl.password.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nnl.password.R;
import com.nnl.password.content.DBContentManager;

public class LoginActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	private Button btnSubmit = null;
	private String strLicense = null;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        btnSubmit = findViewById(R.id.buttonSubmit);
        if(btnSubmit != null){
        	btnSubmit.setOnClickListener(this);
        }
        
        Button btnForgetPassword = findViewById(R.id.buttonForgetPassword);
        if(btnForgetPassword != null){
        	btnForgetPassword.setOnClickListener(this);
        }
	}
	@Override
	public void onClick(View view) {
		switch(view.getId())
		{
		case R.id.buttonSubmit:
			EditText edtPassword = findViewById(R.id.editText);
			if(checkLicense(edtPassword.getText().toString())== true)
			{
				//Intent intent = new Intent(this, ContentActivity.class);
				Intent intent = new Intent(this, RecyclerContentActivity.class);
				startActivity(intent);
				finish();
			}
			else
			{
				Toast.makeText(this, R.string.WrongLicense, Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.buttonForgetPassword:
			new AlertDialog.Builder(this).setTitle(R.string.ForgetPassword).setMessage(R.string.ResetPasswordWarning)
				.setPositiveButton(R.string.ResetPassword, new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						DBContentManager.Companion.getInstance(getApplicationContext()).clear();
						Intent intent = new Intent(LoginActivity.this, SetPasswordActivity.class);
						Bundle bundle = new Bundle();
						bundle.putByte(SetPasswordActivity.OPT_FIRSTIN, (byte)2);
						intent.putExtras(bundle);
						startActivity(intent);
						finish();
					}
					
				})
				.setNegativeButton(R.string.DonotResetPassword, null).show();
			break;
		default:
			break;
		}
	}
	
	private Boolean checkLicense(String strText){
		//strLicense = getSharedPreferences("OPT_PASSWORD", MODE_PRIVATE).getString("password", null);

		strLicense = DBContentManager.Companion.getInstance(getApplicationContext()).getLicense();
		if(strLicense.equals(strText))
		{
			return true;
		}
		return false;
	}
}