package org.trendtube.app.volley;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.trendtube.app.model.ResponseMetadata;

import java.io.UnsupportedEncodingException;
import java.util.Map;
/**
 * Created by shankar on 9/12/15.
 */
public class TTGsonRequest<T extends ResponseMetadata> extends TTRequest<T> {

	private final Class<T> mClazz;

	/**
	 * @param activity
	 *            :Context
	 * @param url
	 *            :URL
	 * @param params
	 *            :parameters
	 * @param listener
	 *            :TTResponseListener on which Response callback will occur
	 * @param clazz
	 *            :Expecting model class
	 */
	public TTGsonRequest(Context activity, String url, Map<String, String> params, TTResponseListener<T> listener, Class<T> clazz) {
		this(activity, params==null? Request.Method.GET: Request.Method.POST, url, params, listener, null, clazz, Request.Priority.NORMAL);

      //  setRetryPolicy(new TTRetryPolicy(100,3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

	/**
	 * 
	 * 
	 * @param activity
	 *            :Context
	 * @param method
	 *            :com.android.volley.Request.Method
	 * @param url
	 *            :URL
	 * @param params
	 *            :parameters
	 * @param listener
	 *            :TTResponseListener on which Response callback will occur
	 * @param clazz
	 *            :Expecting model class
	 */
	public TTGsonRequest(Context activity, int method, String url, Map<String, String> params, TTResponseListener<T> listener, Class<T> clazz) {
		this(activity, method, url, params, listener, null, clazz, Request.Priority.NORMAL);

	}

	/**
	 * @param activity
	 *            :Context
	 * @param method
	 *            :com.android.volley.Request.Method
	 * @param url
	 *            :URL
	 * @param params
	 *            :parameters
	 * @param listener
	 *            :TTResponseListener on which Response callback will occur
	 * @param taskId
	 *            :this key is for developer purpose it is same like tag
	 * @param clazz
	 *            :Expecting model class
	 */
	public TTGsonRequest(Context activity, int method, String url, Map<String, String> params, TTResponseListener<T> listener, Object taskId, Class<T> clazz) {
		this(activity, method, url, params, listener, taskId, clazz, Request.Priority.NORMAL);

	}

	/**
	 * @param activity
	 *            :Context
	 * @param method
	 *            :com.android.volley.Request.Method
	 * @param url
	 *            :URL
	 * @param params
	 *            :parameters
	 * @param listener
	 *            :TTResponseListener on which Response callback will occur
	 * @param taskId
	 *            :this key is for developer purpose it is same like tag
	 * @param clazz
	 *            :Expecting model class
	 * @param priority
	 *            :Priority of request
	 */
	public TTGsonRequest(Context activity, int method, String url, Map<String, String> params, TTResponseListener<T> listener, Object taskId, Class<T> clazz, Request.Priority priority) {
		super(activity, method, url, params, listener, taskId, priority);
		this.mClazz = clazz;

	}

	@Override
	protected void deliverResponse(T response) {

        super.deliverResponse(response);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println(response);
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {


        try {
			
			String json=changeData(response);
            System.out.println("============"+getUrl());

			Response<T> success = Response.success(new GsonBuilder().create().fromJson(json, mClazz), HttpHeaderParser.parseCacheHeaders(response));
			if (success!=null) {
				Response.error(new NullResponseError());
			}
			return success;
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}catch (Exception e) {
			System.out.println(mClazz+"$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			return Response.error(new VolleyError(e));
		}
	}

	private String changeData(NetworkResponse response) throws UnsupportedEncodingException {


		String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers)).trim();
		if (getUrl().contains("getYouTubeRegionsFromDB") && json.length() > 2) {
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@_____SRP");
			json = json.substring(1, 1) + "[" + json.substring(1, json.length());
			json = json.substring(json.length()-2, json.length()-2) + "[" + json.substring(json.length()-2, json.length());

		} else if (getUrl().contains("getYouTubeCategoriesFromDB") && json.length() > 2) {
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@_____SRP");
            json = "{\"categories\":" + json + "}";
        }
	/*	if (getUrl().contains("/99api/" + Utils.version + "/searchProperty/") ||getUrl().contains("/99api/" + Utils.version + "/searchProjects/")) {
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@_____SRP");
			json=AndroidUtils.removefirstchar(json, '[');
			json=AndroidUtils.removelastchar(json, ']');
			
		}else*/ /*if (getUrl().contains("/99api/" + Utils.version + "/refinecluster/") &&  (getUrl().contains("clusterType=Possession")|| getUrl().contains("clusterType=Building"))) {
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@_____1");
			json="{\"Top_Results_Array\":"+json+"}";
		}*//*else if (getUrl().contains("/getnewprojectdetail/") && getUrl().contains("?rtype=json&tab=adv")) {
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@_____2");
			json="{\"Top_Results_Array\":"+json+"}";
		}*//*else if (getUrl().contains("/searchdata/")) {
			json="{\"data\":"+json+"}";
		}*
		else if(getUrl().contains("/societyprojectlist/")){
			json="{\"Top_Results_Array\":"+json+"}";
		}*//*else if (getUrl().contains("/contactus/")) {
			json="{\"contact\":"+json+"}";
		}*/

       /* else if(getUrl().contains("/societyprojectlist/")){
            json="{\"Top_Results_Array\":"+json+"}";
        }*/
		System.out.println(getUrl());
		System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::::::");
		System.out.println(json);
		return json;
		
	}
	@Override
	public void deliverError(VolleyError error) {
        System.out.println("=================%%%%%%  "+getUrl());
        super.deliverError(error);
		System.out.println("%%%%%%%%%%%%%%%%%%%%%% "+mClazz);
	}

}