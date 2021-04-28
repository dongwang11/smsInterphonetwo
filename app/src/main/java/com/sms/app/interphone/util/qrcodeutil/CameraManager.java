package com.sms.app.interphone.util.qrcodeutil;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Build.VERSION;
import android.os.Handler;
import android.view.SurfaceHolder;

import java.io.IOException;


/**
 * @author VincenT Li
 *	这个类是相机服务对象，封装预览图像功能，这是用于预览和解码。
 */
public final class CameraManager {

	private static final int MIN_FRAME_WIDTH = 225;
	private static final int MIN_FRAME_HEIGHT = 225;
	private int MAX_FRAME_WIDTH = 650;
	private int MAX_FRAME_HEIGHT = 650;
	private static CameraManager cameraManager;
	private Camera camera;
	private Rect tempRect;//以此来判断是否第一次运行，若需要即时改变预览窗大小则需要这个
	private Rect framingRect;
	private Rect framingRectInPreview;
	private boolean initialized;
	private boolean previewing;
	private final Context context;
	private final CameraConfigurationManager configManager;
	private boolean useOneShotPreviewCallback = true;
	/**预览帧被传递到这里，我们传递给注册的处理程序。请确保清除该处理程序，这样它将只接收一个消息*/
	private final CameraPreviewCallback previewCallback;
	/**自动对焦回调到这里，并派出所要求的处理*/
	private final CameraAutoFocusCallback autoFocusCallback;
	/**随着调用活动的上下文初始化相机管理静态对象*/
	
	public static void init(Context context) {
		if (cameraManager == null) {
			cameraManager = new CameraManager(context);
	}}
	public static CameraManager get() {
		return cameraManager;
	}
	
	private CameraManager(Context context) {
		this.context = context;
		useOneShotPreviewCallback = getAndroidSDKVersion()>3;
		this.configManager = new CameraConfigurationManager(context);
		previewCallback = new CameraPreviewCallback(configManager, useOneShotPreviewCallback);
		autoFocusCallback = new CameraAutoFocusCallback();
	}
	public static int getAndroidSDKVersion() {  
        int version = 0;  
        try {  
            version = Integer.valueOf(VERSION.SDK_INT);
        } catch (NumberFormatException e) {
        }  
        return version;  
    }  
	public void setFrameWidthAndHeight(int width,int height) {
		if(tempRect!=null){
			MAX_FRAME_HEIGHT = height;
			MAX_FRAME_WIDTH = width;
			tempRect = null;
		}
	}

	/**打开摄像头驱动和初始化硬件参数*/
	public void openDriver(SurfaceHolder holder) throws IOException {
		if (camera == null) {
			camera = Camera.open();
			if (camera == null) {
				throw new IOException();
			}
			camera.setPreviewDisplay(holder);
			if (!initialized) {
				initialized = true;
				configManager.initFromCameraParameters(camera);
			}
			configManager.setDesiredCameraParameters(camera);
			CameraFlashlightManager.enableFlashlight();
		}
	}

	/**关闭摄像头驱动程序，如果仍在使用*/
	public void closeDriver() {
		if (camera != null) {
			CameraFlashlightManager.disableFlashlight();
			camera.release();
			camera = null;
		}
	}

	/**要求摄像机硬件开始绘制预览帧到屏幕上*/
	public void startPreview() {
		if (camera != null && !previewing) {
			camera.startPreview();
			previewing = true;
		}
	}

	/**告诉相机停止绘制预览帧*/
	public void stopPreview() {
		if (camera != null && previewing) {
			if (!useOneShotPreviewCallback) {
				camera.setPreviewCallback(null);
			}
			camera.stopPreview();
			previewCallback.setHandler(null, 0);
			autoFocusCallback.setHandler(null, 0);
			previewing = false;
		}
	}
	public void enableFlash() {
		try {
			if (context.getPackageManager().hasSystemFeature(
					PackageManager.FEATURE_CAMERA_FLASH)) {
				Parameters p = camera.getParameters();
				p.setFlashMode(Parameters.FLASH_MODE_TORCH);
				camera.setParameters(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void disableFlash() {
		try {
			if (context.getPackageManager().hasSystemFeature(
					PackageManager.FEATURE_CAMERA_FLASH)) {
				Parameters p = camera.getParameters();
				p.setFlashMode(Parameters.FLASH_MODE_OFF);
				camera.setParameters(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**执行截取一张图片的方法
	 * 数据将作为字节[]在message.obj传值
	 * 宽度和高度编码在message.arg1和message.arg2传值*/
	public void requestPreviewFrame(Handler handler, int message) {
		if (camera != null && previewing) {
			previewCallback.setHandler(handler, message);
			if (useOneShotPreviewCallback) {
				camera.setOneShotPreviewCallback(previewCallback);
			} else {
				camera.setPreviewCallback(previewCallback);
	}}}

	/**要求照相机的硬件进行自动对焦*/
	public void requestAutoFocus(Handler handler, int message) {
		if (camera != null && previewing) {
			autoFocusCallback.setHandler(handler, message);
			camera.autoFocus(autoFocusCallback);
	}}

	/**计算框架矩形的界面应显示用户位置的条码。这个目标有助于对齐，以及迫使用户保持足够远，以确保图像将集中的设备。*/
	public Rect getFramingRect() {
		Point screenResolution = configManager.getScreenResolution();
		if (tempRect == null) {
			if (camera == null) {
				return null;
			}
			int width = screenResolution.x * 3 / 4;
			if (width < MIN_FRAME_WIDTH) {
				width = MIN_FRAME_WIDTH;
			} else if (width > MAX_FRAME_WIDTH) {
				width = MAX_FRAME_WIDTH;
			}
			int height = screenResolution.y * 3 / 4;
			if (height < MIN_FRAME_HEIGHT) {
				height = MIN_FRAME_HEIGHT;
			} else if (height > MAX_FRAME_HEIGHT) {
				height = MAX_FRAME_HEIGHT;
			}
			int leftOffset = (screenResolution.x - width) / 2;
			int topOffset = (screenResolution.y - height) / 2;
			framingRect = new Rect(leftOffset, topOffset-100, leftOffset + width,
					topOffset + height-100);//上移100
		}
		tempRect = framingRect;
		return framingRect;
	}

	/**
	 * 
	 */
	public Rect getFramingRectInPreview() {
		if (framingRectInPreview == null) {
			Rect rect = new Rect(getFramingRect());
			Point cameraResolution = configManager.getCameraResolution();
			Point screenResolution = configManager.getScreenResolution();
			rect.left = rect.left * cameraResolution.y / screenResolution.x;
			rect.right = rect.right * cameraResolution.y / screenResolution.x;
			rect.top = rect.top * cameraResolution.x / screenResolution.y;
			rect.bottom = rect.bottom * cameraResolution.x / screenResolution.y;
			framingRectInPreview = rect;
		}
		return framingRectInPreview;
	}

	/**
	 * 
	 * 将结果点从静止坐标转换到屏幕坐标。
	 * @param points
	 *            The points returned by the Reader subclass through
	 *            Result.getResultPoints().
	 * @return An array of Points scaled to the size of the framing rect and
	 *         offset appropriately so they can be drawn in screen coordinates.
	 */
	/*
	 * public Point[] convertResultPoints(ResultPoint[] points) { Rect frame =
	 * getFramingRectInPreview(); int count = points.length; Point[] output =
	 * new Point[count]; for (int x = 0; x < count; x++) { output[x] = new
	 * Point(); output[x].x = frame.list_item_chat_left + (int) (points[x].getX() + 0.5f);
	 * output[x].y = frame.top + (int) (points[x].getY() + 0.5f); } return
	 * output; }
	 */

	/**
	 * 一个建基于预览缓存格式适当的luminancesource对象的工厂方法，所描述的相机参数
	 * @param data V preview frame.
	 * @param width The width of the image.
	 * @param height The height of the image.
	 * @return V YUVLuminanceSource instance.
	 */
	public YUVLuminanceSource getLuminanceSource(byte[] data, int width, int height) {
		Rect rect = getFramingRectInPreview();
		int previewFormat = configManager.getPreviewFormat();
		String previewFormatString = configManager.getPreviewFormatString();
		switch (previewFormat) {
		case ImageFormat.NV21:
		case ImageFormat.NV16:
			return new YUVLuminanceSource(data, width, height, rect.left, rect.top, rect.width(), rect.height());
		default:
			if ("yuv420p".equals(previewFormatString)) {
				return new YUVLuminanceSource(data, width, height, rect.left, rect.top, rect.width(), rect.height());
			}
		}
		throw new IllegalArgumentException("Unsupported picture format: "
				+ previewFormat + '/' + previewFormatString);
	}

	public Context getContext() {
		return context;
	}

}
