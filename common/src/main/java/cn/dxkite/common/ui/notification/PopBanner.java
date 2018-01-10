package cn.dxkite.common.ui.notification;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.dxkite.common.ui.notification.popbanner.Adapter;
import cn.dxkite.common.ui.notification.popbanner.Information;

/**
 * 弹窗通知
 * TODO: 支持XML布局
 * Created by DXkite on 2018/1/10 0010.
 */

public class PopBanner extends View {


    private Adapter messageAdapter;
    private TextView textView;
    private ImageView imageView;
    private PopupWindow popupWindow;
    private final String TAG = "PopBanner";
    private View container = null;
    private int imageResource = 0;
    private int backgroundColor = Color.parseColor("#FFC125");
    private Information information;
    private LinearLayout layout;
    private Handler dismissHandler;


    public PopBanner(Context context, View container, int imageResource) {
        super(context);
        this.imageResource = imageResource;
        this.container = container;
        initView();
    }

    public Adapter getMessageAdapter() {
        return messageAdapter;
    }

    public void setMessageAdapter(Adapter messageAdapter) {
        this.messageAdapter = messageAdapter;
    }

    /**
     * 更新信息
     */
    public void update() {
        Adapter adapter = getMessageAdapter();
        if (adapter != null) {
            information = adapter.refersh();
        } else {
            Log.e(TAG, "adapter is null");
        }
    }

    /**
     * 显示
     */
    public void show() {
        // UI线程调用
        dismissHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                dismiss();
            }
        };

        if (!popupWindow.isShowing()) {
            imageView.setImageResource(imageResource);
            if (information.getBackgroundColor() != null && !TextUtils.isEmpty(information.getBackgroundColor())) {
                layout.setBackgroundColor(Color.parseColor(information.getBackgroundColor()));
            } else {
                layout.setBackgroundColor(backgroundColor);
            }
            if (information.getColor() != null && !TextUtils.isEmpty(information.getColor())) {
                textView.setTextColor(Color.parseColor(information.getColor()));
            }
            if (information.isTouchable()) {
                popupWindow.setTouchable(true);
                textView.setClickable(true);
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(information.getUrl()));
                        getContext().startActivity(i);
                    }
                });
            }
            // 自动消失
            if (information.getTime() > 0) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(information.getTime());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        dismissHandler.sendEmptyMessage(0);
                    }
                }).start();
            }
            textView.setText(information.getMessage());
            popupWindow.showAsDropDown(getContainer(), 0, 0);
        }
    }

    public void dismiss() {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
        imageView.setImageResource(imageResource);
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public View getContainer() {
        return container;
    }

    private void initView() {
        layout = new LinearLayout(getContext());
        textView = new TextView(getContext());
        imageView = new ImageView(getContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        textView.setSingleLine();
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setSelected(true);
        layout.addView(imageView);
        layout.addView(textView);
        layout.setGravity(Gravity.CENTER);
        popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setTouchable(false);
    }

}
