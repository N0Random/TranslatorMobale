package egorko.api;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import YandexLinguisticsAPI.*;

public class MainActivity extends AppCompatActivity {
private Translate Tr;

    EditText outText;
    private void FillSpinner(int idSpin,String[] data,int idSelection)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) findViewById(idSpin);
        spinner.setAdapter(adapter);
        spinner.setSelection(idSelection);
    }
    private  Map<String,String> CreateMap(String [] data, ArrayList<String> outData)
    {
        Arrays.sort(data);
        Map<String,String> returnMap= new  HashMap<String,String>();
        for(String _iter:data)
        {
            String[] Buf=_iter.split(":");
            returnMap.put(Buf[0],Buf[1]);
            outData.add(Buf[0].trim());
        }
        return returnMap ;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Resources res = getResources();
        String [] data=res.getStringArray(R.array.Lang);
        ArrayList<String> NameLang= new ArrayList<String>();
        Tr=new Translate(CreateMap(data,NameLang),getApplicationContext());
// заполнение combobox\spinner
       FillSpinner(R.id.inputLang,NameLang.toArray(new String[NameLang.size()]),0);
        FillSpinner(R.id.outputLang,NameLang.toArray(new String[NameLang.size()]),1);
         outText=  (EditText) findViewById(R.id.outputText);

      // V.setText( Tr.TranslateText("русский","английский","Привет Мир"));




    }
    public void refresh(View v)
    {
        int buf=((Spinner)findViewById(R.id.inputLang)).getSelectedItemPosition();
        ((Spinner)findViewById(R.id.inputLang)).setSelection(((Spinner)findViewById(R.id.outputLang)).getSelectedItemPosition());
        ((Spinner)findViewById(R.id.outputLang)).setSelection(buf);
    }
    public void onclick(View v) {
        if(Tr.isOnline())
        {
        MyAsync process=new MyAsync();
        process.execute(Tr);
        }
        else
            outText.setText(getString(R.string.errNAN));
        // Пытался сделать через потоки - не получилось
       /* Thread t = new Thread(new Runnable() {
            public void run() {
                String _inLang=((Spinner)findViewById(R.id.inputLang)).getSelectedItem().toString();
                String _oLang=((Spinner)findViewById(R.id.outputLang)).getSelectedItem().toString();
                String _text=((EditText)findViewById(R.id.inputText)).getText().toString();
                Tr.TranslateText(_inLang,_oLang,_text);



            }
        });
        t.start();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        outText.append(Tr.getRetStr());
        */
    }


    //Класс  для вызова асинхронного вызова функции TranslateText

    public class MyAsync extends AsyncTask<Translate,Void, String> {
        private  String iStr;
        private  String oStr;
        private  String text;
        @Override
        protected void  onPreExecute(){
            iStr=((Spinner) findViewById(R.id.inputLang)).getSelectedItem().toString();
            oStr=((Spinner) findViewById(R.id.outputLang)).getSelectedItem().toString();
            text=((EditText) findViewById(R.id.inputText)).getText().toString();
        }

        @Override
        protected String doInBackground(Translate... trans) {
            return trans[0].TranslateText(iStr,oStr,text);
        }

        @Override
        protected void onPostExecute(String result) {
            outText.setText(result);
        }
    }



}
