package com.example.taxcalculatorfrontend.FEL;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private Button mainBtnCalculate;
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
    }

    private void initView() {
        mainInputGross = (EditText) findViewById(R.id.main_input_gross);
        mainBtnCalculate = (Button) findViewById(R.id.main_btn_calculate);
    }

    public void startAPI(){
        String str = mainInputGross.getText().toString();
        try {
            this.amount = Double.parseDouble(str);
            Toast.makeText(getApplicationContext(), "Calling API...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,TaxesAPI.class);
            intent.putExtra("amount",amount);
            intent.putExtra("messenger",new Messenger(handler));
            startService(intent);
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Enter gross amount...", Toast.LENGTH_SHORT).show();
            Log.e("ERROR","Cannot parse string as double");
        }
    }
}

