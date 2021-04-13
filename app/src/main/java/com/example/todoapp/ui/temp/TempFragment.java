package com.example.todoapp.ui.temp;

import android.content.Context;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.todoapp.R;

public class TempFragment extends Fragment implements SensorEventListener {

    private TempViewModel tempViewModel;
    private SensorManager mSensorManager;
    private Context context;
    private Sensor tmpSensor;
    private TextView textView;
    private ImageView mImageView;
    private Boolean isTemperatureSensorAvailable;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tempViewModel =
                new ViewModelProvider(this).get(TempViewModel.class);
        View root = inflater.inflate(R.layout.fragment_temp, container, false);
        mImageView= root.findViewById(R.id.set_imageview);
        textView = root.findViewById(R.id.text_temperature);

        context = root.getContext();
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        checkTemperatureAvailable(mSensorManager);


        return root;
    }

    private void checkTemperatureAvailable(SensorManager mSensorManager) {
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null){
            tmpSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            isTemperatureSensorAvailable = true;
        }else{
            styletextConvert("Temperature Sensor is not Available ");

            textView.setText("Temperature Sensor is not Available ");
            isTemperatureSensorAvailable = false;
        }

    }

    private void styletextConvert(String message) {
        SpannableString ss = new SpannableString(message);
        StyleSpan boldItalic = new StyleSpan(Typeface.BOLD_ITALIC);
        ss.setSpan(boldItalic, 0,message.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float value = event.values[0];
        if(value <= -1)
            mImageView.setImageResource(R.drawable.extremement_glacial);
        else if(value > -1 && value <= 6)
            mImageView.setImageResource(R.drawable.glacial_pluvieux);
        else if(value > 6 && value <= 11)
            mImageView.setImageResource(R.drawable.nuagueux_pluvieux);
        else if(value > 11 && value <= 20)
            mImageView.setImageResource(R.drawable.nuagueux);
        else if(value > 20 && value <= 27)
            mImageView.setImageResource(R.drawable.ensoilelle_nuagueux);
        else if(value > 27)
            mImageView.setImageResource(R.drawable.extremement_ensoilelle);


        textView.setText(event.values[0]+"°C");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        }

    @Override
    public void onResume(){
        super.onResume();
        mSensorManager.registerListener(this, tmpSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause(){
        super.onPause();
        if(isTemperatureSensorAvailable)
            mSensorManager.unregisterListener(this);
    }

}