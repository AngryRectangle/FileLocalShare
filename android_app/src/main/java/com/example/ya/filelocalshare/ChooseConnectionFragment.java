package com.example.ya.filelocalshare;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.DatagramPacket;

public class ChooseConnectionFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private DatagramPacket[] packets;

    public ChooseConnectionFragment() {
        // Required empty public constructor
    }

    public static ChooseConnectionFragment newInstance(DatagramPacket[] packets) {
        ChooseConnectionFragment fragment = new ChooseConnectionFragment();
        fragment.packets = packets;
        return fragment;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View fragment = inflater.inflate(
                R.layout.fragment_choose_connection,
                container,
                false
        );
        View chooseButton;
        for(int i =0; i<packets.length; i++) {
            chooseButton = inflater.inflate(R.layout.server_connection_button, null, false);
            chooseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            ((TextView)chooseButton.findViewById(R.id.pcName)).setText(new String(packets[i].getData()));
            ((LinearLayout)fragment.findViewById(R.id.connectionList)).addView(chooseButton);
        }
        return fragment;
    }

}
