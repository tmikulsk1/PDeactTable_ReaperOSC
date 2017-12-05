package com.example.tmikulsk1.pdeacttable_osc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OutConfig extends AppCompatActivity {

    private EditText outPort;
    private EditText hostIp;
    private Button setConfigs;
    public static boolean secondConfirm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_config);

        CharSequence text_port = Integer.toString(MainActivity.OUTPORT);
        CharSequence text_ip = MainActivity.DEVICEIP.toString();

        outPort = findViewById(R.id.out_port);
        outPort.setText(text_port);
        hostIp = findViewById(R.id.host_ip);
        hostIp.setText(text_ip);
        setConfigs = findViewById(R.id.set_configs_out);
        if (secondConfirm){
            outPort.setEnabled(false);
            hostIp.setEnabled(false);
            setConfigs.setEnabled(false);
        }
        setConfigs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (outPort.getText() != null){

                    MainActivity.OUTPORT = Integer.parseInt(outPort.getText().toString());
                    MainActivity.DEVICEIP = hostIp.getText().toString();

                    secondConfirm = true;

                    OutConfig.this.finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Entrada inválida", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
