package es.jarroyo.cleanproject.forecast.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import es.jarroyo.cleanproject.BuildConfig;
import es.jarroyo.cleanproject.base.RequestError;
import es.jarroyo.cleanproject.forecast.source.DataSourceInterface;
import es.jarroyo.cleanproject.forecast.model.domain.model.Data;
import es.jarroyo.cleanproject.utils.ApiConstants;
import es.jarroyo.cleanproject.utils.NetworkUtils;

import static com.google.common.base.Preconditions.checkNotNull;


public class RemoteDataSource implements DataSourceInterface {

    private static RemoteDataSource INSTANCE;
    private Context mContext;
    private RequestQueue mRequestQueue;

    private RemoteDataSource(@NonNull Context context) {
        checkNotNull(context);
        mContext = context;
    }

    public static RemoteDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getData(@NonNull LoadDataCallback callback, Double latitude, Double longitud) {
        if (NetworkUtils.isNetworkAvailable(mContext)) {
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(mContext);
            }
            makeRequestGetData(callback, latitude, longitud);
        } else {
            RequestError errorCallback = new RequestError();
            errorCallback.setCodeError(RequestError.ERROR_NO_INTERNET);
            callback.onError(errorCallback);
        }
    }

    private void makeRequestGetData(final LoadDataCallback callback, Double latitud, Double longitud) {
        String url = prepareUrlGetData(latitud, longitud);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        proccessData(response, callback);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                RequestError errorCallback = new RequestError();
                callback.onError(errorCallback);
            }
        });
        ;
        // Add the request to the RequestQueue.
        mRequestQueue.add(stringRequest);

    }

    private String prepareUrlGetData(Double lat, Double longitud) {
        String url = ApiConstants.URL_BASE_OPEN_WEATHER_API + ApiConstants.URL_OPEN_WEATHER_API_FORECAST;
        url = url + "?lat=" + lat + "&lon=" + longitud + "&APPID=" + BuildConfig.API_KEY_OPEN_WEATHER+"&units=metric";
        return url;
    }


    private void proccessData(String response, LoadDataCallback callback) {
        callback.onSuccess(parseResponse(response));
    }

    private List<Data> parseResponse(String response) {
        List<Data> listData = new ArrayList<>();
        Gson gson = new Gson();
        try {
            JSONObject mainObject = new JSONObject(response);
            if (mainObject.has(ApiConstants.KEY_OPEN_WEATHER_API_INFO)) {
                JSONArray jsonArray = mainObject.getJSONArray(ApiConstants.KEY_OPEN_WEATHER_API_INFO);
                Type listType = new TypeToken<List<Data>>() {
                }.getType();
                listData = (List<Data>) gson.fromJson(jsonArray.toString(), listType);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return listData;
        }
    }
}
