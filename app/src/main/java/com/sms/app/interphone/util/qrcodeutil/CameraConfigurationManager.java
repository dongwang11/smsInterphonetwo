package com.sms.app.interphone.util.qrcodeutil;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.util.DisplayMetrics;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author VincenT Li 照相机基本设置，方向大小
 */
public final class CameraConfigurationManager {

	private static final int DESIRED_ZOOM = 20;// 所需的变焦 20则为2倍
	private final Context context;
	private Point screenResolution;// 屏幕分辨率
	private Point cameraResolution;// 相机分辨率
	private String previewFormatString;
	private int previewFormat;

	public CameraConfigurationManager(Context context) {
		this.context = context;
	}

	/** 初始化相机中获取的参数 */
	public void initFromCameraParameters(Camera camera) {
		Parameters parameters = camera.getParameters();
		previewFormat = parameters.getPreviewFormat();
		previewFormatString = parameters.get("preview-format");
//		 WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
//		 Display display = manager.getDefaultDisplay();
//		 screenResolution = new Point(display.getWidth(),display.getHeight());
		DisplayMetrics display = context.getResources().getDisplayMetrics();
		screenResolution = new Point(display.widthPixels, display.heightPixels);
		// 横竖屏问题
		Point screenResolutionForCamera = new Point();
			screenResolutionForCamera.x = screenResolution.x;
			screenResolutionForCamera.y = screenResolution.y;
		if (screenResolution.x < screenResolution.y) {
			screenResolutionForCamera.x = screenResolution.y;
			screenResolutionForCamera.y = screenResolution.x;
		}
		cameraResolution = getCameraResolution(parameters,screenResolutionForCamera);
//		cameraResolution = getCameraResolution(parameters,screenResolution);
	}

	/** 设置摄像头拍摄的图像，用于预览和解码 */
	public void setDesiredCameraParameters(Camera camera) {
		Parameters parameters = camera.getParameters();
		parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);
		setFlash(parameters);
		setZoom(parameters);
		setDisplayOrientation(camera, 90);
		camera.setParameters(parameters);
	}
	/**相机分辨率*/
	public Point getCameraResolution() {
		return cameraResolution;
	}
	/**屏幕分辨率*/
	public Point getScreenResolution() {
		return screenResolution;
	}
	/**获得预览格式*/
	public int getPreviewFormat() {
		return previewFormat;
	}
	/**获得预览格式字符串*/
	public String getPreviewFormatString() {
		return previewFormatString;
	}

	/**获得相机分辨率*/
	private static Point getCameraResolution(Parameters parameters, Point screenResolution) {
		String previewSizeValueString = parameters.get("preview-size-values");
		Point cameraResolution = null;
		if (previewSizeValueString == null) {
			previewSizeValueString = parameters.get("preview-size-value");
		}
		if (previewSizeValueString != null) {
			cameraResolution = findBestPreviewSizeValue(previewSizeValueString, screenResolution);
		}
		if (cameraResolution == null) {
			cameraResolution = new Point((screenResolution.x >> 3) << 3, (screenResolution.y >> 3) << 3);
		}
		return cameraResolution;
	}
	/**最佳的预览大小值*/
	private static Point findBestPreviewSizeValue(CharSequence previewSizeValueString, Point screenResolution) {
		int bestX = 0;
		int bestY = 0;
		int newX;
		int newY;
		int diff = Integer.MAX_VALUE;
		for (String previewSize : Pattern.compile(",").split(previewSizeValueString)) {
			previewSize = previewSize.trim();
			int dimPosition = previewSize.indexOf('x');
			if (dimPosition < 0) {
				continue;
			}
			try {
				newX = Integer.parseInt(previewSize.substring(0, dimPosition));
				newY = Integer.parseInt(previewSize.substring(dimPosition + 1));
			} catch (Exception e) {

				continue;
			}
			int newDiff = Math.abs(newX - screenResolution.x) + Math.abs(newY - screenResolution.y);
			if (newDiff == 0) {
				bestX = newX;
				bestY = newY;
				break;
			} else if (newDiff < diff) {
				bestX = newX;
				bestY = newY;
				diff = newDiff;
		}}
		if (bestX > 0 && bestY > 0) {
			return new Point(bestX, bestY);
		}
		return null;
	}
	/**最佳MOT缩放值*/
	private static int findBestMotZoomValue(CharSequence stringValues, int tenDesiredZoom) {
		int tenBestValue = 0;
		for (String stringValue : Pattern.compile(",").split(stringValues)) {
			stringValue = stringValue.trim();
			double value;
			try {
				value = Double.parseDouble(stringValue);
			} catch (NumberFormatException nfe) {
				return tenDesiredZoom;
			}
			int tenValue = (int) (10.0 * value);
			if (Math.abs(tenDesiredZoom - value) < Math.abs(tenDesiredZoom - tenBestValue)) {
				tenBestValue = tenValue;
		}}
		return tenBestValue;
	}
	
	/**设置闪光灯参数*/
	private void setFlash(Parameters parameters) {
		if (Build.MODEL.contains("Behold II") && CameraManager.getAndroidSDKVersion() == 3) { // 3
			parameters.set("flash-value", 1);
		} else {
			parameters.set("flash-value", 2);
		}
		parameters.set("flash-mode", "off");
	}
	
	/**设置缩放参数*/
	private void setZoom(Parameters parameters) {
		String zoomSupportedString = parameters.get("zoom-supported");
		if (zoomSupportedString != null && !Boolean.parseBoolean(zoomSupportedString)) {
			return;
		}
		int tenDesiredZoom = DESIRED_ZOOM;
		String maxZoomString = parameters.get("max-zoom");
		if (maxZoomString != null) {
			try {
				int tenMaxZoom = (int) (10.0 * Double.parseDouble(maxZoomString));
				if (tenDesiredZoom > tenMaxZoom) {
					tenDesiredZoom = tenMaxZoom;
				}
			} catch (NumberFormatException nfe) {
		}}
		String takingPictureZoomMaxString = parameters.get("taking-picture-zoom-max");
		if (takingPictureZoomMaxString != null) {
			try {
				int tenMaxZoom = Integer.parseInt(takingPictureZoomMaxString);
				if (tenDesiredZoom > tenMaxZoom) {
					tenDesiredZoom = tenMaxZoom;
			}} catch (Exception e) {
		}}
		String motZoomValuesString = parameters.get("mot-zoom-values");
		if (motZoomValuesString != null) {
			tenDesiredZoom = findBestMotZoomValue(motZoomValuesString, tenDesiredZoom);
		}
		String motZoomStepString = parameters.get("mot-zoom-step");
		if (motZoomStepString != null) {
			try {
				double motZoomStep = Double.parseDouble(motZoomStepString.trim());
				int tenZoomStep = (int) (10.0 * motZoomStep);
				if (tenZoomStep > 1) {
					tenDesiredZoom -= tenDesiredZoom % tenZoomStep;
			}} catch (Exception e) {
		}}
		if (maxZoomString != null || motZoomValuesString != null) {
			parameters.set("zoom", String.valueOf(tenDesiredZoom / 10.0));
		}
		if (takingPictureZoomMaxString != null) {
			parameters.set("taking-picture-zoom", tenDesiredZoom);
	}}
	
	/**设置显示方向*/
	private void setDisplayOrientation(Camera camera, int angle) {
		try {
			Method downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[] { int.class });
			if (downPolymorphic != null)
				downPolymorphic.invoke(camera, new Object[] { angle });
		} catch (Exception e) {}
	}

}
