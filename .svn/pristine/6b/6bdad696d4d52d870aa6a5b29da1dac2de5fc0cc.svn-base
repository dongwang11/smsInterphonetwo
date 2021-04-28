package com.sms.app.interphone.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.sms.app.interphone.R;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;
import com.sms.app.interphone.util.qrcodeutil.CameraManager;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author Administrator
 */
@SuppressLint("DrawAllocation")
public final class CustomScanView extends View {
	//刷新界面的时间
	private static final long ANIMATION_DELAY = 5L;
	private static final int OPAQUE = 0xFF;
	//四个绿色边角长度
	private int ScreenRate = 0;
	//四个绿色边角宽度
	private static final int CORNER_WIDTH = 5;
	//扫描框中的中间线的宽度
//	private static final int MIDDLE_LINE_WIDTH = 0;
	//扫描框中的中间线的与扫描框左右的间隙
	private static final int MIDDLE_LINE_PADDING = 15;
	//中间那条线每次刷新移动的距离
	private static final int SPEEN_DISTANCE = 5;
	//手机的屏幕密度
	private static float density;
	//初始化文本
	private String text = getResources().getString(R.string.sms_chat_wi_hite_content);
	//字体大小
	private static final int TEXT_SIZE = 16;
	//字体距离扫描框下面的距离
	private static final int TEXT_PADDING_TOP = 30;
	//中间滑动线的最顶端位置
	private int slideTop;
	//中间滑动线的最底端位置
	private int slideBottom;
	private Paint paint;
	private Bitmap resultBitmap;
	private final int maskColor;
	private final int resultColor;
	private final int resultPointColor;
	private Collection<ResultPoint> possibleResultPoints;
	private Collection<ResultPoint> lastPossibleResultPoints;

	public static boolean isFirst = false;
	
	public CustomScanView(Context context, AttributeSet attrs) {
		super(context, attrs);
		density = context.getResources().getDisplayMetrics().density;
		//将像素转换成dp
		ScreenRate = (int)(20 * density);
		paint = new Paint();
		Resources resources = context.getResources();
		maskColor = resources.getColor(R.color.maskColor);
		resultColor = resources.getColor(R.color.resultColor);
		resultPointColor = resources.getColor(R.color.resultPointColor);
		possibleResultPoints = new HashSet<ResultPoint>(5);
	}

	@Override
	public void onDraw(Canvas canvas) {
		try {
			Rect frame = CameraManager.get().getFramingRect();
			if (frame == null) {
				return;
			}
			if(!isFirst){
				isFirst = true;
				slideTop = frame.top-5;
				slideBottom = frame.bottom;
			}
			int width = canvas.getWidth();
			int height = canvas.getHeight();
			//画出扫描框外面的阴影部分，共四个部分，扫描框的上面到屏幕上面，扫描框的下面到屏幕下面
			//扫描框的左边面到屏幕左边，扫描框的右边到屏幕右边
			paint.setColor(resultBitmap != null ? resultColor : maskColor);
			canvas.drawRect(0, 0, width, frame.top, paint);
			canvas.drawRect(0, frame.top, frame.left, frame.bottom, paint);
			canvas.drawRect(frame.right, frame.top, width, frame.bottom, paint);
			canvas.drawRect(0, frame.bottom, width, height, paint);


			if (resultBitmap != null) {
				paint.setAlpha(OPAQUE);
				canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
			} else {
				//画扫描框边上的角，总共8个部分
				paint.setColor(Color.parseColor("#0690FF"));
				canvas.drawRect(frame.left, frame.top, frame.left + ScreenRate,
						frame.top + CORNER_WIDTH, paint);
				canvas.drawRect(frame.left, frame.top, frame.left + CORNER_WIDTH, frame.top
						+ ScreenRate, paint);
				canvas.drawRect(frame.right - ScreenRate, frame.top, frame.right,
						frame.top + CORNER_WIDTH, paint);
				canvas.drawRect(frame.right - CORNER_WIDTH, frame.top, frame.right, frame.top
						+ ScreenRate, paint);
				canvas.drawRect(frame.left, frame.bottom - CORNER_WIDTH, frame.left
						+ ScreenRate, frame.bottom, paint);
				canvas.drawRect(frame.left, frame.bottom - ScreenRate,
						frame.left + CORNER_WIDTH, frame.bottom, paint);
				canvas.drawRect(frame.right - ScreenRate, frame.bottom - CORNER_WIDTH,
						frame.right, frame.bottom, paint);
				canvas.drawRect(frame.right - CORNER_WIDTH, frame.bottom - ScreenRate,
						frame.right, frame.bottom, paint);
				//绘制中间的线,每次刷新界面，中间的线往下移动SPEEN_DISTANCE
				slideTop += SPEEN_DISTANCE;
				if(slideTop >= slideBottom){
					slideTop=frame.top;
				}
				Rect lineRect = new Rect();
				lineRect.left = frame.left-MIDDLE_LINE_PADDING;
				lineRect.right = frame.right+MIDDLE_LINE_PADDING;
				lineRect.top = slideTop;
				lineRect.bottom = slideTop+MIDDLE_LINE_PADDING;
				canvas.drawBitmap(((BitmapDrawable)(getResources().getDrawable(R.mipmap.icon_capture_line))).getBitmap(), null, lineRect, paint);
				//画扫描框下面的字
				paint.setColor(Color.WHITE);
				paint.setTextSize(TEXT_SIZE * density);
				paint.setAlpha(0xC8);
				paint.setTypeface(Typeface.create("System", Typeface.BOLD));
				float textWidth = paint.measureText(text);
				canvas.drawText(text, (width - textWidth)/2, (float) (frame.bottom + (float)TEXT_PADDING_TOP *density), paint);
				Collection<ResultPoint> currentPossible = possibleResultPoints;
				Collection<ResultPoint> currentLast = lastPossibleResultPoints;
				if (currentPossible.isEmpty()) {
					lastPossibleResultPoints = null;
				} else {
					possibleResultPoints = new HashSet<ResultPoint>(5);
					lastPossibleResultPoints = currentPossible;
					paint.setAlpha(OPAQUE);
					paint.setColor(resultPointColor);
					for (ResultPoint point : currentPossible) {
						canvas.drawCircle(frame.left + point.getX(), frame.top
								+ point.getY(), 6.0f, paint);
					}
				}
				if (currentLast != null) {
					paint.setAlpha(OPAQUE / 2);
					paint.setColor(resultPointColor);
					for (ResultPoint point : currentLast) {
						canvas.drawCircle(frame.left + point.getX(), frame.top
								+ point.getY(), 3.0f, paint);
					}
				}

				//只刷新扫描框的内容，其他地方不刷新
				postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
						frame.right, frame.bottom);

			}
		} catch (Resources.NotFoundException e) {
			ExceptionUtil.handleException(e);
		}
	}

	public void drawScanView() {
		resultBitmap = null;
		invalidate();
	}
	public void setText(String text){
		this.text = text;
	}
	/**
	 * 位图的结果点突出显示，而不是活动的扫描显示
	 * @param barcode An image of the decoded barcode.
	 */
	public void drawResultBitmap(Bitmap barcode) {
		resultBitmap = barcode;
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		possibleResultPoints.add(point);
	}

}
