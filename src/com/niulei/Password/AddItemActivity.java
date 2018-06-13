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

public class AddItemActivity extends Activity implements OnClickListener{
	public static final String OPT_MODIFY_OR_ADD = "modify_or_add"; //0 添加 1 修改
	public static final String OPT_MODIFY_INDEX = "modify_index";  //当前修改项

	private byte bModifyFlag = 0;
	private int iModifyIndex = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.additem);

		Button btnOK = (Button)findViewById(R.id.btnOK);
		btnOK.setOnClickListener(this);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		bModifyFlag = bundle.getByte(OPT_MODIFY_OR_ADD);
		if(bModifyFlag == 1){
			iModifyIndex = bundle.getInt(OPT_MODIFY_INDEX);
		}
		
		if(bModifyFlag == 1){
			ContentManager contentmanager = ContentManager.getInstance();
			EditText edtTitle = (EditText)findViewById(R.id.edtAddTitle);
			EditText edtUsername = (EditText)findViewById(R.id.edtAddUsername);
			EditText edtPassword = (EditText)findViewById(R.id.edtAddPassword);
			edtTitle.setText(contentmanager.getTitle(iModifyIndex));
			edtUsername.setText(contentmanager.getUsername(iModifyIndex));
			edtPassword.setText(contentmanager.getPassword(iModifyIndex));
		}
		//updateBackgroundDrawable();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.btnOK:
			EditText edtTitle = (EditText)findViewById(R.id.edtAddTitle);
			EditText edtUsername = (EditText)findViewById(R.id.edtAddUsername);
			EditText edtPassword = (EditText)findViewById(R.id.edtAddPassword);
			String strTitle = edtTitle.getText().toString();
			String strUsername = edtUsername.getText().toString();
			String strPassword = edtPassword.getText().toString();
			
			if(strTitle != null && strTitle != "" && strUsername != null && strUsername != ""
					&& strPassword != null && strPassword != ""){
				ContentManager contentManager = ContentManager.getInstance();
				if(bModifyFlag == 0){
					contentManager.addItem(strTitle, strUsername, strPassword);
				}
				else{
					contentManager.setTitle(iModifyIndex, strTitle);
					contentManager.setUsername(iModifyIndex, strUsername);
					contentManager.setPassword(iModifyIndex, strPassword);
				}
				contentManager.writeData();
				this.finish();
			}
			else
			{
				Toast.makeText(this, R.string.IncompleteInfo, Toast.LENGTH_LONG).show();
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
