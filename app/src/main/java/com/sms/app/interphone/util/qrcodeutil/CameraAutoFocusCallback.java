/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sms.app.interphone.util.qrcodeutil;

import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;

/**
 * @author VincenT Li
 *	重写安卓照相机聚焦，使用指定频率进行自动对焦
 */
final class CameraAutoFocusCallback implements Camera.AutoFocusCallback {

  private static final long AUTOFOCUS_INTERVAL_MS = 1500L;//聚焦间隔太短有些手机不支持
  private Handler autoFocusHandler;
  private int autoFocusMessage;
  /**初始化聚焦handler，传递聚焦message*/
  void setHandler(Handler autoFocusHandler, int autoFocusMessage) {
    this.autoFocusHandler = autoFocusHandler;
    this.autoFocusMessage = autoFocusMessage;
  }
  /**聚焦时间频率
   * @param success 聚焦成功
   * @param camera 照相机调用
   * */
  public void onAutoFocus(boolean success, Camera camera) {
    if (autoFocusHandler != null) {
      Message message = autoFocusHandler.obtainMessage(autoFocusMessage, success);
      autoFocusHandler.sendMessageDelayed(message, AUTOFOCUS_INTERVAL_MS);
      autoFocusHandler = null;
    } else {
    }
  }

}
