package com.musicapp.retrofit;


import com.musicapp.models.BinauralPojo;
import com.musicapp.models.CategoryPojoModel;
import com.musicapp.models.DataPojo;
import com.musicapp.models.ChangePassModel;
import com.musicapp.models.EffectsPojo;
import com.musicapp.models.ExplorePojo;
import com.musicapp.models.LoginVideoModel;
import com.musicapp.models.NotificationPojo;
import com.musicapp.models.PrivacyPolicyPojo;
import com.musicapp.models.SearchPojo;
import com.musicapp.models.User;
import com.musicapp.models.UserDataPojo;
import com.musicapp.models.ViewAllPojo;
import com.musicapp.models.ViewProfileModel;
import com.musicapp.retrofit.response.RestResponse;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * @author PA1810.
 */


// for api calling stuff

public interface APIInterface {

    @Headers({"Content-Type:application/json"})
    @POST("signup")
    Call<RestResponse<User>> signup(@Body HashMap<String, Object> hashMap);

    @GET("songsList")
    Call<RestResponse<CategoryPojoModel>> getCategories();

    @GET("effectCategoryList")
    Call<RestResponse<DataPojo>> getEffectCategories();


    @GET("getLoginPageVideo")
    Call<RestResponse<LoginVideoModel>> getLoginVideo();


    @Headers({"Content-Type:application/json"})
    @POST("login")
    Call<RestResponse<User>> login(@Body HashMap<String, Object> hashMap);

    @Headers({"Content-Type:application/json"})
    @POST("facebookLogin")
    Call<RestResponse<User>> facebooklogin(@Body HashMap<String, Object> hashMap);

    @Headers({"Content-Type:application/json"})
    @POST("googleLogin")
    Call<RestResponse<User>> googleLogin(@Body HashMap<String, Object> hashMap);

    @Headers({"Content-Type:application/json"})
    @POST("personlize")
    Call<RestResponse<User>> submitPersonalized(@Body HashMap<String, Object> hashMap);

    @GET("dashboard")
    Call<RestResponse<DataPojo>> dashboardData();

    @GET("songsList")
    Call<RestResponse<DataPojo>> getSongList();


    @PUT("changePassword")
    Call<RestResponse<ChangePassModel>> changePassword(@Body HashMap<String, Object> map);

    @POST("logout")
    Call<RestResponse<User>> logout();

    @GET("user")
    Call<RestResponse<UserDataPojo>> viewprofile(@Query("id") String user_id);


    @Headers({"Content-Type:application/json"})
    @GET("searchSongsList")
    Call<RestResponse<SearchPojo>> search(@Query("name") String name);


    @Multipart
    @PUT("userUpdate")
    Call<RestResponse<UserDataPojo>> editProfile2(
            @Part("username") RequestBody usernam,
            @Part("email") RequestBody ema,
            @Part("gender") RequestBody gen,
            @Part("firstname") RequestBody first,
            @Part("lastname") RequestBody last,
            @Part("phone") RequestBody phon,
            @Part("work") RequestBody wor,
            @Part MultipartBody.Part imageFile
    );

    @GET("exploreSongsList")
    Call<RestResponse<ExplorePojo>> exploreSongs();

    @GET("searchCategoryList")
    Call<RestResponse<CategoryPojoModel>> searchCategory();

    @GET("binauralEffectSongsList")
    Call<RestResponse<BinauralPojo>> binauralEffect();


    @GET("mostPopularSongs")
    Call<RestResponse<ViewAllPojo>> mostPopular();


    @GET("userNotificationList")
    Call<RestResponse<NotificationPojo>> getNotifications();





    /*"username": "b2",
            "email": "a91@gmail.com",
            "gender": "male",
            "firstname": "b1",
            "lastname": "2",
            "image": "http://163.47.212.61:8000/media/user_img/993cbbfe454b00bb3ef4fa511fa3469c_06hO8GL.jpg",
            "phone": "1236547890",
            "work": "dev"*/


    @Headers({"Content-Type:application/json"})
    @POST("songAddToFavourite")
    Call<RestResponse<User>> addToFavourite(@Body HashMap<String, Object> hashMap);

    @Headers({"Content-Type:application/json"})
    @POST("removeUserFavourite")
    Call<RestResponse<User>> removeFromFavourite(@Body HashMap<String, Object> hashMap);

    @GET("songsList")
    Call<RestResponse<DataPojo>> getSongList(@Query("id") int id);

    @GET("favouriteSongList")
    Call<RestResponse<DataPojo>> getFavouriteSongList(@Query("user") String id);

    @Headers({"Content-Type:application/json"})
    @POST("forgot-password")
    Call<RestResponse<User>> forgotpass(@Body HashMap<String, Object> hashMap);

    @GET("staticPage")
    Call<RestResponse<PrivacyPolicyPojo>> privacyPolicy(@Query("pagerole") String pageroll);

    @PUT("updateTodayspecialVideoCount")
    Call<RestResponse<User>> updatevideocount(@Body HashMap<String, Object> map);

    @Headers({"Content-Type:application/json"})
    @POST("contactUs")
    Call<RestResponse<User>> contactUs(@Body HashMap<String, Object> hashMap);

    @GET("songDetails")
    Call<RestResponse<User>> songDetail(@Query("id") int id);

    @Headers({"Content-Type:application/json"})
    @POST("addAlarmNotification")
    Call<RestResponse<User>> addAlarm(@Body HashMap<String, Object> hashMap);

    @Headers({"Content-Type:application/json"})
    @POST("addDurationNotification")
    Call<RestResponse<User>> setDuration(@Body HashMap<String, Object> hashMap);






   /* @GET("researcher_list")
    Call<ListResponse<User>> researcherList();

    @POST("signup")
    Call<RestResponse<User>> signup(@Body HashMap<String, Object> hashMap);

    @Multipart
    @POST("signup")
    Call<RestResponse<User>> signupp(@PartMap HashMap<String, RequestBody> map, @Part MultipartBody.Part part);

    @POST("login")
    Call<RestResponse<User>> login(@Body HashMap<String, Object> hashMap);

    @POST("save_profile")
    Call<RestResponse<User>> saveProfile(@Body HashMap<String, Object> hashMap);


    @Multipart
    @POST("save_profile")
    Call<RestResponse<User>> saveProfilee(@PartMap HashMap<String, RequestBody> hashMap, @Part MultipartBody.Part part);

    @GET("get_researcher_info")
    Call<RestResponse<User>> getResearcherInfo(@Query("user_id") int user_id);

    @POST("change_password")
    Call<RestResponse> changePassword(@Body HashMap<String, Object> hashMap);


    @POST("save_consent")
    Call<RestResponse> saveConsent(@Body HashMap<String, Object> hashMap);

    @GET("audio_list")
    Call<ListResponse<MediaData>> audioList(@Query("user_id") int user_id);

    @GET("video_list")
    Call<ListResponse<MediaData>> videoList(@Query("user_id") int user_id);

    @GET("participant_list")
    Call<ListResponse<Participant>> participantList(@Query("user_id") int user_id);

    @GET("delete_media")
    Call<RestResponse> deleteMedia(@Query("id") int id);

    @POST("delete_postlist")
    Call<DeletePostResponse> deletePost(@Query("id") String id);


    @Multipart
    @POST("upload_media")
    Call<RestResponse<MediaData>> uploadMedia(@Query("user_id") int user_id,
                                              @Query("media_type") int mediaType,
                                              @Query("title") String title,
                                              @Part MultipartBody.Part image);


    @Multipart
    @POST("upload_multimedia")
    Call<RestResponse<MediaData>> uploadMediaoffline(@Query("user_id") int user_id,
                                                     @Query("media_type") int mediaType,
                                                     @Query("title") JSONArray name,
                                                     @Part ArrayList<MultipartBody.Part> image);




    @GET("forgotPassword")
    Call<ForgotPassword> forgotPass(@Query("email") String email);
*/
}