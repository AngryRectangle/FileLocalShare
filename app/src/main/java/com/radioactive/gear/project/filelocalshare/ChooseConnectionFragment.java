package com.radioactive.gear.project.filelocalshare;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.radioactive.gear.project.core.Debug;

import java.io.IOException;
import java.net.DatagramPacket;

public class ChooseConnectionFragment extends Fragment {
    private LayoutInflater inflater;
    private View fragment;

    @Override
    public View onCreateView(
            final LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        fragment = inflater.inflate(
                R.layout.fragment_choose_connection,
                container,
                false
        );

        this.inflater = inflater;
        return fragment;
    }

    public void addButton(final DatagramPacket packet) {
        View chooseButton;
        chooseButton = inflater.inflate(R.layout.server_connection_button, null, false);
        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ((TextView) chooseButton.findViewById(R.id.pcName)).setText(new String(packet.getData()));
        ((LinearLayout) fragment.findViewById(R.id.connectionList)).addView(chooseButton);
    }
}
