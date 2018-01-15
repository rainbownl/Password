package com.niulei.Password;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ContentActivity extends Activity {
	private static final int MENU_GROUP_ID = 0;
	private static final int MENU_ID_ADD = 0;
	private static final int MENU_ID_MODIFY_PSW = 1;
	private static final int MENU_ID_SETBG = 2;
	private static final int MENU_ID_ABOUT = 3;
	private static final int FILE_SELECT_CODE = 100;
	private static final int IMAGE_CROP_CODE = 101;
	private long exitTime = 0;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(MENU_GROUP_ID, MENU_ID_ADD, MENU_ID_ADD, R.string.AddItem);
		menu.add(MENU_GROUP_ID, MENU_ID_MODIFY_PSW, MENU_ID_MODIFY_PSW, R.string.ChangePassword);
		//menu.add(MENU_GROUP_ID, MENU_ID_SETBG, MENU_ID_SETBG, R.string.SetBackground);
		menu.add(MENU_GROUP_ID, MENU_ID_ABOUT, MENU_ID_ABOUT, R.string.About);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
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
				startActivityForResult(Intent.createChooser(intent, "«Î—°‘Ò“ª’≈Õº∆¨"), FILE_SELECT_CODE);
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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content);
		
		ListView listview = (ListView)findViewById(R.id.listView);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getData());
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ContentActivity.this, ItemDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt(ItemDetailActivity.OPT_DETAIL_INDEX, position);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		if(ContentManager.getInstance().getNumOfItem() <= 0){
			Toast.makeText(this, R.string.AddRecordTip, Toast.LENGTH_LONG).show();
		}
		//updateBackgroundDrawable();
	}

	List<String> getData()
	{
		List<String> data = new ArrayList<String>();
		ContentManager contentmanager = ContentManager.getInstance();
		for(int i = 0; i < contentmanager.getNumOfItem(); i++){
			data.add(contentmanager.getTitle(i));
		}
		return data;
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ListView listview = (ListView)findViewById(R.id.listView);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getData());
		listview.setAdapter(adapter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
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
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
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
						
						File fileBg = new File(ContentManager.getInstance().getBgImagePath());
						if(fileBg.exists()){
							fileBg.delete();
						}
						Uri uriSave = Uri.fromFile(fileBg);
						if(uriSave != null){
							Intent intent = new Intent();
							intent.setAction("com.android.camera.action.CROP");
							intent.setDataAndType(uri, "image/");
							intent.putExtra("crop", true);
							intent.putExtra("aspectX", metrics.widthPixels);
							intent.putExtra("aspectY", metrics.heightPixels);
							intent.putExtra("outputX", metrics.widthPixels);
							intent.putExtra("outputY", metrics.heightPixels);
							intent.putExtra("scale", true);
							intent.putExtra("scaleUpIfNeeded", true);
							intent.putExtra("return-data", false);
							intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSave);
							intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
							startActivityForResult(intent, IMAGE_CROP_CODE);
						}
					}	
				}
				updateBackgroundDrawable();
				break;
			}
			case IMAGE_CROP_CODE:
			{
				if(resultCode == RESULT_OK){
					updateBackgroundDrawable();
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
