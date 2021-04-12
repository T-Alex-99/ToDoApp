package com.example.todoapp.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.todoapp.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

public class ProximityFragment extends Fragment implements SensorEventListener {

    private ProximityViewModel proximityViewModel;
    private TextView textView;
    private SensorManager sensorManager;
    private Sensor proximity;
    private static final int SENSOR_SENSITIVITY = 4;
    private Context c;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        proximityViewModel =
                new ViewModelProvider(this).get(ProximityViewModel.class);
        View root = inflater.inflate(R.layout.fragment_proximity, container, false);


        c = root.getContext();
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        return root;
    }


    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY)
        {
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                //near
                Toast.makeText(c.getApplicationContext(), "near", Toast.LENGTH_SHORT).show();
            } else {
                //far
                Toast.makeText(c.getApplicationContext(), "far", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.text_slideshow);
        YouTubePlayerView youTubePlayerView = view.findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
    }

}