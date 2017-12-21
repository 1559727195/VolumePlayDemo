package com.example.volumedemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * 自定义音量显示控件
 *
 * @author zhaokaiqiang
 *
 * @time 2014年6月25日 上午11:42:10
 */
public class VolumeView extends View {

    private static final String TAG = "VolumeView";
    // 增加音量图片
    private Bitmap addBitmap;
    // 减少音量图片
    private Bitmap reduceBitmap;
    // 小喇叭图片
    private Bitmap volume;
    private Paint paint = new Paint();
//    // 控件高度
    private int height = 150;

    // 控件宽度
    private int width = 930;
    // 最大音量
    private int MAX = 20;
    // 两个音量矩形最左侧之间的间隔
    private int rectMargen = 25;
    // 音量矩形高
    private int rectH = 70;
    // 音量矩形宽
    private int recW = 15;
    // 当前选中的音量
    private int current = 0;
    // 最左侧音量矩形距离控件最左侧距离
    private int leftMargen = 0;
    private Context context;

    public VolumeView(Context context) {
        super(context);
//        init(context);
    }

    public VolumeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public VolumeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        addBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.add);//+号
        volume = BitmapFactory.decodeResource(getResources(), R.drawable.volice);//声音图标
        reduceBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.delete);
//        leftMargen = volume.getWidth() + reduceBitmap.getWidth();//音量图标和减音量图标的宽度
//        leftMargen = volume.getWidth() + reduceBitmap.getWidth() + volume.getWidth() / 4;//音量图标和减音量图标的宽度


        height = DisplayUtil.dip2px(context,75);
//        width = DisplayUtil.dip2px(context,460);
        width = 4 * getScreenWidth() / 5 + getScreenWidth() / 10;//控件高度
        leftMargen = getScreenWidth() / 5 / 4;//控件左边距
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VolumeView);

        rectMargen = (int) a.getDimension(R.styleable.VolumeView_rectMargen, DisplayUtil.dip2px(context,(float)12.5));
        recW = (int) a.getDimension(R.styleable.VolumeView_recW, DisplayUtil.dip2px(context,(float)7.5));
        rectH = (int) a.getDimension(R.styleable.VolumeView_rectH, DisplayUtil.dip2px(context,(float)35));//
    }
    /**
     * 得到屏幕宽度
     * @return
     */
    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制背景颜色
        paint.setColor(getResources().getColor(R.color.white));
//        canvas.drawRect(0, 0, width, height, paint);//白色背景框

        // 绘制没有被选中的白色音量矩形
        paint.setColor(getResources().getColor(R.color.green));
        for (int i = current; i < MAX; i++) {
            canvas.drawRect(leftMargen + (i + 2) * rectMargen, (height - rectH) / 2,
                            leftMargen + (i + 2) * rectMargen + recW, (height - rectH) / 2 + rectH,
                    paint);
            //left,top,right,bottom
        }

        // 绘制被选中的橘黄色音量矩形
        paint.setColor(getResources().getColor(R.color.orange));
        for (int i = 0; i < current; i++) {
            canvas.drawRect(leftMargen + (i + 2) * rectMargen, (height - rectH) / 2, leftMargen + (i + 2) * rectMargen + recW, (height - rectH) / 2 + rectH,
                    paint);
        }

//        // 绘制音量图片
////        canvas.drawBitmap(volume, volume.getWidth() / 2, (height - volume.getHeight()) / 2, paint);
//        canvas.drawBitmap(volume,volume.getWidth() / 2,(height - volume.getHeight()) / 2, paint);//绘制音量图片

//        // 绘制音量减少图片
//        canvas.drawBitmap(reduceBitmap, reduceBitmap.getWidth() + volume.getWidth(), (height - reduceBitmap.getHeight()) / 2, paint);
        //绘制音量减少图片
        canvas.drawBitmap(reduceBitmap,reduceBitmap.getWidth(), (height - reduceBitmap.getHeight()) / 2,paint);

        // 绘制音量增加图片
        canvas.drawBitmap(addBitmap, leftMargen + (MAX + 2) * rectMargen, (height - addBitmap.getHeight()) / 2, paint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < leftMargen + rectMargen) {//点击减区域时候响应减的事件
                    if (current > 0) {
                        current -= 1;
                        if (onChangeListener != null) {
                            onChangeListener.onChange(current);
                        }
                    }
                    Log.d(TAG, "current:" + current);
                } else if (event.getX() > leftMargen + (MAX + 1) * rectMargen + recW){//点击加区域时候响应加事件
                    if (current < 20) {
                        current += 1;
                        if (onChangeListener != null) {
                            onChangeListener.onChange(current);
                        }
                    }
                    Log.d(TAG, "current:" + current);
                }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_MOVE:
                // 当触摸位置在音量矩形之内时，获取当前选中的音量矩形数量
                if ((event.getX() > leftMargen + rectMargen && event.getX() < leftMargen + (MAX + 1) * rectMargen + recW)
                        && (event.getY() > (height - rectH) / 2 && event.getY() < (height - rectH) / 2 + rectH)) {
                    current = (int) ((event.getX() - (leftMargen)) / (rectMargen)) - 1;
                    if (onChangeListener != null) {
                        onChangeListener.onChange(current);
                    }
                    Log.d(TAG, "current:" + current);
                }
                break;
        }
        // 通知界面刷新
        invalidate();
        // 拦截触摸事件
        return true;
    }

    // 高度父布局要占用的位置大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public interface OnChangeListener {
        void onChange(int count);
    }

    private OnChangeListener onChangeListener;

    public void setOnChangeListener(OnChangeListener onChangeListener) {
//        this.onChangeListener = onChangeListener;
        this.onChangeListener = onChangeListener;
    }

}