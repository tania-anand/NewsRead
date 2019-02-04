package api.news.com.newsapi.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Objects;

/**
 * Created by taniaanand on 03/02/19.
 */

public class Utils {

    public static boolean checkInternetConnectivity(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = Objects.requireNonNull(cm).getActiveNetworkInfo();
        return netInfo!=null && netInfo.isConnected();
    }
}
