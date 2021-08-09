package com.mobile.bnkcl.com.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

import com.mobile.bnkcl.R;

public class ShadowLayout extends RelativeLayout {

    public static final int ALL = 0x1111;

    public static final int LEFT = 0x0001;

    public static final int TOP = 0x0010;

    public static final int RIGHT = 0x0100;

    public static final int BOTTOM = 0x1000;

    public static final int SHAPE_RECTANGLE = 0x0001;

    public static final int SHAPE_OVAL = 0x0010;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private RectF mRectF = new RectF();
    private int mShadowColor = Color.TRANSPARENT;

    private float mShadowRadius = 0;

    private float mShadowDx = 0;

    private float mShadowDy = 0;

    private int mShadowSide = ALL;

    private int mShadowShape = SHAPE_RECTANGLE;

    public ShadowLayout(Context context) {
        this(context, null);
    }

    public ShadowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void setShadowSide(String t,String r,String b,String l){
        if (t.equals("T")){
            mShadowSide = TOP;
        }
        if (r.equals("R")){
            mShadowSide = RIGHT;
        }
        if (b.equals("B")){
            mShadowSide = BOTTOM;
        }
        if (l.equals("L")){
            mShadowSide = LEFT;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float effect = mShadowRadius + dip2px(5);
        float rectLeft = 0;
        float rectTop = 0;
        float rectRight = this.getMeasuredWidth();
        float rectBottom = this.getMeasuredHeight();
        int paddingLeft = 0;
        int paddingTop = 0;
        int paddingRight = 0;
        int paddingBottom = 0;
        this.getWidth();
        if ((mShadowSide & LEFT) == LEFT) {
            rectLeft = effect;
            paddingLeft = (int) effect;
        }
        if ((mShadowSide & TOP) == TOP) {
            rectTop = effect;
            paddingTop = (int) effect;
        }
        if ((mShadowSide & RIGHT) == RIGHT) {
            rectRight = this.getMeasuredWidth() - effect;
            paddingRight = (int) effect;
        }
        if ((mShadowSide & BOTTOM) == BOTTOM) {
            rectBottom = this.getMeasuredHeight() - effect;
            paddingBottom = (int) effect;
        }
        if (mShadowDy != 0.0f) {
            rectBottom = rectBottom - mShadowDy;
            paddingBottom = paddingBottom + (int) mShadowDy;
        }
        if (mShadowDx != 0.0f) {
            rectRight = rectRight - mShadowDx;
            paddingRight = paddingRight + (int) mShadowDx;
        }
        mRectF.left = rectLeft;
        mRectF.top = rectTop;
        mRectF.right = rectRight;
        mRectF.bottom = rectBottom;
        this.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setUpShadowPaint();
        if (mShadowShape == SHAPE_RECTANGLE) {
            canvas.drawRect(mRectF, mPaint);
        } else if (mShadowShape == SHAPE_OVAL) {
            canvas.drawCircle(mRectF.centerX(), mRectF.centerY(), Math.min(mRectF.width(), mRectF.height()) / 2, mPaint);
        }
    }

    private void init(AttributeSet attrs) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        this.setWillNotDraw(false);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ShadowLayout);
        if (typedArray != null) {
            mShadowColor = typedArray.getColor(R.styleable.ShadowLayout_shadowColor,
                    getContext().getResources().getColor(android.R.color.black));
            mShadowRadius = typedArray.getDimension(R.styleable.ShadowLayout_shadowRadius, dip2px(0));
            mShadowDx = typedArray.getDimension(R.styleable.ShadowLayout_shadowDx, dip2px(0));
            mShadowDy = typedArray.getDimension(R.styleable.ShadowLayout_shadowDy, dip2px(0));
            mShadowSide = typedArray.getInt(R.styleable.ShadowLayout_shadowSide, ALL);
            mShadowShape = typedArray.getInt(R.styleable.ShadowLayout_shadowShape, SHAPE_RECTANGLE);
            typedArray.recycle();
        }
        setUpShadowPaint();
    }

    private void setUpShadowPaint() {
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setShadowLayer(mShadowRadius, mShadowDx, mShadowDy, mShadowColor);
    }

    private float dip2px(float dpValue) {
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        float scale = dm.density;
        return (dpValue * scale + 0.5F);
    }
}