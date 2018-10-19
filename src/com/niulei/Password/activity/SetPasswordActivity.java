package com.niulei.password.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.niulei.password.R;
import com.niulei.password.content.DBContentManager;

public class SetPasswordActivity extends Activity implements OnClickListener{
	public static final String OPT_FIRSTIN = "first_in";
	private byte bMode = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_password);
		
		Button btnSubmit = findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(this);
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			bMode = bundle.getByte(OPT_FIRSTIN);
		}
		if(bMode == 1){
			Toast.makeText(this, R.string.SetPasswordTip, Toast.LENGTH_LONG).show();
		}

		EditText edtSet2 = findViewById(R.id.edtPassword2);
		if (edtSet2 != null){
			edtSet2.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {

				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					EditText edtSet = findViewById(R.id.edtPassword);
					if (edtSet != null){
						String s1 = edtSet.getText().toString();
						ImageView imageCheck = findViewById(R.id.imgViewPassMatch);
						if (s1.compareTo(s.toString()) == 0){
							imageCheck.setVisibility(View.VISIBLE);
						}else{
							imageCheck.setVisibility(View.INVISIBLE);
						}
					}
				}

				@Override
				public void afterTextChanged(Editable s) {

				}
			});
		}
		//updateBackgroundDrawable();
	}
	@Override
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.btnSubmit:
			EditText edtSet1 = findViewById(R.id.edtPassword);
			EditText edtSet2 = findViewById(R.id.edtPassword2);
			String str1 = edtSet1.getText().toString();
			String str2 = edtSet2.getText().toString();
			if(str1 != null && str1.length() <= 0){
				Toast.makeText(this, R.string.InputPasswordEmpty, Toast.LENGTH_LONG).show();
			}
			else if(str1.equals(str2))
			{
				DBContentManager contentmanager = DBContentManager.Companion.getInstance(getApplicationContext());
				contentmanager.writeLicense(str1);
				if(bMode == 1 || bMode == 2){
					//Intent intent = new Intent(this, ContentActivity.class);
					Intent intent = new Intent(this, RecyclerContentActivity.class);
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
}
