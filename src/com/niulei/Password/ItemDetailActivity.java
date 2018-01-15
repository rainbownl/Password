package com.niulei.Password;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ItemDetailActivity extends Activity implements OnClickListener{
	public static final String OPT_DETAIL_INDEX = "itemDetailIndex";
	private int index = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itemdetail);
		Button btnEdit = (Button)findViewById(R.id.btnDetailEdit);
		btnEdit.setOnClickListener(this);
		Button btnDelete = (Button)findViewById(R.id.btnDetailDelete);
		btnDelete.setOnClickListener(this);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		index = bundle.getInt(OPT_DETAIL_INDEX);
		ContentManager contentManager = ContentManager.getInstance();
		if(contentManager.getNumOfItem() > 0 && index >= 0 && index < contentManager.getNumOfItem()){
			TextView tvUsername = (TextView)findViewById(R.id.tvDetailUsername);
			tvUsername.setText(contentManager.getUsername(index));
			
			TextView tvPassword = (TextView)findViewById(R.id.tvDetailPassword);
			tvPassword.setText(contentManager.getPassword(index));
		}
		//updateBackgroundDrawable();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.btnDetailEdit:
			Intent intent = new Intent(this, AddItemActivity.class);
			Bundle bundle = new Bundle();
			bundle.putByte(AddItemActivity.OPT_MODIFY_OR_ADD, (byte)1);
			bundle.putInt(AddItemActivity.OPT_MODIFY_INDEX, index);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.btnDetailDelete:
			new AlertDialog.Builder(this).setTitle(R.string.Delete).setMessage(R.string.DeleteItemWarning)
				.setPositiveButton(R.string.Delete,  new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dlg, int witch) {
						// TODO Auto-generated method stub
						ContentManager contentmanager = ContentManager.getInstance();
						contentmanager.deleteItem(index);
						contentmanager.writeData();
						ItemDetailActivity.this.finish();
					}
				})
				.setNegativeButton(R.string.DonotDelete, null)
				.show();
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
