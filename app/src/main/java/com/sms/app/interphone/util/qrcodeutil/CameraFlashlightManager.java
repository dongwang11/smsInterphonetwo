package com.sms.app.interphone.util.qrcodeutil;

import android.os.IBinder;

import java.lang.reflect.Method;

/**
 * @author VincenT Li
 *	这个类是用来管理手机闪光灯
 */
final class CameraFlashlightManager {

  private static final Object iHardwareService;
  private static final Method setFlashEnabledMethod;
  static {
    iHardwareService = getHardwareService();
    setFlashEnabledMethod = getSetFlashEnabledMethod(iHardwareService);
    if (iHardwareService == null) {
    	//支持
    } else {
    	//不支持
    }
  }
  /**开启闪光灯*/
  public static void enableFlashlight() {
    setFlashlight(true);
  }
  /**关闭闪光灯*/
  public static void disableFlashlight() {
    setFlashlight(false);
  }
  /**通过系统隐藏服务类获取闪光灯*/
  private static Object getHardwareService() {
    Class<?> serviceManagerClass = maybeForName("android.os.ServiceManager");
    if (serviceManagerClass == null) {
      return null;
    }
    Method getServiceMethod = maybeGetMethod(serviceManagerClass, "getService", String.class);
    if (getServiceMethod == null) {
      return null;
    }
    Object hardwareService = invoke(getServiceMethod, null, "hardware");
    if (hardwareService == null) {
      return null;
    }
    Class<?> iHardwareServiceStubClass = maybeForName("android.os.IHardwareService$Stub");
    if (iHardwareServiceStubClass == null) {
      return null;
    }
    Method asInterfaceMethod = maybeGetMethod(iHardwareServiceStubClass, "asInterface", IBinder.class);
    if (asInterfaceMethod == null) {
      return null;
    }
    return invoke(asInterfaceMethod, null, hardwareService);
 }
  /**设置闪光灯可用*/
  private static Method getSetFlashEnabledMethod(Object iHardwareService) {
    if (iHardwareService == null) {
      return null;
    }
    Class<?> proxyClass = iHardwareService.getClass();
    return maybeGetMethod(proxyClass, "setFlashlightEnabled", boolean.class);
  }
  /**通过包名获取系统隐藏类*/
  private static Class<?> maybeForName(String name) {
    try {
      return Class.forName(name);
    } catch (Exception cnfe) {
      return null;
    }
  }
  /**通过方法名获取系统隐藏类*/
  private static Method maybeGetMethod(Class<?> clazz, String name, Class<?>... argClasses) {
    try {
      return clazz.getMethod(name, argClasses);
    } catch (Exception e) {
      return null;
    }
  }
  /**方法调用*/
  private static Object invoke(Method method, Object instance, Object... args) {
    try {
      return method.invoke(instance, args);
    } catch (Exception e) {
      return null;
    }
  }
  /**初始化闪光灯*/
  private static void setFlashlight(boolean active) {
    if (iHardwareService != null) {
      invoke(setFlashEnabledMethod, iHardwareService, active);
    }
  }

}
