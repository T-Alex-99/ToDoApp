package com.example.todoapp.ui.shakeToDo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.todoapp.R;
import com.example.todoapp.ui.MiniAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class ShakeFragment extends Fragment {

    private ShakeViewModel shakeViewModel;

    //Shake-Event
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private Context c;
    private TextView selected;
    private TextView placeholder;


    //To-Do RecyclerView
    private RecyclerView rv;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private ArrayList<String> content;
    private MiniAdapter adapter;
    private EditText ed;
    private RecyclerView.LayoutManager layoutRV;
    private Integer randomInt;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shakeViewModel =
                new ViewModelProvider(this).get(ShakeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_shake, container, false);

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
            if (mAccel > 13) {
                try {
                    Random random = new Random();
                    randomInt = random.nextInt(adapter.getItemCount()) ;
                    //selected.setBackgroundColor(Color.parseColor("#338205"));
                    Drawable green = getResources().getDrawable(R.drawable.text_selected_1,null);
                    selected.setBackground(green);
                    YoYo.with(Techniques.BounceInDown)
                            .duration(1200)
                            .repeat(0)
                            .playOn(selected);
                    selected.setText(content.get(randomInt));
                } catch (IllegalArgumentException e) {
                    Toast.makeText(c.getApplicationContext(), "Ohne To-Dos bringt dein shaken nichts!", Toast.LENGTH_LONG).show();
                    Drawable grey = getResources().getDrawable(R.drawable.text_selected_0,null);
                    selected.setBackground(grey);
                    selected.setText("");
                }
                //Toast.makeText(c.getApplicationContext(), "Shake event detected" + randomInt, Toast.LENGTH_SHORT).show();
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


        placeholder = view.findViewById(R.id.text_rv_placeholder);
        selected = view.findViewById(R.id.selected);

        //Model erzeugen
        content = new ArrayList<String>();
        content.add("Hausaufgaben machen");
        content.add("Mathe lernen");
        adapter = new MiniAdapter(content);

        //Model mit View verknüpfen
        rv.setAdapter(adapter);

        shakeViewModel.checkList(placeholder,content);

        // fab fügt Editier-Inhalt der Liste hinzu
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content.add(ed.getText().toString());
                adapter.notifyDataSetChanged(); // Model benachrichtigen
                //Überprüft ob Einträge vorhanden sind
                shakeViewModel.checkList(placeholder,content);

                Random random = new Random();
                FirebaseMessaging fm = FirebaseMessaging.getInstance();
                String projectId = "159698303810";
                Log.d("TAG", "Try to send a Message at Server: "+projectId);
                fm.send(new RemoteMessage.Builder( projectId + "@gcm.googleapis.com")
                        .setMessageId(""+random.nextInt())
                        .addData("Hallo", "Das ist eine generierte Message von mir")
                        .addData("action", "ECHO")
                        .build());

            }
        });

        //fab löscht item
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content.remove(adapter.getSelectedItem());
                adapter.notifyDataSetChanged(); // Model benachrichtigen
                //Überprüft ob Einträge vorhanden sind
                shakeViewModel.checkList(placeholder,content);
            }
        });
    }
}