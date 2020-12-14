package cn.katasea.util;

public class PasswordHelper {

	public static String encryptPassword(String password) {
		String newPassword = null;
		try {
			newPassword = AESUtil.encrypt(password,AESUtil.encrypt(password,password));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newPassword;
	}
}
