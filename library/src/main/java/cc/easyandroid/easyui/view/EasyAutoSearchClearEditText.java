package cc.easyandroid.easyui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * @author cgp
 */
public class EasyAutoSearchClearEditText extends EditText implements View.OnFocusChangeListener, TextWatcher {

    /**
     * 清空图标 *
     */
    private Drawable mClearDrawable;

    /**
     * 是否获得焦点 *
     */
    private boolean hasFocus;

    private OnAutoSearchListener onAutoSearchListener;

    public interface OnAutoSearchListener {
        void search(String s);
    }

    public void setOnAutoSearchListener(OnAutoSearchListener onAutoSearchListener) {
        this.onAutoSearchListener = onAutoSearchListener;
    }

    public EasyAutoSearchClearEditText(Context context) {
        this(context, null);
    }

    public EasyAutoSearchClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public EasyAutoSearchClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 获取EditText最右侧的删除图标
        mClearDrawable = this.getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            mClearDrawable = getResources().getDrawable(android.R.drawable.ic_delete);
        }
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        // 默认右侧删除图标不可见
        setClearIconVisible(false);

        setOnFocusChangeListener(this);
        super.addTextChangedListener(this);
    }

    private TextWatcher watcher;

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        // super.addTextChangedListener(watcher);//屏蔽调用父类，自己处理
        this.watcher = watcher;
    }

    /**
     * 设置右侧的删除图标是否可见
     *
     * @param visible
     */
    public void setClearIconVisible(boolean visible) {
        Drawable drawable = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], drawable, getCompoundDrawables()[3]);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (watcher != null) {
            watcher.beforeTextChanged(s, start, count, after);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (onAutoSearchListener != null) {
            onAutoSearchListener.search(s.toString());
        }
        if (watcher != null) {
            watcher.afterTextChanged(s);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFocus = hasFocus;
        if (hasFocus) {
            // 有焦点时焦点，当EditText里的内容长度>0时，显示图标，否则隐藏图标
            setClearIconVisible(getText().length() > 0);
        } else {
            // 失去焦点，不显示清空图标
            setClearIconVisible(false);
        }
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int before, int count) {
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        }
        if (watcher != null) {
            watcher.onTextChanged(text, start, before, count);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                // 判断是否触摸的为右侧图标
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight()) && (event.getX() < (getWidth() + getTotalPaddingRight()));
                if (touchable) {
                    // 如果触摸了右侧图标，清空文字
                    setText("");
                    return true;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }
}
