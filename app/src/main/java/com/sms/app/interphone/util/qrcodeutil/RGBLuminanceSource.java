/*
 * Copyright 2009 ZXing authors
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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.zxing.LuminanceSource;

import java.io.FileNotFoundException;

/**
 * @author VincenT Li
 *	提取二维码色彩
 */
public final class RGBLuminanceSource extends LuminanceSource {

	private final byte[] luminances;
	
	/**初始化传入路径，读取bitmap*/
	public RGBLuminanceSource(String path) throws FileNotFoundException {
		this(loadBitmap(path));
	}
	
	/**初始化传入bitmap*/
	public RGBLuminanceSource(Bitmap bitmap) {
		super(bitmap.getWidth(), bitmap.getHeight());
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		//要取得该图片的像素数组内容
		int[] pixels = new int[width * height];
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		luminances = new byte[width * height];
		//将int数组转换为byte数组，运行for循环得到图片每点像素颜色
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				int pixel = pixels[offset + x];
				int r = (pixel >> 16) & 0xff;
				int g = (pixel >> 8) & 0xff;
				int b = pixel & 0xff;
				//当某一点三种颜色值相同时，相应字节对应空间赋值为其值 
				if (r == g && g == b) {
					luminances[offset + x] = (byte) r;
				} else {
					luminances[offset + x] = (byte) ((r + g + g + b) >> 2);
		}}}
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
		System.arraycopy(luminances, y * width, row, 0, width);
		return row;
	}
	/**得到矩阵，图片的像素数组*/
	@Override
	public byte[] getMatrix() {
		return luminances;
	}

	private static Bitmap loadBitmap(String path) throws FileNotFoundException {
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		if (bitmap == null) {
			throw new FileNotFoundException("Couldn't open " + path);
		}
		return bitmap;
	}

}
