package com.example.homeactivity.Filter;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ServiceApi {

    /** 필터 */
    @GET("preview/{style_id}/{OriginalUrl}")
    Call<ImageResult> SendInfo(@Path("style_id") int style_id,
                                  @Path("OriginalUrl") String OriginalUrl);

    @GET("transfer/{style_id}/{OriginalUrl}")
    Call<ImageResult> ResultImage(@Path("style_id") int style_id,
                                  @Path("OriginalUrl") String OriginalUrl);
}
