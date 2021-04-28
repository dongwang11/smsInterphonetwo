package com.sms.app.interphone.util.qrcodeutil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.view.Gravity;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;


public final class EncodingUtil {
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;
	
	public static Bitmap createQRCode(String str, int widthAndHeight){
		try {
			if (str == null || "".equals(str) || str.length() < 1) {
				return null;
			}
			Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");

			BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight,hints);
			matrix = updateBit(matrix, 3);//自定义白边边框宽度
			int width = matrix.getWidth();
			int height = matrix.getHeight();
			int[] pixels = new int[width * height];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (matrix.get(x, y)) {
						pixels[y * width + x] = BLACK;
			}}}
			Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static BitMatrix updateBit(BitMatrix matrix, int margin){
		int tempM = margin*2;
		int[] rec = matrix.getEnclosingRectangle();   //获取二维码图案的属性
		int resWidth = rec[2] + tempM;
		int resHeight = rec[3] + tempM;
		BitMatrix resMatrix = new BitMatrix(resWidth, resHeight); // 按照自定义边框生成新的BitMatrix
		resMatrix.clear();
		for(int i= margin; i < resWidth- margin; i++){   //循环，将二维码图案绘制到新的bitMatrix中
			for(int j=margin; j < resHeight-margin; j++){
				if(matrix.get(i-margin + rec[0], j-margin + rec[1])){
					resMatrix.set(i,j);
		}}}
		return resMatrix;
	}
//	/**
//	* 图片放大缩小
//	*/
//	private static BufferedImage  zoomInImage(BufferedImage  originalImage, int width, int height){
//		BufferedImage newImage = new BufferedImage(width,height,originalImage.getType());
//		Graphics g = newImage.getGraphics();
//		g.drawImage(originalImage, 0,0,width,height,null);
//		g.dispose();
//		return newImage;
//	}
	/**
	 * 创建一维码
	 * @param text 需生产内容
	 * @param desiredWidth 条形码宽度
	 * @param desiredHeight 条形码高度
	 * @param displayCode 是否在下方显示数字
	 * @return
	 */
	public static Bitmap createBarCode(Context context, String text, int desiredWidth, int desiredHeight, boolean displayCode) {
		try {
			Bitmap resultBitmap = null;
			int marginWidth = 5;
			BarcodeFormat format = BarcodeFormat.CODE_128;//条形码类型
			if (displayCode) {//是否在下方显示数字
				Bitmap barcodeBitmap = encodeBarCode(text, format,desiredWidth, desiredHeight);
				Bitmap codeBitmap = encodeNumCode(text, desiredWidth + 2 * marginWidth, desiredHeight, context);
				resultBitmap = encodeMixture(barcodeBitmap, codeBitmap, new PointF(0, desiredHeight));
			} else {
				resultBitmap = encodeBarCode(text, format,desiredWidth, desiredHeight);
			}
			return resultBitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 生成条形码Bitmap
	 * @param text 需生产内容
	 * @param format 模板
	 * @param desiredWidth 条形码宽度
	 * @param desiredHeight 条形码高度
	 * @return 返回条形码bitmap
	 */
	private static Bitmap encodeBarCode(String text, BarcodeFormat format, int desiredWidth, int desiredHeight) {
		MultiFormatWriter writer = new MultiFormatWriter();
		try {
			BitMatrix result = writer.encode(text, format, desiredWidth,desiredHeight, null);
			int width = result.getWidth();
			int height = result.getHeight();
			int[] pixels = new int[width * height];
			for (int y = 0; y < height; y++) {
				int offset = y * width;
				for (int x = 0; x < width; x++) {
					pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
			}}
			Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 数字码Bitmap
	 * @param text 数字码内容
	 * @param width 数字码宽度
	 * @param height 数字码高度
	 * @return 返回Bitmap类型
	 */
	private static Bitmap encodeNumCode(String text, int width, int height, Context context) {
		TextView tv = new TextView(context);
		tv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		tv.setText(text);
		tv.setWidth(width);
		tv.setHeight(height);
		tv.setGravity(Gravity.CENTER_HORIZONTAL);
		tv.setDrawingCacheEnabled(true);
		tv.setTextColor(Color.BLACK);
		tv.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());
		tv.buildDrawingCache();
		Bitmap bitmapCode = tv.getDrawingCache();
		return bitmapCode;
	}

	/**
	 * 将条形码和数字码合并生产bitmap
	 * @param first 条形码
	 * @param second 数字码
	 * @param fromPoint 位置中心偏移
	 * @return 返回合并后bitmap
	 */
	private static Bitmap encodeMixture(Bitmap first, Bitmap second, PointF fromPoint) {
		if (first == null || second == null || fromPoint == null) {
			return null;
		}
		int marginW = 5;
		Bitmap newBitmap = Bitmap.createBitmap(
				first.getWidth() + second.getWidth() + marginW,
				first.getHeight() + second.getHeight(), Config.ARGB_8888);
		Canvas cv = new Canvas(newBitmap);
		cv.drawBitmap(first, marginW, 0, null);
		cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
    //    cv.save(Canvas.ALL_SAVE_FLAG);
      	cv.save();
		cv.restore();
		return newBitmap;
	}
}
