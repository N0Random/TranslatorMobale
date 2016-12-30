
package YandexLinguisticsAPI;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import egorko.api.R;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Path;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Translate{



    String RetStr;
    Map<String,String> Lang;
    private Context mAppContext;
    public Translate( Map<String,String> _Lang,Context context)
    {

        mAppContext=context;
        RetStr=mAppContext.getString(R.string.emptyStr);
        Lang=_Lang;
    }
    private Map<String,String> CreatParam(String LangPair,String text){
    Map<String,String> returnMap=new HashMap<String,String>();
        returnMap.put(mAppContext.getString(R.string.SecondPar), text);
        returnMap.put( mAppContext.getString(R.string.FirstPar), mAppContext.getString(R.string.API_kay));
        returnMap.put(mAppContext.getString(R.string.ThirdPar),LangPair);
        return returnMap;
    };
public String TranslateText(String _In,String _Out,String text)
{

    StringBuilder Buld=new StringBuilder(Lang.get(_In)+"-"+Lang.get(_Out));
    String LangPair=Buld.toString();
    Retrofit restAdapter = new Retrofit.Builder()
                                .baseUrl(mAppContext.getString(R.string.Base_url))
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
    API Service=restAdapter.create(API.class);
    Call<Resp> call=Service.Translate(CreatParam(LangPair,text));
    ;
    try {
      Response<Resp>w= call.execute();
        Resp result=w.body();
        switch (result.getCode()) {
            case 200:
                RetStr = result.getText().toArray(new String[result.getText().size()])[0];
                break;
            case 401:
                RetStr = mAppContext.getString(R.string.err401);
                break;
            case 402:
                RetStr = mAppContext.getString(R.string.err402);
                break;
            case 404:
                RetStr = mAppContext.getString(R.string.err404);
                break;
            case 413:
                RetStr = mAppContext.getString(R.string.err413);
                break;
            case 422:
                RetStr = mAppContext.getString(R.string.err422);
                break;
            case 501:
                RetStr = mAppContext.getString(R.string.err501);
                break;
            default:
                RetStr = mAppContext.getString(R.string.errNAN);
                break;
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
return RetStr;
}

    public Map<String, String> getLang() {
        return Lang;
    }

    public void setLang(Map<String, String> lang) {
        Lang = lang;
    }
    public String getRetStr() {
        return RetStr;
    }

    public void setRetStr(String retStr) {
        RetStr = retStr;
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) mAppContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}

