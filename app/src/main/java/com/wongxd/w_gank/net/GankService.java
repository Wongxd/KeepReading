package com.wongxd.w_gank.net;

import com.wongxd.w_gank.model.GankBean;
import com.wongxd.w_gank.model.MeiZiBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by wxd1 on 2017/4/19.
 */

public interface GankService {

    @GET("day/{year}/{month}/{day}")
    Observable<GankBean> getOneDay(@Path("year") String year, @Path("month") String month, @Path("day") String day);

    @GET("data/Android/10/{page}")
    Observable<GankBean> getAndroid(@Path("page") int page);

    
    @GET("data/福利/10/{page}")
    Observable<MeiZiBean> getMeiZi(@Path("page") int page);
}
