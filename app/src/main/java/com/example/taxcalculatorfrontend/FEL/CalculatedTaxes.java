package com.example.taxcalculatorfrontend.FEL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.taxcalculatorfrontend.BLL.Model.Taxes;
import com.example.taxcalculatorfrontend.DAL.TaxesAPI;
import com.example.taxcalculatorfrontend.R;


public class CalculatedTaxes extends Fragment {
    private TextView fragValBrut;
    private TextView fragValProv;
    private TextView fragValFed;
    private TextView fragValDue;
    private TextView fragValNet;
    View inf;
    public CalculatedTaxes() {
    }

    public static CalculatedTaxes newInstance(String param1, String param2) {
        CalculatedTaxes fragment = new CalculatedTaxes();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inf = inflater.inflate(R.layout.fragment_calculated_taxes, container, false);
        initView();
        Taxes taxes = new Taxes(0,"0;0;0;0");
        updateView(taxes);

        return inf;
    }

    private void initView() {
        fragValBrut = inf.findViewById(R.id.frag_val_brut);
        fragValProv = inf.findViewById(R.id.frag_val_prov);
        fragValFed = inf.findViewById(R.id.frag_val_fed);
        fragValDue = inf.findViewById(R.id.frag_val_due);
        fragValNet = inf.findViewById(R.id.frag_val_net);
    }

    public void updateView(Taxes taxes){
        Log.d("DEBUG","Updating Fragment with:" + taxes.toString());
        fragValBrut.setText(taxes.getGrossRevenue()+"$");
        fragValDue.setText(taxes.getAmountDue()+"$");
        fragValFed.setText(taxes.getFederalRate()+"$");
        fragValProv.setText(taxes.getProvincialRate()+"$");
        fragValNet.setText(taxes.getNetRevenue()+"$");
    }

}