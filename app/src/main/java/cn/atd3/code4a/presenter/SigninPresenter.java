package cn.atd3.code4a.presenter;

import android.text.TextUtils;
import android.util.Log;

import cn.atd3.code4a.presenter.interfaces.SigninContract;
import rx.Subscriber;

/**
 * 作者：YGL
 * 电话：13036804886
 * 邮箱：2369015621@qq.com
 * 版本号：1.0
 * 类描述：主要负责处理业务逻辑
 * 备注消息：
 * 创建时间：2018/01/10   22:15
 **/
public class SigninPresenter extends SigninContract.Presenter {
    private static final String TAG = "SigninPresenter";
    private String account;
    private String password;
    @Override
    public void signinButtonClick(String account,String password) {
        this.account=account;
        this.password=password;
        //检查用户名和密码
        if (checkAccount()&&checkPassword()){
            //提交
            mModel.signin(account,password,false,null).subscribe(new Subscriber<Object>() {
                @Override
                public void onCompleted() {
                    Log.i(TAG,"onCompleted()");
                }

                @Override
                public void onError(Throwable e) {
                    Log.i(TAG,"onError(Throwable e) e:"+e);
                }

                @Override
                public void onNext(Object o) {
                    //在Model中的subscriber.onNext(o);就是调用的这个方法
                    Integer i=Integer.valueOf(o.toString());
                    if(i>=0){
                        //登陆成功
                        mView.signinSuccessful();
                    }else {
                        //失败
                        mView.remoteError(String.valueOf(i));
                    }
                }
            });
        }else {
            mView.accountError("Error!");
        }
    }

    private Boolean checkAccount(){
        return !TextUtils.isEmpty(account);
    }
    private Boolean checkPassword(){
        return !TextUtils.isEmpty(password);
    }
}
