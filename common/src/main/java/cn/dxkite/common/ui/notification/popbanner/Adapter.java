package cn.dxkite.common.ui.notification.popbanner;

/**
 * Created by DXkite on 2018/1/10 0010.
 * 适配器，用于通知获取
 *
 */

public interface Adapter {
    /**
     * 获取更新通知信息
     * @return 通知信息内容
     */
    public Information refersh();
}
