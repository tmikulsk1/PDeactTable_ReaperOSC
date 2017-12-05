package com.illposed.osc;

/**
 * Created by tmikulsk1 on 25/11/2017.
 */

import android.widget.Toast;

import com.example.tmikulsk1.pdeacttable_osc.MainActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SimpleOSCListener implements OSCListener {

        private boolean messageReceived = false;
        private Date receivedTimestamp = null;
        private List<Object> getMessage;

        public Date getReceivedTimestamp() {
            return receivedTimestamp;
        }

        public boolean isMessageReceived() {
            return messageReceived;
        }

        public List<Object> getReceivedMessage() {return getMessage;}

        @Override
        public void acceptMessage(Date time, OSCMessage message) {
            messageReceived = true;
            receivedTimestamp = time;
            getMessage = new ArrayList<>();
            getMessage = message.getArguments();

        }
    }

