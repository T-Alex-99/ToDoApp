package com.example.todoapp.ui.home;

import android.view.View;
import android.widget.TextView;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ShakeViewModel extends ViewModel {

    public ShakeViewModel() {
    }

    //Wenn keine To-Dos vorhanden sind, soll der Text sichtbar sein!
    public void checkList(TextView text, ArrayList<String> content) {
        if (content.size() == 0) {
            text.setVisibility(View.VISIBLE);
        }
        else {
            text.setVisibility(View.INVISIBLE);
        }

    }
}