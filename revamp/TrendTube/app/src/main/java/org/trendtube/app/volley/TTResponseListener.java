package org.trendtube.app.volley;

import com.android.volley.VolleyError;

/**
 * @author Shankar
 * 
 * @param <T> : Response Object
 */
public interface TTResponseListener<T> {

	/**
	 * @param request
	 *            :Request of resulting response
	 * @param response
	 *            :Final Response When correct response will occur
	 */
	void onResponse(TTRequest<T> request, T response);

	/**
	 * @param request
	 *            :Request of resulting response
	 * @param error
	 *            :VolleyError When an error will occur
	 * 
	 *            ::Type of Error 
	 *            >>TimeoutError 
	 *            >>ServerError 
	 *            >>ParseError 
	 *            >>NetworkError => NoConnectionError 
	 *            >>AuthFailureError
	 * 
	 */
	void onErrorResponse(TTRequest<T> request, VolleyError error);

}