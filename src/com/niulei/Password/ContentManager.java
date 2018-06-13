package com.niulei.Password;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import android.content.Context;
import android.os.Environment;
import android.util.Log;


public class ContentManager{	
	private String strFilePath = null;
	private PswContent pswcontent = null;
	private static ContentManager instance = null;
	private String strBgPath = null;
	private String strExportFilePath = null;
	
	public static synchronized ContentManager getInstance(){
		if(instance == null){
			instance = new ContentManager();
		}
		return instance;
	}
	
	public boolean init(Context context){
		File fileRoot = context.getFilesDir();
		File fileExtRoot = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		strFilePath = fileExtRoot.getPath()+"/rem.data";
		strBgPath = fileExtRoot.getPath()+"/bg.png";
		strExportFilePath = fileExtRoot.getPath()+"/rem.data";
		Log.d("niulei", strFilePath);
		pswcontent = new PswContent();
		return true;
	}
	
	//从文件中读取内容
	@SuppressWarnings("resource")
	public int readData(){
		FileInputStream fileinputstream = null;
		byte[] decryptData = null;
		byte[] encryptData = null;
		try {
			fileinputstream = new FileInputStream(new File(strFilePath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(fileinputstream != null){
			ObjectInputStream objInputStream = null;
			try {
				objInputStream = new ObjectInputStream(fileinputstream);
			} catch (StreamCorruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				encryptData = (byte[])objInputStream.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OptionalDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(encryptData != null){
			try {
				decryptData = MyEncryptor.decrypt(encryptData);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(decryptData != null){
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decryptData);
			ObjectInputStream objInputStream = null;
			try {
				objInputStream = new ObjectInputStream(byteArrayInputStream);
			} catch (StreamCorruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(objInputStream != null){
				try {
					pswcontent = (PswContent)objInputStream.readObject();
				} catch (OptionalDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return 0;
	}
	
	//将数据写入到文件中
	@SuppressWarnings("resource")
	public int writeData(){		
		FileOutputStream fileoutputstream = null;
		byte[] encryptData = null;
		byte[] decryptData = null;
		try {
			fileoutputstream = new FileOutputStream(new File(strExportFilePath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		if(byteArrayOutputStream != null && pswcontent != null){
			ObjectOutputStream objOutputStream = null;
			try {
				objOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				objOutputStream.writeObject(pswcontent);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		decryptData = byteArrayOutputStream.toByteArray();
		try {
			encryptData = MyEncryptor.encrypt(decryptData);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(fileoutputstream != null){
			ObjectOutputStream objOutputStream = null;
			try {
				objOutputStream = new ObjectOutputStream(fileoutputstream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				objOutputStream.writeObject(encryptData);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		return 0;
	}
	
	public int addItem(String strTitle, String strUserName, String strPassword){
		pswcontent.strTitleArray.add(strTitle);
		pswcontent.strUsernameArray.add(strUserName);
		pswcontent.strPasswordArray.add(strPassword);
		pswcontent.iNumOfItem++;
		return 0;
	}
	
	public int deleteItem(int i){
		if(pswcontent.iNumOfItem > 0 && i >= 0 && i < pswcontent.iNumOfItem){
			pswcontent.strTitleArray.remove(i);
			pswcontent.strUsernameArray.remove(i);
			pswcontent.strPasswordArray.remove(i);
			pswcontent.iNumOfItem--;
		}
		return 0;
	}
	
	public int getNumOfItem(){
		if(pswcontent != null){
			return pswcontent.iNumOfItem;
		}
		return 0;
	}
	
	public String getTitle(int index){
		if(pswcontent != null){
			return pswcontent.strTitleArray.get(index);
		}
		return null;
	}
	public String getUsername(int index){
		if(pswcontent != null){
			return pswcontent.strUsernameArray.get(index);
		}
		return null;
	}
	public String getPassword(int index){
		if(pswcontent != null){
			return pswcontent.strPasswordArray.get(index);
		}
		return null;
	}
	public int setTitle(int index, String strText){
		if(pswcontent != null && pswcontent.iNumOfItem > 0 && index >= 0 && index < pswcontent.iNumOfItem){
			pswcontent.strTitleArray.set(index, strText);
			return 0;
		}
		return -1;
	}
	public int setUsername(int index, String strText){
		if(pswcontent != null && pswcontent.iNumOfItem > 0 && index >= 0 && index < pswcontent.iNumOfItem){
			pswcontent.strUsernameArray.set(index, strText);
			return 0;
		}
		return -1;
	}
	public int setPassword(int index, String strText){
		if(pswcontent != null && pswcontent.iNumOfItem > 0 && index >= 0 && index < pswcontent.iNumOfItem){
			pswcontent.strPasswordArray.set(index, strText);
			return 0;
		}
		return -1;
	}
	
	public String getLicense(){
		if(pswcontent != null){
			return pswcontent.strLincense;
		}
		return null;
	}
	
	public void SetLicense(String strLicense){
		if(pswcontent != null){
			pswcontent.strLincense = strLicense;
		}
	}
	public void clear(){
		pswcontent.iNumOfItem = 0;
		pswcontent.strLincense = null;
		pswcontent.strTitleArray.clear();
		pswcontent.strUsernameArray.clear();
		pswcontent.strPasswordArray.clear();
	}
	
	public String getBgImagePath(){
		return strBgPath;
	}
	
	public void SetBgImagePath(String strPath){
		strBgPath = strPath;
	}
}
