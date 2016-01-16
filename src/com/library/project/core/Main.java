/**
 * 
 */
package com.library.project.core;

import com.library.project.helper.DBHelper;
import com.library.project.modules.LoginScreen;

/**
 * @author darshanbidkar
 * 
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		new LoginScreen();
		Runtime.getRuntime().addShutdownHook(DBHelper.sShutDownThread);
	}

}
