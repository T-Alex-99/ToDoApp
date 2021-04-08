package com.example.todoapp.ui.home;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.ui.MiniAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    //Shake-Event
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private Context c;

    //To-Do RecyclerView
    private RecyclerView rv;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private ArrayList<String> content;
    private MiniAdapter adapter;
    private EditText ed;
    private RecyclerView.LayoutManager layoutRV;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        c = root.getContext();


        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        return root;
    }
    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 10) {
                content.remove(ed.getText().toString());
                Toast.makeText(c.getApplicationContext(), "Shake event detected", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    @Override
    public void onResume() {
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }
    @Override
    public void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ed = view.findViewById(R.id.edit_text);
        rv = view.findViewById(R.id.rec_view_first);
        fab1 = view.findViewById(R.id.fab);
        fab2 = view.findViewById(R.id.fabDelete);
        rv = (RecyclerView)view.findViewById(R.id.rec_view_first);
        layoutRV = new LinearLayoutManager(this.getActivity());
        rv.setLayoutManager(layoutRV);

        //Model erzeugen
        content = new ArrayList<String>();
        content.add("Hausaufgaben machen");
        content.add("Mathe lernen");
        adapter = new MiniAdapter(content);

        //Model mit View verknüpfen
        rv.setAdapter(adapter);


        // fab fuegt Editier-Inhalt der Liste hinzu
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content.add(ed.getText().toString());
                adapter.notifyDataSetChanged(); // Model benachrichtighen
            }
        });

        //fab delete
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content.remove(adapter.getSelectedItem());
                adapter.notifyDataSetChanged(); // Model benachrichtighen
            }
        });
    }
}