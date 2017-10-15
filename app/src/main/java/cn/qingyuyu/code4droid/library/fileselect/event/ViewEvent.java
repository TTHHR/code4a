package cn.qingyuyu.code4droid.library.fileselect.event;

import android.os.Bundle;

/**
 * Created by admin on 2016/11/23.
 */
public class ViewEvent {

    private EvenType mType;
    private Bundle mArgs;

    public ViewEvent(EvenType type, Bundle args) {
        this.mType = type;
        this.mArgs = args;
    }

    public EvenType getType() {
        return mType;
    }

    public void setType(EvenType type) {
        this.mType = type;
    }

    public Bundle getArgs() {
        return mArgs;
    }

    public void setArgs(Bundle args) {
        this.mArgs = args;
    }

    /**
     * 更新界面事件类型.
     * KeyCodeBack  back
     * updateToolbarFileInfo  更新toolbar内容
     * SetItemThumb           设置缩略图
     * updateTabTitle         tab title 更新
     */
    public enum EvenType {
        hideTop,
        gotoFileClickPosition,
        backPath,
    }

    public static class Keys{
        public static final String GOTO_PATH = "goto_path";
        public static final String BACK_PATH = "back_path";
        public static final String BACK_PATH_HEADER = "back_path_header";
    }

}
