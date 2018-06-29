package jjj.demo.playRobotBendizhao;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.view.View;

public class EatBeanMan extends View {
	Paint paintC;
	Paint paintEye;

	public EatBeanMan(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
		paintC = new Paint();
		paintC.setColor(Color.YELLOW);
		paintC.setAntiAlias(true);
		paintEye = new Paint();
		paintEye.setColor(Color.BLACK);
		paintEye.setAntiAlias(true);
	}

	boolean isDrawing = false;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO 自动生成的方法存根
		int wMeasure = MeasureSpec.makeMeasureSpec(300, MeasureSpec.EXACTLY);
		int hMeasure = wMeasure;
		if (!isDrawing) {
			mHandler.sendEmptyMessage(101);
			isDrawing = true;
			rectF = new RectF(0, 0, 300, 300);
		}
		super.onMeasure(wMeasure, hMeasure);
	}

	/**
	 * 吃豆人开始角度
	 */
	int startAngle = 45;
	/**
	 * 吃豆人的要画的度数
	 */
	int sweepAngle = 270;
	/**
	 * 吃豆人每两次显示绘制开始角度的差值，大于0咬，小于0张嘴
	 */
	int csAngle = -10;
	/**
	 * 吃豆人每两次显示绘制度数的差值，大于0咬，小于0张嘴
	 */
	int cAngle = 20;
	Handler mHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO 自动生成的方法存根
			switch (msg.what) {
			case 101:
				invalidate();
				if (sweepAngle >= 360) {
					csAngle = 10;
					cAngle = -20;
				} else if (sweepAngle <= 270) {
					csAngle = -10;
					cAngle = 20;
				}
				startAngle += csAngle;
				sweepAngle += cAngle;
				mHandler.sendEmptyMessageDelayed(101, 40);
				break;

			}
			return false;
		}
	});
	RectF rectF;

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawArc(rectF, startAngle, sweepAngle, true, paintC);
		canvas.drawCircle(rectF.right / 5 * 3, rectF.bottom / 4, 10, paintEye);
	}
}
