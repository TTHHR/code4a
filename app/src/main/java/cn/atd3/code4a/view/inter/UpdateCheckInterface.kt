package cn.atd3.code4a.view.inter

import cn.atd3.code4a.model.model.UpdateInfo

/**
 * 更新检查界面接口
 * Created by DXkite on 2018/3/22 0022.
 */

interface UpdateCheckInterface {
    /**
     * 通知更新信息
     */
    fun noticeUpdateInfo(version:UpdateInfo)

    /**
     * 通知更新图标下载完成
     */
    fun noticeUpdateIcon()
}

