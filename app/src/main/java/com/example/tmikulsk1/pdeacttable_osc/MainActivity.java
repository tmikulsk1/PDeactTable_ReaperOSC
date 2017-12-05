package com.example.tmikulsk1.pdeacttable_osc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.illposed.osc.*;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //CONFIGS
    public static int INPORT = 50100;
    public static int OUTPORT = 60500;
    public static String DEVICEIP = "192.168.1.191";
    public static boolean HASNEWCONFIG = false;
    //VERIFIERS SEND/RECEIVE
    public static int currentTrack = 1;
    //
    public static float volumeFader;
    public static float panFader;
    public static boolean isPlay;
    public static boolean isStop;
    public static boolean isPause;
    //
    public static boolean isLoop;
    //
    public static boolean isMute = false;
    public static boolean isSolo = false;
    public boolean ISCONFIGURED = false;
    //OSC OBJECTS
    private List<Object> messageArguments;
    private String messageAddress;
    private OSCPortOut oscPortOut;
    private OSCPortIn oscPortIn;
    private OSCListener oscListener;
    //STOP THREAD
    private boolean ThreadStop = true;
    //FIELDS AND BUTTONS
    private ImageButton nextTrack;
    private ImageButton previousTrack;
    //
    private ImageButton playButton;
    private ImageButton stopButton;
    private ImageButton pauseBuotton;
    private ImageButton loopButton;
    private ImageButton muteButton;
    private ImageButton soloButton;
    private SeekBar pan;
    private SeekBar volume;
    private TextView track;
    //
    //OSC MESSAGE
    private String message = "/device/track/follows/mixer";
    private ArrayList<Object> val = new ArrayList<>();
    private boolean newMessage = true;
    private boolean newInfo = true;
    //


    /**
     * OSC NEEDS TO BE EXECUTED WITHIN A THREAD
     * THIS THREAD HAVE THE IN, OUT AND LISTENER METHODS
     * AND UPDATE THE INFOS TO THE USER INTERFACE
     */
    public Thread oscmessage = new Thread() {

        /**
         * VERIFIES IF THE ARGUMENT RECEIVED BY OSC IS A NUMBER
         * @param arg osc message argument
         * @return true if is number or false if isn't
         */
        public boolean isNumber(String arg){

            try {
                BigDecimal n = new BigDecimal(arg);
                return true;
            } catch (Exception e){
                return false;
            }

        }

        /**
         * FUNCTION TO OPEN ALL THE CONNECTIONS
         * SET THE IN AND OUT PARAMETERS
         * ONLY OPENED ONCE, CANNOT BE CALLED TWICE
         * ONLY EXECUTES AFTER THE USER PORT AND IP PARAMETERS ARE SET
         */
        public void openConnection(){
            try {
                oscPortIn = null;
                oscPortIn = new OSCPortIn(INPORT);
                oscPortOut = null;
                oscPortOut = new OSCPortOut(InetAddress.getByName(DEVICEIP), OUTPORT);
                oscListener = null;
                oscListener = new OSCListener() {
                    @Override
                    public void acceptMessage(Date time, OSCMessage message) {

                        messageAddress = message.getAddress();
                        messageArguments = message.getArguments();

                        if (isNumber(messageArguments.get(0).toString())){
                            ListenerPatterns.infoRetrieval(Float.parseFloat(messageArguments.get(0).toString()),message.getAddress());
                            newInfo = true;

                        }else{
                            ListenerPatterns.infoRetrieval(messageArguments.get(0).toString(), message.getAddress());
                        }

                    }
                };

            } catch (UnknownHostException e){
                Toast.makeText(getApplicationContext(), "Error1: " + e, Toast.LENGTH_LONG).show();
            } catch (Exception e){
                Toast.makeText(getApplicationContext(), "Error2: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }

        /**
         * EVERY TIME THAT IS A NEW MESSAGE COMING, THIS FUNCTION LISTEN
         * RECEIVES MANY MESSAGES AND AT THE END CLOSES THE LISTENING METHOD
         */
        public void oscIN(){
            try {
                oscPortIn.addListener("/*/", oscListener);
                oscPortIn.startListening();
                oscmessage.sleep(500);
                oscPortIn.stopListening();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error " + e.toString(), Toast.LENGTH_LONG).show();
            }
        }

        /**
         * EVERY TIME THAT THE USER SEND A NEW MESSAGE THIS FUCTION IS EXECUTED
         * ONLY SENDS ONE MESSAGE AT TIME
         */
        public void oscOUT(){

            OSCMessage cmdLineOSC = new OSCMessage(message, val);

            try {
                oscPortOut.send(cmdLineOSC);
                // oscmessage.sleep(10);
                newMessage = false;

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "FALHA " + e.toString(), Toast.LENGTH_LONG).show();
            }
        }

        /**
         * UPDATE THE VERIFIERS AND SET CURRENT PROGRESS TO SEEKBARS
         * GRABS THE ARGUMENTS FROM OSC MESSAGE (THROWED TO A 'GLOBAL' VAR) AND SETS IT
         */
        public void updateInfos(){
            try{
                while (newInfo) {
                    track.setText(String.valueOf(currentTrack));
                    volume.setProgress(getScaleInv(10, volumeFader));
                    pan.setProgress(getScaleInv(10, panFader));
                    if (isPlay) {
                        playButton.setPressed(true);
                        stopButton.setPressed(false);
                        //pauseBuotton.setPressed(false);
                    } else {
                        playButton.setPressed(false);
                    }
                    if (isPause) {
                        pauseBuotton.setPressed(true);
                        playButton.setPressed(true);
                        stopButton.setPressed(false);
                    }else {
                        pauseBuotton.setPressed(false);
                    }
                    if (isStop){
                        stopButton.setPressed(true);
                        //playButton.setPressed(false);
                        //pauseBuotton.setPressed(false);
                    }else {
                        stopButton.setPressed(false);
                    }
                    if (isLoop){
                        loopButton.setPressed(true);
                    }else {
                        loopButton.setPressed(false);
                    }
                    if (isMute){
                        muteButton.setPressed(true);
                    }else {
                        muteButton.setPressed(false);
                    }
                    if (isSolo){
                        soloButton.setPressed(true);
                    }else {
                        soloButton.setPressed(false);
                    }
                    newInfo = false;
                }
            } catch (Exception e){

            }

        /*
        MAIN FUNCTION - RUNS WHEN THE THREAD IS STARTED
         */
        }
        @Override
        public void run() {

            while(ThreadStop) {

                if (HASNEWCONFIG){
                    try {
                        openConnection();
                        HASNEWCONFIG = false;
                    }catch (Exception e){}
                }

                oscIN();

                if (newMessage){

                    try {
                        oscOUT();
                    } catch (Exception e){}
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            updateInfos();
                        }catch (Exception e){}
                    }
                });

            }

        }
    };

    /**
     * CREATE MENU'S CONFIGURATION
     * ACCESS MENU.XML (IN RES/MENU FOLDER)
     * @param menu created menu
     * @return true when it's created
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.configs, menu);

        return true;
    }

    /**
     * WHEN AN OPTION IT'S SELECTED FROM MENU, OPEN THE CORRESPOND CONFIG ACTIVITY
     * @param item option that is inside menu.xml file (res/menu folder)
     * @return true ifs the desired item whas selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.in_configs_menu){
            startActivity(new Intent(MainActivity.this, InConfig.class));
            return true;
        }
        if (id == R.id.out_config_menu){
            startActivity(new Intent(MainActivity.this, OutConfig.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * NEEDS THIS METHOD TO CHECK IF THE USER ALREADY INPUTS THE PORT AND IP
     * IF THE USER DIDN'T, THE initializeOSC() WILL NOT START THE THREAD
     */
    @Override
    protected void onResume() {
        super.onResume();
        ThreadStop = true;

        if (!ISCONFIGURED) {
            initializeOSC();
        }

    }

    /**
     * DEFAULT TO STOP THE EVENTS INSIDE THE THREAD
     */
    @Override
    protected void onPause() {
        super.onPause();
        ThreadStop = false;
    }

    /**
     * MAIN - INITIALIZES EVERY THING
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        val.add(0);

        nextTrack = findViewById(R.id.next_track);
        track = findViewById(R.id.track_number);
        previousTrack = findViewById(R.id.previous_track);
        playButton = findViewById(R.id.play_button);
        stopButton = findViewById(R.id.stop_button);
        pauseBuotton = findViewById(R.id.pause_button);
        loopButton = findViewById(R.id.loop_button);
        muteButton = findViewById(R.id.mute_button);
        soloButton = findViewById(R.id.solo_button);
        pan = findViewById(R.id.pan_bar);
        volume = findViewById(R.id.volume_bar);

        volume.setMax(10);
        volume.incrementProgressBy(0);
        pan.setMax(10);
        pan.incrementProgressBy(1);

        initializeOSC();

         //METHOD TO SEND NEW OSC MESSAGES
        nextTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newMessage = true;
                val.clear();
                message = "/device/track/+";
            }
        });
        //METHOD TO SEND NEW OSC MESSAGES
        previousTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newMessage = true;
                val.clear();
                message = "/device/track/-";
            }
        });
        //METHOD TO SEND NEW OSC MESSAGES
        pan.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                newMessage = true;

                val.clear();
                message = "";

                val.add(getScale(1, 0, pan.getProgress(), 0, 10));
                //val.add(1);

                message = "/track/" +
                        Integer.toString(currentTrack) +
                        "/" +
                        "pan";
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //METHOD TO SEND NEW OSC MESSAGES
        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                newMessage = true;

                val.clear();
                message = "";

                val.add(getScale(1, 0, volume.getProgress(), 0, 10));
                //val.add(1);

                message = "/track/" +
                        Integer.toString(currentTrack) +
                        "/" +
                        "volume";
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //METHOD TO SEND NEW OSC MESSAGES
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newMessage = true;

                val.clear();
                message = "";

                val.add(1007);
                message = "/action";
            }
        });
        //METHOD TO SEND NEW OSC MESSAGES
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newMessage = true;

                val.clear();
                message = "";

                val.add(1016);
                message = "/action";
            }
        });
        //METHOD TO SEND NEW OSC MESSAGES
        pauseBuotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newMessage = true;

                val.clear();
                message = "";

                val.add(40073);
                message = "/action";
            }
        });
        //METHOD TO SEND NEW OSC MESSAGES
        loopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newMessage = true;

                val.clear();
                message = "";
                val.add(1068);
                message = "/action";
            }
        });
        //METHOD TO SEND NEW OSC MESSAGES
        muteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMute) {
                    newMessage = true;

                    val.clear();
                    message = "";
                    val.add((float)0);
                    message = "/track/" + currentTrack + "/mute";
                }
                else {
                    newMessage = true;

                    val.clear();
                    message = "";
                    val.add((float)1);
                    message = "/track/" + currentTrack + "/mute";
                }
            }
        });
        //METHOD TO SEND NEW OSC MESSAGES
        soloButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSolo){
                    newMessage = true;

                    val.clear();
                    message = "";
                    val.add((float)0);
                    message = "/track/" + currentTrack + "/solo";
                } else {
                    newMessage = true;

                    val.clear();
                    message = "";
                    val.add((float)1);
                    message = "/track/" + currentTrack + "/solo";
                }
            }
        });


    }

    /**
     * FUNCTION CREATED TO CONVERT THE SEEKBAR '.getProgress()' VALUES TO THE REAPER (DAW) PATTERN (0.0 TO 1.0)
     * @param maxAllowed max value - the old max value will be converted to this param input
     * @param minAllowed min value - the old min value will be converted to this param input
     * @param unscaledNum number that will be rescaled
     * @param min - old min value - the min value in the old scale
     * @param max - old max value - the max value in the old scale
     * @return
     */
    public float getScale(float maxAllowed, float minAllowed, int unscaledNum, float min, float max){

        float val = ((maxAllowed - minAllowed) * ((float)unscaledNum - min) / (max - min) + minAllowed);
        return val;

    }

    /**
     * FUCTION TCREATED TO GRAB OSC MESSAGES ARGUMENT (IN FLOAT) AND CONVERT TO INTEGER
     * @param maxAllowed - max value - the max number in'.maxProgress'
     * @param unscaledNum number that will be converted
     * @return
     */
    public int getScaleInv(int maxAllowed, float unscaledNum){

        float val = (unscaledNum * maxAllowed);
        return (int)val;

    }

    /**
     * IF THE USER HAS CONFIGURED THE PORTS AND IP, THE THREAD WILL BE STARTED.
     */
    public void initializeOSC(){
        if ((OutConfig.secondConfirm == true) && (InConfig.firstConfirm == true)){
            HASNEWCONFIG = true;
        }

        if (HASNEWCONFIG) {

            ISCONFIGURED = true;

            oscmessage.setDaemon(true);
            oscmessage.start();

        }else{
            if ((OutConfig.secondConfirm == false) && (InConfig.firstConfirm == true) ){
            Toast.makeText(getApplicationContext(), "" + "Configure a porta de saída e IP antes!", Toast.LENGTH_SHORT).show();
        } else if ((OutConfig.secondConfirm == true) && (InConfig.firstConfirm == false)){
                Toast.makeText(getApplicationContext(), "Configure a porta  de entrada antes!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Configure ambas as portas antes!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
