package com.example.andrearubeis.wash_up;



import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
        @Multipart
        @POST("upload.php")
        Call<Result> uploadImage(@Part MultipartBody.Part file);

}