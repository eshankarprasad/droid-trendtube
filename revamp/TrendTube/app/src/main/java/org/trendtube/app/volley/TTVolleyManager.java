package org.trendtube.app.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RequestQueue.RequestFilter;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.trendtube.app.utils.LruBitmapCache;

/**
 * Created by shankar on 9/12/15.
 * All Http Request must be pass through. This class is manager class to handle
 * all http request of NNacres
 */
public class TTVolleyManager {

	private static RequestQueue mRequestQueue;
	private static ImageLoader mImageLoader;

	private TTVolleyManager(Context context) {
		init(context);
	}

	private static void init(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);

		//int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		// Use 1/8th of the available memory for this memory cache.
		//int cacheSize = 1024 * 1024 * memClass / 8;
		mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache());
	}

	private static RequestQueue getRequestQueue(Context context) {
		if (mRequestQueue != null) {
			return mRequestQueue;
		} else {
			// throw new IllegalStateException("RequestQueue not initialized");
			init(context);

			return mRequestQueue;
		}
	}

	public static ImageLoader getImageLoader(Context context) {
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(getRequestQueue(context), new LruBitmapCache());

		}
		return mImageLoader;
	}

	// public static void displayImage(Context context,String uri, ImageView
	// imageView) {
	// displayImage(uri, imageView, options, listener, null);
	// String requestUrl;
	// ImageLoader mImageLoader2 = mImageLoader;
	// ImageContainer container=mImageLoader2.get(requestUrl, new
	// ImageListener() {
	//
	// @Override
	// public void onErrorListing(VolleyError error) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onResponse(ImageContainer response, boolean isImmediate) {
	// // TODO Auto-generated method stub
	//
	// }
	// }, maxWidth, maxHeight);
	// mImageLoader.
	// mImageLoader2.
	// }
	//

	/**
	 * @param nacresRequest
	 *            this method will add request to RequestQueue
	 *
	 *
	 */
	public static void addToQueue(TTRequest<?> nacresRequest, boolean clearCache) {
		if (clearCache) {
			getRequestQueue(nacresRequest.getContext()).getCache().clear();
		}
		getRequestQueue(nacresRequest.getContext()).add(nacresRequest);
	}

	/**
	 * Cancels all requests in this queue with the given taskId. taskId must be
	 * non-null and equality is by identity.
	 */
	public static void cancelAllByTaskId(Context context, final Object taskId) {

		//@Ashish Saini --patch to handle Class cast exception due to ImageRequest type of request

		try {
			if (taskId == null) {
				throw new IllegalArgumentException("Cannot cancelAll with a null taskId");
			}
			getRequestQueue(context).cancelAll(new RequestFilter() {
				@Override
				public boolean apply(Request<?> request) {

					return taskId.equals(((TTRequest<?>) request).getTaskId());
				}
			});
		}
		catch (ClassCastException e){
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Cancels all requests in this queue with the given taskId. taskId must be
	 * non-null and equality is by identity.
	 */
	public static void cancelAllByTag(Context context, Object tag) {
		getRequestQueue(context).cancelAll(tag);

	}

	/**
	 * Cancels all requests in this queue with the given url-Prefix. Tag must be
	 * non-null and equality is by identity.
	 */
	public static void cancelAllByUrlPrefix(Context context, final String taskId) {
		if (taskId == null) {
			throw new IllegalArgumentException("Cannot cancelAll with a null url-Prefix");
		}
		getRequestQueue(context).cancelAll(new RequestFilter() {
			@Override
			public boolean apply(Request<?> request) {

				return ((TTRequest<?>) request).getUrl().startsWith(taskId);
			}
		});
	}

	/**
	 * Cancels all requests in this queue.
	 */
	public static void cancelAll(Context context) {
		getRequestQueue(context).cancelAll(new RequestFilter() {
			@Override
			public boolean apply(Request<?> request) {
				return true;
			}
		});
	}

	/**
	 * Cancels Request in this queue.
	 */
	public static void cancelRequest(Context context, final Request<?> nacresRequest) {
		getRequestQueue(context).cancelAll(new RequestFilter() {
			@Override
			public boolean apply(Request<?> request) {
				return request.equals(nacresRequest);
			}
		});
	}
}
