package com.example.sentmessage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.os.Handler;
import android.provider.Settings;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    TextView text1;
    Button btn_sent,btn_info;
    EditText editText, writeapikey, fieldx;

    String res;
    Handler ktra;
    String bientam, writeapikey_tam, fieldx_tam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /******* khai báo tìm địa chỉ ánh xạ */
        find_id();
        /***********  nhấn để gửi dữ liệu xác nhận tắt mở đèn   ****************/
        nut_nhan_sent();
        /***********  nhấn để chọn wifi   ****************/
        nut_info();

        if(!isNetworkConnected())
        {
            xem_xet_fireBase();
        }
    }

    public void find_id()/** khai báo ãnh xạ*/
    {

        editText = (EditText) findViewById(R.id.text_mess);
        writeapikey = (EditText) findViewById(R.id.Write_APIKey);
        fieldx = (EditText) findViewById(R.id.Fieldx);
        btn_sent = (Button) findViewById(R.id.btn);
        btn_info = (Button) findViewById(R.id.btn_info);
        text1 = (TextView) findViewById(R.id.text1);

        ktra = new Handler();

    }

    public void nut_nhan_sent()
    {
        btn_sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text1.setText("Loading....");
                if(!isNetworkConnected())
                {
                    xem_xet_fireBase();
                }
                else
                {
                    bientam = editText.getText().toString();
                    fieldx_tam = fieldx.getText().toString();
                    writeapikey_tam = writeapikey.getText().toString();
                    if (bientam.equals("")){
                        text1.setText("\nNhập IP sai giá trị!!! ");
                        Toast.makeText(MainActivity.this,"Nhập IP sai giá trị!!!",Toast.LENGTH_LONG).show();
                    }
                    else if(bientam.equals("Set IP nha!!!")){
                        text1.setText("\nNhập IP sai giá trị!!! ");
                        Toast.makeText(MainActivity.this,"Nhập IP sai giá trị!!!",Toast.LENGTH_LONG).show();
                    }
                    else {
                        res = null;
                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    postData();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        t.start();
                    }
                }
            }
        });
    }

    public void nut_info()
    {
        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Nguyen Duy Bang");
                builder.setMessage("Mail: duybang140494@gmail.com");
                builder.setIcon(R.drawable.icon_wifi);
                builder.setCancelable(false);

                builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    public void postData() throws  IOException
    {
        HttpRequest mReq = new HttpRequest();
        Log.d("checkaaaaaaaaaaa", bientam);
        res = mReq.sendGet("https://api.thingspeak.com/update.json?api_key=" + writeapikey_tam +"&field" + fieldx_tam+ "=" + "con%chó%con" + "&status=" + bientam);
        Log.d("checkaaaaaaaaaaa", "11111111111");
        if (res != null) {
            ktra.post(new Runnable() {
                @Override
                public void run() {
                    if (res.equals("HTTP/1.1 200 OK")) {
                        text1.setText(res);
                        Toast.makeText(MainActivity.this, "Kết nối được!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        text1.setText(res);
                        Toast.makeText(MainActivity.this, "Không kết nối được!\nKiểm tra lại mạng!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    protected void onResume() {
        if(!isNetworkConnected())
        {
            xem_xet_fireBase();
        }
        super.onResume();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        return cm.getActiveNetworkInfo() != null;
    }

    public void xem_xet_fireBase()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("KHÔNG CÓ KẾT NỐI MẠNG");
        builder.setMessage("YÊU CẦU KẾT NỐI INTERNET");
        builder.setIcon(R.drawable.icon_wifi);
        builder.setCancelable(false);

        builder.setPositiveButton("Kết nối Internet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
