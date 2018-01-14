package cn.atd3.code4a.presenter;

import android.text.TextUtils;
import android.util.Log;

import cn.atd3.code4a.presenter.interfaces.SignupContract;
import rx.Subscriber;

/**
 * 作者：YGL
 * 电话：13036804886
 * 邮箱：2369015621@qq.com
 * 版本号：1.0
 * 类描述：
 * 备注消息：
 * 创建时间：2018/01/12   10:19
 **/
public class SignupPresenter extends SignupContract.Presenter {
    private static final String TAG = "SignupPresenter";
    private String user;
    private String email;
    private String password;
    @Override
    public void signupButtonClick(String user,String email, String password) {
        this.user=user;
        this.email=email;
        this.password=password;
        if (checkUser()&&checkEmail()&&checkPassword()){
            //提交数据
            mModel.signup(user,email,password,null).subscribe(new Subscriber<Object>() {
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
                    Log.i("xxxxx","o:"+o);
                    Integer i=Integer.valueOf(o.toString());
                    if(i>=0){
                        //注册成功
                        mView.signupSuccessful();
                    }else {
                        //失败
                        mView.remoteError(String.valueOf(i));
                    }
                }
            });
        }else {
            mView.userNameError("Error!");
        }
    }

    private Boolean checkUser(){
        return !TextUtils.isEmpty(user);
    }
    private Boolean checkEmail(){
        return !TextUtils.isEmpty(email);
    }
    private Boolean checkPassword(){
        return !TextUtils.isEmpty(password);
    }
}
