package cn.dxkite.common.ui.notification.popbanner;

/**
 * 弹窗信息
 * Created by DXkite on 2018/1/10 0010.
 */

public class Information {

    private String message = "this is default information!";
    private String url = null;
    private boolean touchable = false;
    private int time = 2000;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isTouchable() {
        return touchable;
    }

    public void setTouchable(boolean touchable) {
        this.touchable = touchable;
    }
}
