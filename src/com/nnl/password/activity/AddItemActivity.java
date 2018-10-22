package com.nnl.password.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nnl.password.content.ContentItem;
import com.nnl.password.R;
import com.nnl.password.content.DBContentManager;

public class AddItemActivity extends Activity implements OnClickListener{
	public static final String OPT_MODIFY_OR_ADD = "modify_or_add"; //0 添加 1 修改
	public static final String OPT_MODIFY_INDEX = "modify_index";  //当前修改项

	private byte bModifyFlag = 0;
	private int iModifyIndex = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.additem);

		Button btnOK = findViewById(R.id.btnOK);
		btnOK.setOnClickListener(this);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			bModifyFlag = bundle.getByte(OPT_MODIFY_OR_ADD);
			if (bModifyFlag == 1) {
				iModifyIndex = bundle.getInt(OPT_MODIFY_INDEX);
			}
		}
		
		if(bModifyFlag == 1){
			DBContentManager contentManager = DBContentManager.Companion.getInstance(getApplicationContext());
			EditText edtTitle = findViewById(R.id.edtAddTitle);
			EditText edtUsername = findViewById(R.id.edtAddUsername);
			EditText edtPassword = findViewById(R.id.edtAddPassword);

			ContentItem item = contentManager.getItem(iModifyIndex);
			edtTitle.setText(item.getTitle());
			edtUsername.setText(item.getUserName());
			edtPassword.setText(item.getPassword());
		}
		//updateBackgroundDrawable();
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.btnOK:
			EditText edtTitle = findViewById(R.id.edtAddTitle);
			EditText edtUsername = findViewById(R.id.edtAddUsername);
			EditText edtPassword = findViewById(R.id.edtAddPassword);
			String strTitle = edtTitle.getText().toString();
			String strUsername = edtUsername.getText().toString();
			String strPassword = edtPassword.getText().toString();
			
			if(strTitle != "" && !strUsername.equals("")
					&& !strPassword.equals("")){
				DBContentManager contentManager = DBContentManager.Companion.getInstance(getApplicationContext());
				if(bModifyFlag == 0){
					contentManager.insert(strTitle, strUsername, strPassword);
				}
				else{
					ContentItem item = contentManager.getItem(iModifyIndex);
					contentManager.update(item.getId(), strTitle, strUsername, strPassword);
				}
				this.finish();
			}
			else
			{
				Toast.makeText(this, R.string.IncompleteInfo, Toast.LENGTH_LONG).show();
			}
			break;
		}
	}
}
