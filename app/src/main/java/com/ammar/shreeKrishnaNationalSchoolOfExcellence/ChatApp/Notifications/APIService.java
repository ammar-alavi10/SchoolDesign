package com.ammar.shreeKrishnaNationalSchoolOfExcellence.ChatApp.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAnV-UbCY:APA91bGooRxUqWc9LnBaQXuTIybwMr6FMio5Htp2nfxe7LrhosffOFY8fy9hbhHsrA-MXyC5ZqIJcnxK7J6kha-EWShfRpjIFmle4dPt-p_ZfGao-NPJhsOil0dQdxZvPhCMngEMvw0e"
    })

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);
}
