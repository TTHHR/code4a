package cn.atd3.code4a.model;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import cn.atd3.code4a.net.Remote;
import cn.atd3.code4a.presenter.interfaces.SigninUserContract;
import cn.atd3.proxy.exception.PermissionException;
import cn.atd3.proxy.exception.ServerException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：YGL
 * 版本号：1.0
 * 类描述：
 * 备注消息：
 * 创建时间：2018/03/09   9:42
 **/
public class SigninUserModel implements SigninUserContract.Model{
    @Override
    public Observable<Boolean> setEmail(final String email) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                Object o=null;
                try {
                    o= Remote.user.method("setEmail").call(email);
                }catch (ServerException e){
                    Logger.i("ServerException:"+e);
                }catch (PermissionException e){
                    Logger.i("ServerException:"+e);
                }catch (IOException e){
                    Logger.i("ServerException:"+e);
                }
                subscriber.onNext(Boolean.valueOf(o.toString()));
            }
        }).subscribeOn(Schedulers.io())//在其他线程执行
                .observeOn(AndroidSchedulers.mainThread());//在主线程触发
    }

    @Override
    public Observable<Boolean> setAvatar(String avatar) {
        return null;
    }

    @Override
    public Observable<Boolean> setPassword(String password) {
        return null;
    }
}
