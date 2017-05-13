package ds.todoapp.api;

import java.util.ArrayList;

import ds.todoapp.models.Todo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Duygu on 12/05/2017.
 */

public interface TodosAPIService {

    @GET("users/{id}/todos")
    Call<ArrayList<Todo>> getTasks(
            @Header("Authorization") String apiKey,
            @Path("id") String userId);

    @POST("users/{id}/todos")
    Call<Todo> add(
            @Header("Authorization") String apiKey,
            @Path("id") String userId,
            @Body Todo todo);


}
