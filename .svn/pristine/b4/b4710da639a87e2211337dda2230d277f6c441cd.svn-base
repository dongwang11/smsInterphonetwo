package com.sms.app.interphone.util.qrcodeutil;

import android.graphics.Bitmap;

import com.google.zxing.LuminanceSource;

/**
 * @author VincenT Li
 *	提取灰阶值、色度
 */
public final class YUVLuminanceSource extends LuminanceSource {
	
	private final byte[] yuvData;
	private final int dataWidth;
	private final int dataHeight;
	private final int left;
	private final int top;
	
	/**初始化传入数值*/
	public YUVLuminanceSource(byte[] yuvData, int dataWidth, int dataHeight, int left, int top, int width, int height) {
		super(width, height);
		if (left + width > dataWidth || top + height > dataHeight) {
			throw new IllegalArgumentException("Crop rectangle does not fit within image data.");
		}
		this.yuvData = yuvData;
		this.dataWidth = dataWidth;
		this.dataHeight = dataHeight;
		this.left = left;
		this.top = top;
	}
	/**得到指定行的像素数据*/
	@Override
	public byte[] getRow(int y, byte[] row) {
		if (y < 0 || y >= getHeight()) {
			throw new IllegalArgumentException("Requested row is outside the image: " + y);
		}
		int width = getWidth();
		if (row == null || row.length < width) {
			row = new byte[width];
		}
		int offset = (y + top) * dataWidth + left;
		System.arraycopy(yuvData, offset, row, 0, width);
		return row;
	}
	
	/**得到矩阵，图片的像素数组*/
	@Override
	public byte[] getMatrix() {
		int width = getWidth();
		int height = getHeight();
		if (width == dataWidth && height == dataHeight) {
			return yuvData;
		}
		int area = width * height;
		byte[] matrix = new byte[area];
		int inputOffset = top * dataWidth + left;
		if (width == dataWidth) {
			System.arraycopy(yuvData, inputOffset, matrix, 0, area);//数组复制
			return matrix;
		}
		for (int y = 0; y < height; y++) {
			int outputOffset = y * width;
			System.arraycopy(yuvData, inputOffset, matrix, outputOffset, width);
			inputOffset += dataWidth;
		}
		return matrix;
	}
	
	//@Override 未知作用  删除？
	//public boolean isCropSupported() {
	//return true;
	//}
	//public int getDataWidth() {
	//return dataWidth;
	//}
	//public int getDataHeight() {
	//return dataHeight;
	//}
	/**渲染出灰度位图(未使用)*/
	public Bitmap renderCroppedGreyscaleBitmap() {
		int width = getWidth();
		int height = getHeight();
		int[] pixels = new int[width * height];
		int inputOffset = top * dataWidth + left;
		for (int y = 0; y < height; y++) {
			int outputOffset = y * width;
			for (int x = 0; x < width; x++) {
				int grey = yuvData[inputOffset + x] & 0xff;
				pixels[outputOffset + x] = 0xFF000000 | (grey * 0x00010101);
			}
			inputOffset += dataWidth;
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
}
