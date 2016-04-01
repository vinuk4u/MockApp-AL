package com.al.mockapp.webservice;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.al.mockapp.models.response.MAResponseArrayModel;
import com.al.mockapp.models.MAStudentModel;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vineeth on 31/03/16
 */

/**
 * Volley adapter for JSON requests with POST method that will be parsed into Java objects by Gson.
 */
public class MAGsonRequest<T> extends Request<T> {
    private final Response.Listener<T> successListener;
    private Gson mGson = new Gson();

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url URL of the request to make
     */
    public MAGsonRequest(int method, String url,
                         Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);

        this.successListener = listener;

        mGson = new Gson();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return null;
    }

    @Override
    public String getBodyContentType() {
        return "application/json; charset=utf-8; ";
    }

    @Override
    protected void deliverResponse(T response) {
        successListener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            final String json = new String(
                    response.data, HttpHeaderParser.parseCharset(response.headers));

            Type listType = new TypeToken<List<MAStudentModel>>() {
            }.getType();
            final MAResponseArrayModel arrayModel = new MAResponseArrayModel();
            arrayModel.setResponseModels((ArrayList<MAStudentModel>) mGson.fromJson(json, listType));
            return Response.success(
                    (T) arrayModel, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            Log.d("UnsupportedException", "");
            e.printStackTrace();
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            Log.d("JsonSyntaxException", "");
            e.printStackTrace();
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            Log.d("Exception", "");
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }
}
