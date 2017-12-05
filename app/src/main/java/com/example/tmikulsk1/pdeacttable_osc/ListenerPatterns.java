package com.example.tmikulsk1.pdeacttable_osc;

import android.widget.Switch;

import com.illposed.osc.utility.OSCPacketDispatcher;

/**
 * CLASS CREATED TO BEFORE THE LISTENER "acceptMessage"
 * COMMANDS THAT INPUT ONLYE THE DESIRED OSC MESSAGES
 * EVERY THING ELSE WILL BE IGNORED
 *
 * EXCLUSIVE TO THIS APP
 *
 * Created by tmikulsk1 on 26/11/2017.
 */

public class ListenerPatterns extends OSCPacketDispatcher {

    private static String s_trackSelected;
    private static String s_volume;
    private static String s_volume2;
    private static String s_volume3;
    private static String s_pan;
    private static String s_pan2;
    private static String s_delay;
    private static String s_reverb;
    private static String s_play;
    private static String s_stop;
    private static String s_pause;
    private static String s_loop;
    private static String s_mute;
    private static String s_mute2;
    private static String s_solo;
    private static String s_solo2;


    public static boolean isReaperPattern(String message) {

        String trackSelected = "/track/number/str";
        s_trackSelected = trackSelected;
        String volume = "/track/" + MainActivity.currentTrack + "/volume/db";
        s_volume = volume;
        String volume2 = "/track/" + MainActivity.currentTrack + "/volume";
        s_volume2 = volume2;
        String volume3 = "/track/volume";
        s_volume3 = volume3;
        String pan = "/track/" + MainActivity.currentTrack + "/pan";
        s_pan = pan;
        String pan2 = "/track/pan";
        s_pan2 = pan2;
        String delay = "";
        s_delay = delay;
        String reverb = "";
        s_reverb = reverb;
        String play = "/play";
        s_play = play;
        String stop = "/stop";
        s_stop = stop;
        String pause = "/pause";
        s_pause = pause;
        String loop = "/repeat";
        s_loop = loop;
        String mute = "/track/" + MainActivity.currentTrack + "/mute";
        s_mute = mute;
        String solo = "/track/" + MainActivity.currentTrack + "/solo";
        s_solo = solo;
        String mute2 = "/track/mute";
        s_mute2 = mute2;
        String solo2 = "/track/solo";
        s_solo2 = solo2;

        if (message.equals(volume) ||
                message.equals(volume2) ||
                message.equals(volume3) ||
                message.equals(trackSelected) ||
                message.equals(pan) ||
                message.equals(pan2)||
                message.equals(delay) ||
                message.equals(reverb) ||
                message.equals(play) ||
                message.equals(stop) ||
                message.equals(pause) ||
                message.equals(loop) ||
                message.equals(mute) ||
                message.equals(mute2) ||
                message.equals(solo) ||
                message.equals(solo2)){
            return true;
        } else {
            return false;
        }
    }

    public static void infoRetrieval(float argument, String address) {

        try {
            if (address.equals(s_volume2)) {
                MainActivity.volumeFader = argument;
            } else if (address.equals(s_volume3)) {
                MainActivity.volumeFader = argument;
            }
            if (address.equals(s_trackSelected)) {
                MainActivity.currentTrack = ((int) argument);
            }
            if (address.equals(s_pan)) {
                MainActivity.panFader = argument;
            } else if (address.equals(s_pan2)) {
                MainActivity.panFader = argument;
            }
            if (address.equals(s_play)) {
                if (argument == (float) 1) {
                    MainActivity.isPlay = true;
                } else {
                    MainActivity.isPlay = false;
                }
            }
            if (address.equals(s_stop)) {
                if (argument == (float) 1) {
                    MainActivity.isStop = true;
                } else {
                    MainActivity.isStop = false;
                }
            }
            if (address.equals(s_pause)) {
                if (argument == (float) 1) {
                    MainActivity.isPause = true;
                } else {
                    MainActivity.isPause = false;
                }
            }
            if (address.equals(s_loop)) {
                if (argument == (float) 1) {
                    MainActivity.isLoop = true;
                } else {
                    MainActivity.isLoop = false;
                }
            }
            if (address.equals(s_mute)) {
                if (argument == (float) 1) {
                    MainActivity.isMute = true;
                } else {
                    MainActivity.isMute = false;
                }
            }
            if (address.equals(s_mute2)) {
                if (argument == (float) 1) {
                    MainActivity.isMute = true;
                } else {
                    MainActivity.isMute = false;
                }
            }
            if (address.equals(s_solo)) {
                if (argument == (float) 1) {
                    MainActivity.isSolo = true;
                } else {
                    MainActivity.isSolo = false;
                }
            }
            if (address.equals(s_solo2)) {
                if (argument == (float) 1) {
                    MainActivity.isSolo = true;
                } else {
                    MainActivity.isSolo = false;
                }
            }
        }catch (Exception e){}

    }

    /**
     * CASE THE ARGUMENT ISN'T A NUMBER
     *
     * @param argument
     * @param address
     */
    public static void infoRetrieval(String argument, String address){

        if (address.equals(s_trackSelected)){
            MainActivity.currentTrack = Integer.parseInt(argument);
        }

    }
}