package com.nnl.password.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.nnl.password.content.ContentItem;
import com.nnl.password.R;
import com.nnl.password.content.DBContentManager;

public class ItemDetailActivity extends Activity implements OnClickListener{
	public static final String OPT_DETAIL_INDEX = "itemDetailIndex";
	private int index = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itemdetail);
		Button btnEdit = findViewById(R.id.btnDetailEdit);
		btnEdit.setOnClickListener(this);
		Button btnDelete = findViewById(R.id.btnDetailDelete);
		btnDelete.setOnClickListener(this);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			index = bundle.getInt(OPT_DETAIL_INDEX);
		}
		//updateBackgroundDrawable();
	}

	@Override
	public void onClick(View view) {
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
						DBContentManager contentmanager = DBContentManager.Companion.getInstance(getApplicationContext());
						contentmanager.delete(contentmanager.getItem(index).getTitle());
						ItemDetailActivity.this.finish();
					}
				})
				.setNegativeButton(R.string.DonotDelete, null)
				.show();
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		DBContentManager contentManager = DBContentManager.Companion.getInstance(getApplicationContext());
		if(contentManager.getCount() > 0 && index >= 0 && index < contentManager.getCount()){
			ContentItem item = contentManager.getItem(index);

			TextView tvUsername = findViewById(R.id.tvDetailUsername);
			tvUsername.setText(item.getUserName());

			TextView tvPassword = findViewById(R.id.tvDetailPassword);
			tvPassword.setText(item.getPassword());

			TextView tvTitle = findViewById(R.id.textView_Title);
			if (tvTitle != null){
				tvTitle.setText(item.getTitle());
			}
		}
	}
}
