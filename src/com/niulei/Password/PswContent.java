package com.niulei.Password;

import java.io.Serializable;
import java.util.ArrayList;

public class PswContent implements Serializable{
	public String strLincense = null;
	private static final long serialVersionUID = 1L;
	public int iNumOfItem;
	public ArrayList<String> strTitleArray;
	public ArrayList<String> strUsernameArray;
	public ArrayList<String> strPasswordArray;
	public PswContent(){
		iNumOfItem = 0;
		strTitleArray = new ArrayList<String>();
		strUsernameArray = new ArrayList<String>();
		strPasswordArray = new ArrayList<String>();
	}
}