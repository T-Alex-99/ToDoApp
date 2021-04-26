package com.example.todoapp.ui.account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.todoapp.GoogleLogin;
import com.example.todoapp.MainActivity;
import com.example.todoapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Account extends Fragment {

    private TextView prename;
    private TextView name;
    private TextView email;
    private TextView accId;
    private ImageView photo;
    private Button login;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        login = root.findViewById(R.id.Login);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            Log.d("personGivenName", personGivenName);
            System.out.println(personGivenName);
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            //Quelle: https://stackoverflow.com/questions/2313148/imageview-setimageuri-does-not-work-when-trying-to-assign-a-r-drawable-x-uri
            //Uri imgUri=Uri.parse("android.resource://my.package.name/"+R.drawable.image);

            //imageView.setImageURI(imgUri);

            prename = root.findViewById(R.id.accPrename);
            name = root.findViewById(R.id.accName);
            email = root.findViewById(R.id.accEmail);
            accId = root.findViewById(R.id.accAccId);
            photo = root.findViewById(R.id.accPhoto);
            System.out.println(personPhoto);
            prename.append(personGivenName);
            name.append(personFamilyName);
            email.append(personEmail);
            accId.append(personId);
            //photo.setImageURI(Uri.parse(personPhoto));
            //photo.setImageBitmap(getImageBitmap(personPhoto));
            Picasso.with(this.getContext()).load(personPhoto).into(photo);
            login.setVisibility(View.GONE);
            //photo.setImageURI(Uri.parse(String.valueOf(acct.getPhotoUrl())));
        } else {
            login = root.findViewById(R.id.Login);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),GoogleLogin.class);
                    startActivity(intent);
                }
            });
            login.setVisibility(View.VISIBLE);
        }
        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prename = view.findViewById(R.id.accPrename);
        name = view.findViewById(R.id.accName);
        email = view.findViewById(R.id.accEmail);
        accId = view.findViewById(R.id.accAccId);
        photo = view.findViewById(R.id.accPhoto);



    }


}