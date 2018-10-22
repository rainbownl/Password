package com.nnl.password.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.nnl.password.R;
import com.nnl.password.content.DBContentManager;

public class ContentActivity extends Activity {
	private static final int MENU_GROUP_ID = 0;
	private static final int MENU_ID_ADD = 0;
	private static final int MENU_ID_MODIFY_PSW = 1;
	private static final int MENU_ID_SETBG = 2;
	private static final int MENU_ID_ABOUT = 3;
	private static final int FILE_SELECT_CODE = 100;
	private long exitTime = 0;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(MENU_GROUP_ID, MENU_ID_ADD, MENU_ID_ADD, R.string.AddItem);
		menu.add(MENU_GROUP_ID, MENU_ID_MODIFY_PSW, MENU_ID_MODIFY_PSW, R.string.ChangePassword);
		//menu.add(MENU_GROUP_ID, MENU_ID_SETBG, MENU_ID_SETBG, R.string.SetBackground);
		menu.add(MENU_GROUP_ID, MENU_ID_ABOUT, MENU_ID_ABOUT, R.string.About);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()){
			case MENU_ID_ADD:
			{
				Intent intent = new Intent(this, AddItemActivity.class);
				Bundle bundle = new Bundle();
				bundle.putByte(AddItemActivity.OPT_MODIFY_OR_ADD, (byte) 0);
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			}
			case MENU_ID_MODIFY_PSW:
			{
				Intent intent = new Intent(this, SetPasswordActivity.class);
				Bundle bundle = new Bundle();
				bundle.putByte(SetPasswordActivity.OPT_FIRSTIN, (byte)0);
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			}
			case MENU_ID_SETBG:
			{
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/");
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				startActivityForResult(Intent.createChooser(intent, "请选择一张图片"), FILE_SELECT_CODE);
				break;
			}
			case MENU_ID_ABOUT:
			{
				new AlertDialog.Builder(this).setTitle(R.string.About).setMessage(R.string.AboutMessage)
					.setPositiveButton(R.string.IKnow, null).show();
			}
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content);
		
		ListView listview = findViewById(R.id.listView);
		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.content_listview, getData());
		//listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View view, int position,
					long id) {
				Intent intent = new Intent(ContentActivity.this, ItemDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt(ItemDetailActivity.OPT_DETAIL_INDEX, position);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		if(DBContentManager.Companion.getInstance(getApplicationContext()).getCount() <= 0){
			Toast.makeText(this, R.string.AddRecordTip, Toast.LENGTH_LONG).show();
		}
		//updateBackgroundDrawable();
	}

	List<String> getData()
	{
		List<String> data = new ArrayList<String>();
		DBContentManager contentmanager = DBContentManager.Companion.getInstance(getApplicationContext());
		for(int i = 0; i < contentmanager.getCount(); i++){
			data.add(contentmanager.getItem(i).getTitle());
		}
		return data;
	}


	@Override
	protected void onResume() {
		super.onResume();
		ListView listview = findViewById(R.id.listView);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.content_listview, R.id.listViewItem_text, getData());
		listview.setAdapter(adapter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
			if(System.currentTimeMillis() - exitTime > 2000){
				Toast.makeText(this, R.string.QuitAppTips, Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			}
			else{
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
			case FILE_SELECT_CODE:
			{
				if(resultCode == RESULT_OK){
					Uri uri = data.getData();
					if(uri != null){
						/*String path = uri.getPath();
						if(path != null){
							if(uri.getScheme().equals("content")){
								String[] proj = {MediaStore.Images.Media.DATA};
								Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
								if(cursor != null){
									cursor.moveToFirst();
									int columnIdx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
									if(columnIdx >= 0){
										ContentManager.getInstance().SetBgImagePath(cursor.getString(columnIdx));
									}
								}
							}
						}*/
						DisplayMetrics metrics = new DisplayMetrics(); 
						getWindowManager().getDefaultDisplay().getMetrics(metrics);

					}	
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
