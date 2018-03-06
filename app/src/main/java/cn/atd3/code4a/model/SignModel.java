package cn.atd3.code4a.model;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import cn.atd3.code4a.model.inter.Sign;
import cn.atd3.code4a.net.Remote;
import cn.atd3.proxy.exception.PermissionException;
import cn.atd3.proxy.exception.ServerException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：YGL
 * 电话：13036804886
 * 邮箱：2369015621@qq.com
 * 版本号：1.0
 * 类描述：负责提供相应数据
 * 备注消息：
 * 创建时间：2018/01/10   22:17
 **/
public class SignModel implements Sign {
    public static final int SIGNIN=1;
    public static final int SIGNUP=2;
    @Override
    public Observable<Integer> signin(final String account, final String password, final Boolean remember, final String code) {
        //这里使用了Rxjava
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                Object o=null;
                try {
                    o= Remote.user.method("signin").call(account,password,remember,code);
                }catch (ServerException e){
                    Logger.i("ServerException:"+e);
                }catch (PermissionException e){
                    Logger.i("PermissionException:"+e);
                }catch (IOException e){
                    Logger.i("IOException:"+e);
                }
                Integer i=Integer.valueOf(o.toString());
                subscriber.onNext(i);
            }
        }).subscribeOn(Schedulers.io())//在其他线程执行
          .observeOn(AndroidSchedulers.mainThread());//在主线程触发
    }

    @Override
    public Observable<Integer> signup(final String user, final String email, final String password,final String code) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                Object o=null;
                try {
                    o= Remote.user.method("signup").call(user,email,password,code);
                }catch (ServerException e){
                    Logger.i("ServerException:"+e);
                }catch (PermissionException e){
                    Logger.i("ServerException:"+e);
                }catch (IOException e){
                    Logger.i("ServerException:"+e);
                }
                Integer i=Integer.valueOf(o.toString());
                subscriber.onNext(i);
            }
        }).subscribeOn(Schedulers.io())//在其他线程执行
          .observeOn(AndroidSchedulers.mainThread());//在主线程触发
    }
}
