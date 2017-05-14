package ds.todoapp.api;

import java.util.ArrayList;

import ds.todoapp.models.Todo;
import ds.todoapp.models.Todos;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Duygu on 12/05/2017.
 */

public interface TodosAPIService {

    @GET("users/{id}/todos")
    Call<Todos> getTasks(
            @Header("Authorization") String apiKey,
            @Path("id") String userId);

    @POST("users/{id}/todos")
    Call<Todo> add(
            @Header("Authorization") String apiKey,
            @Path("id") String userId,
            @Body Todo todo);

    @GET("todos/{id}")
    Call<Todo> get(
            @Header("Authorization") String apiKey,
            @Path("id") String todoId);

    @PUT("todos/{id}")
    Call<Todo> update(
            @Header("Authorization") String apiKey,
            @Path("id") String todoId,
            @Body Todo todo);

    @DELETE("todos/{id}")
    Call<Todo> delete(
            @Header("Authorization") String apiKey,
            @Path("id") String todoId);

}
