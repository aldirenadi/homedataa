package com.aldi.dacari.notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
        "Content-Type:application/json",
        "Authorization:key=AAAAQusltsM:APA91bFfbF4LQHjwuFCONAKRp4akDcDWMXQLtwLA5NkoQnIC23URthwp_G0RMX3gIdviwIffvi7adGXYVf9foMrT0vv0RRSHtYSKmEOD28SCLiiT4JfZ79v_6k3og5aQgNbHMYMa5Tg5"
    })

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);

}
