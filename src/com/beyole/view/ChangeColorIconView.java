package com.beyole.view;

import com.beyole.weixintab.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class ChangeColorIconView extends View {
	private Bitmap mBitmap;
	private Canvas canvas;
	private Paint mPaint;
	// 颜色
	private int mColor = 0xFF45C01A;
	// 透明度 0.0f-1.0f
	private float mAlpha = 0f;
	// 图标
	private Bitmap mIconBitmap;
	// 限制绘制icon的范围
	private Rect mIconRect;
	// icon底部文字
	private String mText = "微信";
	private int mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics());
	// 绘制icon底部文字画笔
	private Paint mTextPaint;
	private Rect textBound = new Rect();

	public ChangeColorIconView(Context context) {
		this(context, null);
	}

	public ChangeColorIconView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ChangeColorIconView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ChangeColorIconView, defStyle, 0);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.ChangeColorIconView_text:
				mText = a.getString(attr);
				break;
			case R.styleable.ChangeColorIconView_textSize:
				mTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
				break;
			case R.styleable.ChangeColorIconView_icon:
				BitmapDrawable bitmapDrawable = (BitmapDrawable) a.getDrawable(attr);
				mIconBitmap = bitmapDrawable.getBitmap();
				break;
			case R.styleable.ChangeColorIconView_color:
				mColor = a.getColor(attr, 0x45C01A);
				break;
			}
		}
		a.recycle();
		mTextPaint = new Paint();
		mTextPaint.setTextSize(mTextSize);
		mTextPaint.setColor(0xff555555);
		// 得到text的绘制范围
		mTextPaint.getTextBounds(mText, 0, mText.length(), textBound);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 得到icon的宽
		int bitmapWidth = Math.min(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - textBound.height());
		int left = getMeasuredWidth() / 2 - bitmapWidth / 2;
		int top = (getMeasuredHeight() - textBound.height()) / 2 - bitmapWidth / 2;
		// 设置icon的绘制范围
		mIconRect = new Rect(left, top, left + bitmapWidth, top + bitmapWidth);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// 计算alpha，默认为0
		int alpha = (int) Math.ceil(255 * mAlpha);
		// 绘制原图
		canvas.drawBitmap(mIconBitmap, null, mIconRect, null);
		setupTargetBitmap(alpha);
		drawSourceText(canvas, alpha);
		drawTargetText(canvas, alpha);
		canvas.drawBitmap(mBitmap, 0, 0, null);
	}

	/**
	 * 绘制纯色块
	 * 
	 * @param alpha
	 */
	private void setupTargetBitmap(int alpha) {
		// 画布
		mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Config.ARGB_8888);
		canvas = new Canvas(mBitmap);
		// 绘制纯色块
		mPaint = new Paint();
		mPaint.setColor(mColor);
		mPaint.setAntiAlias(true);
		// 防抖动
		mPaint.setDither(true);
		mPaint.setAlpha(alpha);
		canvas.drawRect(mIconRect, mPaint);
		// 设置mode，针对内存上的bitmap的paint
		mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		mPaint.setAlpha(255);
		// 绘制图标在内存的bitmap上
		canvas.drawBitmap(mIconBitmap, null, mIconRect, mPaint);
	}

	private void drawSourceText(Canvas canvas, int alpha) {
		mTextPaint.setTextSize(mTextSize);
		mTextPaint.setColor(0xff333333);
		mTextPaint.setAntiAlias(true);
		mTextPaint.setAlpha(255 - alpha);
		canvas.drawText(mText, mIconRect.left + mIconRect.width() / 2 - textBound.width() / 2, mIconRect.bottom + textBound.height(), mTextPaint);
	}

	private void drawTargetText(Canvas canvas, int alpha) {
		mTextPaint.setColor(mColor);
		mTextPaint.setAlpha(alpha);
		canvas.drawText(mText, mIconRect.left + mIconRect.width() / 2 - textBound.width() / 2, mIconRect.bottom + textBound.height(), mTextPaint);
	}

	public void setIconAlpha(float alpha) {
		this.mAlpha = alpha;
		invalidateView();

	}

	/**
	 * 重绘view
	 */
	public void invalidateView() {
		// 如果当前线程为主线程，则调用invalidate，如果当前线程为子线程，则调用postInvalidate
		if (Looper.getMainLooper() == Looper.myLooper()) {
			invalidate();
		} else {
			postInvalidate();
		}
	}

}
