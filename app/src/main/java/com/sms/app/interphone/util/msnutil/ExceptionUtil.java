package com.sms.app.interphone.util.msnutil;

import android.os.Environment;
import com.sms.app.interphone.ui.MyApplicatoin;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {

	private static final String sendEmail = "18576525292@163.com";
	private static final String toEmail = "970158444@qq.com";
	private static final String emailCode = "yanshi123456";


	public static void handleException(Exception e) {

		if (MyApplicatoin.isRelease) {

			final String phone = PhoneManager.getPhoneManager();

			StringWriter stringWriter=new StringWriter();
			PrintWriter printWriter=new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			final String string = stringWriter.toString();

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						//创建HtmlEmail类
						HtmlEmail email = new HtmlEmail();

						email.setHostName("smtp.163.com");
						// 字符编码集的设置
						email.setCharset("utf-8");
						// 收件人的邮箱
						email.addTo(toEmail);
						// 发送人的邮箱2
						email.setFrom(sendEmail,sendEmail);
						// 如果需要认证信息的话，设置认证：用户名-密码     ***是你开启POP3服务时的授权码，不是登录密码
						email.setAuthentication(sendEmail, emailCode);
						// 要发送的邮件主题
						email.setSubject("Android-SMS-Exception");
						// 要发送的信息，由于使用了HtmlEmail，可以在邮件内容中使用HTML标签
						email.setMsg(phone+"\n"+string);
						// 发送
						email.send();

					} catch (EmailException e1) {
						e1.printStackTrace();
					}
				}
			}).start();

		}else{

			e.printStackTrace();

		}
	}

	private static void OutException(String string) {

		String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

		File toFile = new File(path,"/SMSException.txt");

		FileOutputStream fos = null;

		try {

			if(!toFile.exists()){
				boolean newFile = toFile.createNewFile();
			}

			byte[] data = string.getBytes();

			fos = new FileOutputStream(toFile);

			fos.write(data,0,data.length);

			fos.flush();

		} catch (IOException e) {

			ExceptionUtil.handleException(e);

		}finally {

			try {
				fos.close();
			} catch (IOException e) {
				ExceptionUtil.handleException(e);
			}
		}
	}
}
