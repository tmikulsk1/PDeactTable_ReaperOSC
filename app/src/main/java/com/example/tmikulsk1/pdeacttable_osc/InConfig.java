package com.example.tmikulsk1.pdeacttable_osc;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class InConfig extends AppCompatActivity {

    private EditText inPort;
    private TextView myIp;
    private Button setConfigs;
    public static boolean firstConfirm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_config);

        CharSequence text_port = Integer.toString(MainActivity.INPORT);

        inPort = findViewById(R.id.in_port);
        inPort.setText(text_port);
        myIp = findViewById(R.id.my_ip);
        setConfigs = findViewById(R.id.set_configs_in);

        myIp.setText(getIpAdress());
        if (firstConfirm){
            inPort.setEnabled(false);
            setConfigs.setEnabled(false);
        }
        setConfigs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inPort.getText() != null){

                    MainActivity.INPORT = Integer.parseInt(inPort.getText().toString());

                    firstConfirm = true;
                    InConfig.this.finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Entrada inválida", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public String getIpAdress(){

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int ip = wifiManager.getDhcpInfo().ipAddress;

        String ipString = String.format(Locale.getDefault(),
                "%d.%d.%d.%d",
                (ip & 0xff),
                (ip >> 8 & 0xff),
                (ip >> 16 & 0xff),
                (ip >> 24 & 0xff));

        return ipString;

    }
}
