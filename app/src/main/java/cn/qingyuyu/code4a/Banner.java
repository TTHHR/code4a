package cn.qingyuyu.code4a;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * Created by harry on 2018/1/7.
 */

public class Banner {
    private View parent=null;
  private   PopupWindow window=null;
  private   Context context=null;
    public  Banner(View v,Context context)
    {

        parent=v;
        this.context=context;
        // 用于PopupWindow的View
           View contentView= LayoutInflater.from(context).inflate(R.layout.banner, null, false);
         // 创建PopupWindow对象，其中：
          // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
          // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
         window=new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
        // 设置PopupWindow是否能响应外部点击事件
           window.setOutsideTouchable(false);
           // 设置PopupWindow是否能响应点击事件
          window.setTouchable(false);

    }

    public void show()
    {
       if(!window.isShowing()) {
           window.showAsDropDown(parent, 0, 0);
       }
    }
    public void dismiss()
    {
        if(window.isShowing())
            window.dismiss();
    }

}
