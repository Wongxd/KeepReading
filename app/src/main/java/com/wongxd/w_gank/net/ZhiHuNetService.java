package com.wongxd.w_gank.net;


import com.wongxd.w_gank.model.ZhiHuBean;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by wxd1 on 2017/4/7.
 */

public interface ZhiHuNetService {
    @GET("news/latest")
    Observable<ZhiHuBean> getZhiHuLatest();

    @GET("news/before/{time}")
    Observable<ZhiHuBean> getZhiHuBefore(@Path("time") String time);

    @GET("news/{id}")
    Observable<ResponseBody> getZhiHuDetail(@Path("id") int id);
}
