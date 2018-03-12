package cn.atd3.code4a.model;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;

import cn.atd3.code4a.model.model.User;
import cn.atd3.code4a.net.Remote;
import cn.atd3.code4a.presenter.interfaces.SigninUserContract;
import cn.atd3.proxy.Param;
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
    public Observable<Boolean> setAvatar(final File avatar) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                Object o=null;
                try {
                    o= Remote.user.method("setAvatar").call(new Param("file",avatar));
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
    public Observable<Boolean> setPassword(final String oldPwd, final String newPwd) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                Object o=null;
                try {
                    o= Remote.user.method("setPassword").call(oldPwd,newPwd);
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
    public Observable<User> getUserInfo() {
        return Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(Subscriber<? super User> subscriber) {
                Object o=null;
                try {
                    o= Remote.user.method("info").call();
                }catch (ServerException e){
                    Logger.i("ServerException:"+e);
                }catch (PermissionException e){
                    Logger.i("ServerException:"+e);
                }catch (IOException e){
                    Logger.i("ServerException:"+e);
                }
                Gson gson=new Gson();
                User user=gson.fromJson(o.toString(),User.class);
                subscriber.onNext(user);
            }
        }).subscribeOn(Schedulers.io())//在其他线程执行
                .observeOn(AndroidSchedulers.mainThread());//在主线程触发
    }
}
