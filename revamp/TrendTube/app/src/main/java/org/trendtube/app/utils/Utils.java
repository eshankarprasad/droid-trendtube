package org.trendtube.app.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.LocalDate;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Years;
import org.trendtube.app.R;
import org.trendtube.app.constants.Constants;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by shankar on 10/12/15.
 */
public class Utils {

    public static String calculateViewCount(String price) {

        String postfix = "";
        String prefix = "";
        if (price.length() == 1 || price.length() == 2 || price.length() == 3) {
            postfix = "";
            prefix = price;
        } else if (price.length() == 4) {
            postfix = "K";
            prefix = price.substring(0, 1);
        } else if (price.length() == 5) {
            postfix = "K";
            prefix = price.substring(0, 2);
        } else if (price.length() == 6) {
            postfix = "L";
            prefix = price.substring(0, 1);
        } else if (price.length() == 7) {
            postfix = "M";
            prefix = price.substring(0, 1);
        } else if (price.length() == 8) {
            postfix = "M";
            prefix = price.substring(0, 2);
        } else if (price.length() == 9) {
            postfix = "M";
            prefix = price.substring(0, 3);
        } else if (price.length() == 10) {
            postfix = "B";
            prefix = price.substring(0, 1);
        } else if (price.length() == 11) {
            postfix = "B";
            prefix = price.substring(0, 2);
        } else if (price.length() == 12) {
            postfix = "B";
            prefix = price.substring(0, 3);
        } else if (price.length() == 13) {
            postfix = "T";
            prefix = price.substring(0, 1);
        } else if (price.length() == 14) {
            postfix = "T";
            prefix = price.substring(0, 2);
        } else if (price.length() == 15) {
            postfix = "T";
            prefix = price.substring(0, 3);
        } else {
            postfix = "T+";
            prefix = price.substring(0, 3);
        }

        return prefix + postfix;
    }

    public static String calculateAge(long timestamp) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        try {
            date = output.parse(String.valueOf(new Timestamp(timestamp)));
            //MyLog.e("Date: " + output.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        cal.setTime(date);

        return calculateAge(cal);
    }

    public static String calculateAge(String publishedAt) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        try {
            date = sdf.parse(publishedAt);
            //MyLog.e("Date: " + output.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        cal.setTime(date);

        return calculateAge(cal);
    }

    public static String calculateAge(Calendar cal) {

        String age = "";
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);

        LocalDate birthdate = new LocalDate(year, month + 1, day);
        LocalDate now = new LocalDate();
        Years years = Years.yearsBetween(birthdate, now);
        Months months = Months.monthsBetween(birthdate, now);
        Days days = Days.daysBetween(birthdate, now);

        Hours hours = Hours.hoursBetween(birthdate, now);

        //MyLog.e("Hours: " + hours.getHours());
        Minutes minutes = Minutes.minutesBetween(birthdate, now);

        if (years.getYears() > 0) {
            if (years.getYears() == 1) {
                age = years.getYears() + " year ago";
            } else {
                age = years.getYears() + " years ago";
            }
        } else if (months.getMonths() > 0) {
            if (months.getMonths() == 1) {
                age = months.getMonths() + " month ago";
            } else {
                age = months.getMonths() + " months ago";
            }
        } else if (days.getDays() > 0) {
            if (days.getDays() == 1) {
                age = days.getDays() + " day ago";
            } else {
                age = days.getDays() + " days ago";
            }
        } else if (hours.getHours() > 0) {
            if (hours.getHours() == 1) {
                age = hours.getHours() + " hour ago";
            } else {
                age = hours.getHours() + " hours ago";
            }
        } else if (minutes.getMinutes() > 0) {
            if (minutes.getMinutes() == 1) {
                age = minutes.getMinutes() + " minute ago";
            } else {
                age = minutes.getMinutes() + " minutes ago";
            }
        }

        return age;
    }

    public static void displayImage(final Activity activity, final String url, final int placeHolder, final ImageView imageView) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Glide.with(activity).load(url).placeholder(placeHolder).into(imageView);
            }
        });
    }

    public static Dialog getBasicDialog(Context context, String dialogTitle) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_dialog_title_bar);
        dialog.setContentView(R.layout.custom_dialog);

        TextView dialogTitleTV = (TextView) dialog.findViewById(R.id.dialogTitle);
        final TextView dialogDone = (TextView) dialog.findViewById(R.id.done);
        dialogTitleTV.setText(dialogTitle);
        dialogDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        return dialog;
    }

    public static void setPreference(Context ctx, String Key, String Value) {
        SharedPreferences pref = ctx.getSharedPreferences(Constants.APP_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Key, Value);
        editor.apply();
    }

    public static void setPreference(Context ctx, String key, boolean value) {
        SharedPreferences pref = ctx.getSharedPreferences(Constants.APP_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void setPreference(Context ctx, String key, int value) {
        SharedPreferences pref = ctx.getSharedPreferences(Constants.APP_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void setPreference(Context ctx, String key, long value) {
        SharedPreferences pref = ctx.getSharedPreferences(Constants.APP_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static long getLongPreference(Context ctx, String Key) {

        SharedPreferences pref = ctx.getSharedPreferences(Constants.APP_ID, Context.MODE_PRIVATE);
        return pref.getLong(Key, 0l);
    }

    public static String getStringPreference(Context ctx, String Key) {
        SharedPreferences pref = ctx.getSharedPreferences(Constants.APP_ID, Context.MODE_PRIVATE);
        return pref.getString(Key, "");
    }

    public static void deletePreference(Context ctx, String Key) {
        SharedPreferences pref = ctx.getSharedPreferences(Constants.APP_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if (pref.contains(Key)) {
            editor.remove(Key);
            editor.commit();
        }
    }

    public static void handleError(Context context, VolleyError error) {
        if (error == null) {
            showErrorToast(context, Constants.APPLICATION_ERROR, Toast.LENGTH_SHORT);
            return;
        }
        if (isNetworkError(error)) {
            showErrorToast(context, Constants.CONNECTION_ERROR, Toast.LENGTH_SHORT);

        } else if (error instanceof ServerError) {

            showErrorToast(context, Constants.SERVER_ERROR, Toast.LENGTH_SHORT);

        } else {
            showErrorToast(context, Constants.APPLICATION_ERROR, Toast.LENGTH_SHORT);
        }
    }

    public static boolean isNetworkError(VolleyError error) {
        return (error instanceof TimeoutError || error instanceof NetworkError);
    }

    public static void showErrorToast(Context context, String text, int duration) {

        Toast toast = Toast.makeText(context, text, duration);
        View layout = LayoutInflater.from(context).inflate(R.layout.toast_error, null);
        DisplayMetrics dm = new DisplayMetrics();
        if (context instanceof Activity) {
            MyLog.e("instance of activity");
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        } else if (context instanceof FragmentActivity) {
            ((FragmentActivity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        }
        // RelativeLayout toastLayout = (RelativeLayout)
        // layout.findViewById(R.id.toastLayout);
        MyLog.e("Width: " + dm.widthPixels);
        ((TextView) layout.findViewById(R.id.toastMsg)).setWidth(dm.widthPixels - 50);
        /*
		 * ((TextView) layout.findViewById(R.id.toastMsg)) .setHeight((int)
		 * dm.heightPixels / 10);
		 */
        ((TextView) layout.findViewById(R.id.toastMsg)).setText(text);
        toast.setView(layout);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        return;
    }

    public static void showSnacK(View cordinatorLayout, String message, String actionLabel, int length, View.OnClickListener listener) {
        Snackbar.make(cordinatorLayout, message, length).setAction("Ok", listener).show();
    }
}
