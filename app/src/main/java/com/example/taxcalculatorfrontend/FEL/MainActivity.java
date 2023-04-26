package com.example.taxcalculatorfrontend.FEL;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.taxcalculatorfrontend.BLL.Model.Taxes;
import com.example.taxcalculatorfrontend.DAL.TaxesAPI;
import com.example.taxcalculatorfrontend.R;

public class MainActivity extends AppCompatActivity {

    FragmentManager manager;
    private EditText mainInputGross;
    private EditText mainInputIP;
    private Button mainBtnCalculate;
    private ImageView mainImgInfo;
    String apiString;
    Double amount;
    Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            Bundle reply = msg.getData();
            apiString = reply.getString("msg");
            Log.d("DEBUG","Received message:"+apiString);
            CalculatedTaxes calculatedTaxes = (CalculatedTaxes) manager.findFragmentById(R.id.main_frag_layout);
            calculatedTaxes.updateView(new Taxes(amount,apiString));
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_frag_layout, new CalculatedTaxes());
        transaction.commit();
        initView();

        mainBtnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAPI();
            }
        });

        mainImgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoAlert();
            }
        });
    }

    private void initView() {
        mainInputGross = (EditText) findViewById(R.id.main_input_gross);
        mainBtnCalculate = (Button) findViewById(R.id.main_btn_calculate);
        mainInputIP = (EditText) findViewById(R.id.main_input_ip);
        mainImgInfo = (ImageView)  findViewById(R.id.main_img_info);
    }
    public void startAPI(){
        String str = mainInputGross.getText().toString();
        try {
            this.amount = Double.parseDouble(str);
            Toast.makeText(getApplicationContext(), "Calling API...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,TaxesAPI.class);
            intent.putExtra("amount",amount);
            intent.putExtra("messenger",new Messenger(handler));
            intent.putExtra("ip",mainInputIP.getText().toString());
            startService(intent);
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Enter gross amount...", Toast.LENGTH_SHORT).show();
            Log.e("ERROR","Cannot parse string as double");
        }
    }

    public void infoAlert(){
        new AlertDialog.Builder(this)
                .setTitle("IP Configuration")
                .setMessage("Use this to configure the server IP.\n\n" +
                        "Default 10.0.2.2 works with an Android Virtual Machine in Android Studio.\n\n" +
                        "Hint: Get your server's ipv4 to run remotely from a server on the local network.")
                .setNegativeButton("Done",null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }
}

