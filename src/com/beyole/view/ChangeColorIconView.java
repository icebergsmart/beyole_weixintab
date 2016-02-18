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
	// ��ɫ
	private int mColor = 0xFF45C01A;
	// ͸���� 0.0f-1.0f
	private float mAlpha = 0f;
	// ͼ��
	private Bitmap mIconBitmap;
	// ���ƻ���icon�ķ�Χ
	private Rect mIconRect;
	// icon�ײ�����
	private String mText = "΢��";
	private int mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics());
	// ����icon�ײ����ֻ���
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
		// �õ�text�Ļ��Ʒ�Χ
		mTextPaint.getTextBounds(mText, 0, mText.length(), textBound);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// �õ�icon�Ŀ�
		int bitmapWidth = Math.min(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - textBound.height());
		int left = getMeasuredWidth() / 2 - bitmapWidth / 2;
		int top = (getMeasuredHeight() - textBound.height()) / 2 - bitmapWidth / 2;
		// ����icon�Ļ��Ʒ�Χ
		mIconRect = new Rect(left, top, left + bitmapWidth, top + bitmapWidth);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// ����alpha��Ĭ��Ϊ0
		int alpha = (int) Math.ceil(255 * mAlpha);
		// ����ԭͼ
		canvas.drawBitmap(mIconBitmap, null, mIconRect, null);
		setupTargetBitmap(alpha);
		drawSourceText(canvas, alpha);
		drawTargetText(canvas, alpha);
		canvas.drawBitmap(mBitmap, 0, 0, null);
	}

	/**
	 * ���ƴ�ɫ��
	 * 
	 * @param alpha
	 */
	private void setupTargetBitmap(int alpha) {
		// ����
		mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Config.ARGB_8888);
		canvas = new Canvas(mBitmap);
		// ���ƴ�ɫ��
		mPaint = new Paint();
		mPaint.setColor(mColor);
		mPaint.setAntiAlias(true);
		// ������
		mPaint.setDither(true);
		mPaint.setAlpha(alpha);
		canvas.drawRect(mIconRect, mPaint);
		// ����mode������ڴ��ϵ�bitmap��paint
		mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		mPaint.setAlpha(255);
		// ����ͼ�����ڴ��bitmap��
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
	 * �ػ�view
	 */
	public void invalidateView() {
		// �����ǰ�߳�Ϊ���̣߳������invalidate�������ǰ�߳�Ϊ���̣߳������postInvalidate
		if (Looper.getMainLooper() == Looper.myLooper()) {
			invalidate();
		} else {
			postInvalidate();
		}
	}

}
