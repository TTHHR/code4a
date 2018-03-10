package cn.atd3.code4a.presenter;

import cn.atd3.code4a.R;
import cn.atd3.code4a.presenter.interfaces.SigninUserContract;
import rx.Subscriber;

/**
 * 作者：YGL
 * 版本号：1.0
 * 类描述：
 * 备注消息：
 * 创建时间：2018/03/09   9:43
 **/
public class SigninUserPresenter extends SigninUserContract.Presenter {
    @Override
    public void changeEmail(String email) {
        mModel.setEmail(email).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showErrorWithStatus(e.getMessage());
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean){
                    mView.showErrorWithStatus(mContext.getString(R.string.change_failure));
                }else {
                    mView.setEmailSuccessful();
                }
            }
        });
    }

    @Override
    public void changeAvatar(String avatar) {

    }

    @Override
    public void changePassword(String password) {

    }
}
