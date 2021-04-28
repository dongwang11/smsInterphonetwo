package com.sms.app.framework.communication.localayer.bledriver;


import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.framework.trans.bean.Interphone;

public class GlobalConsts {

	public static final byte YES=1;
	public static final byte NO=0;


	//Log 打印
	public static final String TAG="YanShi...Log";
	public static final String ACTION_SHOW_GROUP_CHAT_MESSAGE = "ACTION_SHOW_GROUP_CHAT_MESSAGE";


	//常量
	public static final String DATE_FROM_NAME="DATE_FROM_NAME";

	public static final String DATE_FROM_ID="DATE_FROM_ID";
	public static final String DATE_FROM_UUID="DATE_FROM_UUID";

	public static final String ACTION_LOGIN="action_login";
	public static final String ACTION_BASE="action_base";

	public static final String ACTION_IM_CODE="action_imcode";

	public static final String ACTION_IM_GROUP="action_imgroup";





	public static final String ACTION_MAP="action_map";
	public static final String ACTION_CHAT="action_chat";

	public static final String ACTION_USER="action_user";

	public static final String ACTION_OPENFIRE="action_openfire";

	public static final String ACTION_MSG="action_msg";

	public static final String ACTION_TYPE = "action_type";

	public static final String ACTION_FAQ = "action_faq";

	public static final String ACTION_ABOUT = "action_about";

	public static final String ACTION_APK = "action_apk";



	public static final int ACTTION_LOGIN_FREQ = 10;

	public static final int ACTTION_LOGIN_SUCCESS = 0;

	public static final int ACTTION_LOGIN_ERRO = 1;

	public static final int ACTTION_IS_OPENFIRE = 1;

	public static final int ACTTION_IS_MSG = 2;
	public static final int ACTTION_IS_MSG_OK = 8;
	public static final int ACTTION_IS_MSG_PLAY = 9;



	public static final int ACTTION_IS_START = 3;
	public static final int ACTTION_IS_SCAN = 4;
	public static final int ACTTION_START = 5;
	public static final int ACTTION_ERRO_HITE = 6;
	public static final int ACTTION_ERRO_SHOW = 7;










	public static final String ACTION_NEWQUN="ACTION_newqun";

	public static final int ACTTION_DATE=10;
	public static final int ACTTION_DESTROY_YES=20;
	public static final int ACTTION_DESTROY_NO=30;


	/**
	 * 权限相关常量
	 * */


	//调用相机权限
	public static final int CAMERA = 1;
	//录音权限
	public static final int RECORD_AUDIO = 2;
	//定位权限
	public static final int ACCESS_FINE_LOCATION = 3;
	//读写SD卡权限
	public static final int WRITE_EXTERNAL_STORAGE = 4;

	//安装APK权限
	public static final int INSTALL = 5;







	// SharedPreferences 配置信息

	public static final String USER = "user";
	public static final String USER_ID = "id";
	public static final String USER_KEY = "passkey";
	public static final String GROUPHITE = "grouphite";
	public static final String GroupName = "group_name";
	public static final String GroupHite = "group_hite";



	public static final String PRECURSOR = "precursor";

	public static final String PRECURSOR_ADV_VERSION = "adv_version";
	public static final String PRECURSOR_ADV_URL = "adv_url";


	public static final String PRECURSOR_FIRMWARE_VERSION = "firmware_version";
	public static final String PRECURSOR_FIRMWARE_URL = "firmware_url";

	public static final String PRECURSOR_USER_GUI_VERSION = "user_gui_version";
	public static final String PRECURSOR_USER_GUI_URL = "user_gui_url";

	public static final String PRECURSOR_QA_VERSION = "qa_version";
	public static final String PRECURSOR_QA_URL = "qa_url";




	public static final String PRECURSOR_ANDROID_URL = "android_url";
	public static final String PRECURSOR_ANDROID_VERSION = "android_version";
	public static final String PRECURSOR_ANDROID_DESCRIPTION = "android_description";





	public static final String DEVICE = "device";
	public static final String DEVICE_NAME = "name";
	public static final String DEVICE_ADDRESS = "address";



	/**
	 * Intent 相关常量
	 * */
	public static final String GROUP_NAME = "GROUP_NAME";
	public static final String GROUP_ID = "GROUP_ID";




	public static final int VOICE = 0;
	public static final int VOICE_SIZE = 1;
	public static final int VOICE_BLE = 2;
	public static final int VOICE_CODE = 3;






	/**
	 *
	 * 控制连接或者断开相应常量
	 *
	 * */

	public static void connect(boolean isConnect, Interphone interphone){


		LogUtil.i(TAG,"connect:"+isConnect);

		if(isConnect){


			MyApplicatoin.IS_BLE = isConnect;

		//	MyApplicatoin.interphone = interphone;




		}else{

			MyApplicatoin.IS_BLE = isConnect;

			MyApplicatoin.interphone = null;

			MyApplicatoin.openfrom = null;
		}
	}

}


