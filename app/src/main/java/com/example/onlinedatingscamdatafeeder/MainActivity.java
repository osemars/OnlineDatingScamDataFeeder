package com.example.onlinedatingscamdatafeeder;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import org.datavec.api.transform.TransformProcess;
import org.datavec.api.transform.schema.Schema;
import org.tensorflow.lite.Interpreter;

import com.google.firebase.ml.common.FirebaseMLException;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public FirebaseFirestore db;
    @VisibleForTesting
    public ProgressDialog mProgressDialog;
    String pm1, pm2, pm3, pm4, pm5;
    String po1, po2, po3, po4;
    String cp1, cp4, cp2, cp3, cp5, cp6, cp7, cp8, cp9, cp10, cp11, cp12, cp13, cp14;
    String er1, er2, er3, er4;
    Spinner dropdown_chatting_platform;
    Spinner dropdown_person_occupation;
    Spinner dropdown_payment_method;
    Spinner dropdown_emotions_rating;
    Spinner dropdown_video_call_freq;
    Spinner dropdown_voice_call_freq;
    Switch switch_money_asked_already;
    Switch switch_unforeseen_circumstance;
    Switch switch_picker;
    Switch switch_business_proposal;
    String switch_answer_money_asked;
    String switch_answer_unforeseen;
    String switch_answer_picker;
    String switch_answer_business_proposal;
    String string_dropdown_chatting_platform;
    String string_dropdown_person_occupation;
    String string_dropdown_payment_method;
    String string_dropdown_emotions_rating;
    String string_dropdown_video_call_freq;
    String string_dropdown_voice_call_freq;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();


        switch_money_asked_already = findViewById(R.id.money_asked_already);
        switch_money_asked_already.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switch_money_asked_already.isChecked();
                }
            }
        });

        switch_unforeseen_circumstance = findViewById(R.id.unforeseen_circumstance);
        switch_unforeseen_circumstance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switch_unforeseen_circumstance.isChecked();
                }
            }
        });

        switch_picker = findViewById(R.id.picker);
        switch_picker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switch_picker.isChecked();
                }
            }
        });

        switch_business_proposal = findViewById(R.id.business_proposal);
        switch_business_proposal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switch_business_proposal.isChecked();
                }
            }
        });

        //get the spinner from the xml.
        dropdown_person_occupation = findViewById(R.id.person_occupation);
        dropdown_chatting_platform = findViewById(R.id.chatting_platform);
        dropdown_voice_call_freq = findViewById(R.id.voice_call_freq);
        dropdown_video_call_freq = findViewById(R.id.video_call_freq);
        dropdown_payment_method = findViewById(R.id.payment_method);
        dropdown_emotions_rating = findViewById(R.id.emotions_rating);

        String[] chat_items = new String[]{"Where do you two chat mostly on?", "Dating site/app", "Facebook", "WhatsApp", "Telegram", "Instagram", "LinkedIn", "Email", "Viber", "Snapchat", "WeChat", "Skype", "Line", "Signal", "Others"};
        ArrayAdapter<String> chat_adapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, chat_items);
        dropdown_chatting_platform.setAdapter(chat_adapt);

//        String[] person_occu_items = new String[]{"What does he/she do for a living?", "Business Owner", "Army", "Missionary", "Medical Personnel", "Teacher", "Engineer", "Sailor", "Pilot", "Government Employee", "Unemployed", "Others"};
//        ArrayAdapter<String> adapt_occ = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, person_occu_items);
//        dropdown_person_occupation.setAdapter(adapt_occ);

        String[] person_occu_items = new String[]{"How close do you live from this person?", "same country/same city", "same country/different cities", "currently working abroad", "different countries"};
        ArrayAdapter<String> adapt_occ = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, person_occu_items);
        dropdown_person_occupation.setAdapter(adapt_occ);

        String[] voice_call_items = new String[]{"How ofter do you call each other on voice?", "We haven't spoken at all on call", "We have spoken less than 3 times since we have known", "At least once a week", "Almost everyday"};
        ArrayAdapter<String> adapt_voice = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, voice_call_items);
        dropdown_voice_call_freq.setAdapter(adapt_voice);

        String[] video_call_items = new String[]{"How often do you two video chat?", "We have never done video call", "We have video called less than 3 times since we have known", "At least once a week", "Almost everyday"};
        ArrayAdapter<String> adapt_video = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, video_call_items);
        dropdown_video_call_freq.setAdapter(adapt_video);

//        String[] payment_items = new String[]{"What methods have you used in sending money to this person?", "None", "PayPal", "WesternUnion", "Gift Card", "Crypto Currency", "MoneyPak", "Vanilla Reload", "Reloadit Card", "Others"};
//        ArrayAdapter<String> adapt_paymnet = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, payment_items);
//        dropdown_payment_method.setAdapter(adapt_paymnet);

        String[] payment_items = new String[]{"What methods have you used in sending money to this person?","none" ,"type of payment linked to his/her bank account", "type of payment linked to the bank account of a his/her colleague", "type of payment without a real identity e.g gift cards,crypto,western union", "payment in form of gifts"};
        ArrayAdapter<String> adapt_paymnet = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, payment_items);
        dropdown_payment_method.setAdapter(adapt_paymnet);

        String[] emotions_item = new String[]{"How would you best describe his/her temperament?", "very romantic", "very romantic and fun", "averagely romantic and fun", "casual"};
        ArrayAdapter<String> adapt_emotions = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, emotions_item);
        dropdown_emotions_rating.setAdapter(adapt_emotions);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void uploader(View view) throws FirebaseMLException, IOException {

        //getting the string value of the switches
        if (switch_money_asked_already.isChecked()) {
            switch_answer_money_asked = "1";
        } else {
            switch_answer_money_asked = "0";
        }

        if (switch_business_proposal.isChecked()) {
            switch_answer_business_proposal = "1";
        } else {
            switch_answer_business_proposal = "0";
        }

        if (switch_picker.isChecked()) {
            switch_answer_picker = "1";
        } else {
            switch_answer_picker = "0";
        }

        if (switch_unforeseen_circumstance.isChecked()) {
            switch_answer_unforeseen = "1";
        } else {
            switch_answer_unforeseen = "0";
        }

        //getting the string value of the spinners
        string_dropdown_chatting_platform = dropdown_chatting_platform.getSelectedItem().toString();
        string_dropdown_person_occupation = dropdown_person_occupation.getSelectedItem().toString();
        string_dropdown_emotions_rating = dropdown_emotions_rating.getSelectedItem().toString();
        string_dropdown_payment_method = dropdown_payment_method.getSelectedItem().toString();
        string_dropdown_video_call_freq = dropdown_video_call_freq.getSelectedItem().toString();
        if (string_dropdown_video_call_freq.equals("Almost everyday")) {
            string_dropdown_video_call_freq = "0";
        }
        if (string_dropdown_video_call_freq.equals("At least once a week")) {
            string_dropdown_video_call_freq = "3";
        }
        if (string_dropdown_video_call_freq.equals("We have video called less than 3 times since we have known")) {
            string_dropdown_video_call_freq = "5";
        }
        if (string_dropdown_video_call_freq.equals("We have never done video call")) {
            string_dropdown_video_call_freq = "7";
        }
        string_dropdown_voice_call_freq = dropdown_voice_call_freq.getSelectedItem().toString();
        if (string_dropdown_voice_call_freq.equals("Almost everyday")) {
            string_dropdown_voice_call_freq = "0";
        }
        if (string_dropdown_voice_call_freq.equals("At least once a week")) {
            string_dropdown_voice_call_freq = "1";
        }
        if (string_dropdown_voice_call_freq.equals("We have spoken less than 3 times since we have known")) {
            string_dropdown_voice_call_freq = "2";
        }
        if (string_dropdown_voice_call_freq.equals("We haven't spoken at all on call")) {
            string_dropdown_voice_call_freq = "3";
        }

        //TODO: if you leave the questions empty
        //if you leave the questions empty
//        if(state_string.equals("Choose State")){
//            Toast.makeText(MainActivity.this, "Please Select a Nigerian State", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        if(institute.equals("Type of Facility")){
//            Toast.makeText(MainActivity.this, "Please Select a Facility", Toast.LENGTH_LONG).show();
//            return;
//        }

        showProgressDialog();
        //uploading to firestore database
        Map<String, String> Usermap = new HashMap<>();
        Usermap.put("chat_platform", string_dropdown_chatting_platform);
        Usermap.put("person_occupation", string_dropdown_person_occupation);
        Usermap.put("emotional_rating", string_dropdown_emotions_rating);
        Usermap.put("payment_method", string_dropdown_payment_method);
        Usermap.put("video_freq", string_dropdown_video_call_freq);
        Usermap.put("voice_freq", string_dropdown_voice_call_freq);
        Usermap.put("business_proposal", switch_answer_business_proposal);
        Usermap.put("money_asked", switch_answer_money_asked);
        Usermap.put("picker", switch_answer_picker);
        Usermap.put("miscellaneous", switch_answer_unforeseen);

        db.collection("query_data").add(Usermap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                //After Upload the objects go back to the original state
                hideProgressDialog();
//                Toast.makeText(MainActivity.this, "Kindly wait...Detective A.I is prescribing...", Toast.LENGTH_LONG).show();
//                dropdown_chatting_platform.setSelection(0);
//                dropdown_emotions_rating.setSelection(0);
//                dropdown_payment_method.setSelection(0);
//                dropdown_person_occupation.setSelection(0);
//                dropdown_video_call_freq.setSelection(0);
//                dropdown_voice_call_freq.setSelection(0);
//                switch_business_proposal.setChecked(false);
//                switch_money_asked_already.setChecked(false);
//                switch_picker.setChecked(false);
//                switch_unforeseen_circumstance.setChecked(false);
            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String error = e.getMessage();
                hideProgressDialog();
                Toast.makeText(MainActivity.this, "Sorry an error occured, please try again" + error, Toast.LENGTH_LONG).show();
            }
        });

        //use usermap as the input into the model
        //configuring the input and output options

        if (string_dropdown_emotions_rating.equals("very romantic")) {
            er1 = String.valueOf(1);
            er2 = String.valueOf(2);
            er3 = String.valueOf(0);
            er4 = String.valueOf(0);
        }
        if (string_dropdown_emotions_rating.equals("very romantic and fun")) {
            er1 = String.valueOf(0);
            er2 = String.valueOf(1);
            er3 = String.valueOf(2);
            er4 = String.valueOf(0);
        }
        if (string_dropdown_emotions_rating.equals("averagely romantic and fun")) {
            er1 = String.valueOf(0);
            er2 = String.valueOf(0);
            er3 = String.valueOf(1);
            er4 = String.valueOf(2);
        }
        if (string_dropdown_emotions_rating.equals("casual")) {
            er1 = String.valueOf(0);
            er2 = String.valueOf(0);
            er3 = String.valueOf(2);
            er4 = String.valueOf(1);
        }


        //type of payment made
        if (string_dropdown_payment_method.equals("none")) {
            pm1 = String.valueOf(1);
            pm2 = String.valueOf(5);
            pm3 = String.valueOf(0);
            pm4 = String.valueOf(0);
            pm5 = String.valueOf(0);
        }
        if (string_dropdown_payment_method.equals("type of payment linked to his/her bank account")) {
            pm1 = String.valueOf(0);
            pm2 = String.valueOf(1);
            pm3 = String.valueOf(5);
            pm4 = String.valueOf(0);
            pm5 = String.valueOf(0);
        }
        if (string_dropdown_payment_method.equals("type of payment linked to the bank account of a his/her colleague")) {
            pm1 = String.valueOf(0);
            pm2 = String.valueOf(0);
            pm3 = String.valueOf(1);
            pm4 = String.valueOf(5);
            pm5 = String.valueOf(0);
        }
        if (string_dropdown_payment_method.equals("type of payment without a real identity e.g gift cards,crypto,western union")) {
            pm1 = String.valueOf(0);
            pm2 = String.valueOf(0);
            pm3 = String.valueOf(0);
            pm4 = String.valueOf(1);
            pm5 = String.valueOf(5);
        }
        if (string_dropdown_payment_method.equals("payment in form of gifts")) {
            pm1 = String.valueOf(0);
            pm2 = String.valueOf(0);
            pm3 = String.valueOf(0);
            pm4 = String.valueOf(5);
            pm5 = String.valueOf(1);
        }
//        if (string_dropdown_payment_method.equals("MoneyPak")) {
//            pm1 = String.valueOf(0);
//            pm4 = String.valueOf(0);
//            pm2 = String.valueOf(0);
//            pm3 = String.valueOf(0);
//            pm5 = String.valueOf(0);
//            pm6 = String.valueOf(1);
//            pm7 = String.valueOf(2);
//            pm8 = String.valueOf(0);
//            pm9 = String.valueOf(0);
//        }
//        if (string_dropdown_payment_method.equals("Vanilla Reload")) {
//            pm1 = String.valueOf(0);
//            pm4 = String.valueOf(0);
//            pm2 = String.valueOf(0);
//            pm3 = String.valueOf(0);
//            pm5 = String.valueOf(0);
//            pm6 = String.valueOf(0);
//            pm7 = String.valueOf(1);
//            pm8 = String.valueOf(2);
//            pm9 = String.valueOf(0);
//        }
//        if (string_dropdown_payment_method.equals("Reloadit Card")) {
//            pm1 = String.valueOf(0);
//            pm4 = String.valueOf(0);
//            pm2 = String.valueOf(0);
//            pm3 = String.valueOf(0);
//            pm5 = String.valueOf(0);
//            pm6 = String.valueOf(0);
//            pm7 = String.valueOf(0);
//            pm8 = String.valueOf(1);
//            pm9 = String.valueOf(2);
//        }
//        if (string_dropdown_payment_method.equals("Others")) {
//            pm1 = String.valueOf(0);
//            pm4 = String.valueOf(0);
//            pm2 = String.valueOf(0);
//            pm3 = String.valueOf(0);
//            pm5 = String.valueOf(0);
//            pm6 = String.valueOf(0);
//            pm7 = String.valueOf(0);
//            pm8 = String.valueOf(2);
//            pm9 = String.valueOf(1);
//        }

        //for distance apart
        if (string_dropdown_person_occupation.equals("same country/same city")) {
            po1 = String.valueOf(1);
            po2 = String.valueOf(3);
            po3 = String.valueOf(0);
            po4 = String.valueOf(0);
        }
        if (string_dropdown_person_occupation.equals("same country/different cities")) {
            po1 = String.valueOf(0);
            po2 = String.valueOf(1);
            po3 = String.valueOf(3);
            po4 = String.valueOf(0);
        }
        if (string_dropdown_person_occupation.equals("currently working abroad")) {
            po1 = String.valueOf(0);
            po2 = String.valueOf(0);
            po3 = String.valueOf(1);
            po4 = String.valueOf(3);
        }
        if (string_dropdown_person_occupation.equals("different countries")) {
            po1 = String.valueOf(0);
            po2 = String.valueOf(0);
            po3 = String.valueOf(3);
            po4 = String.valueOf(1);
        }
//        if (string_dropdown_person_occupation.equals("Teacher")) {
//            po1 = String.valueOf(0);
//            po4 = String.valueOf(0);
//            po2 = String.valueOf(0);
//            po3 = String.valueOf(0);
//            po5 = String.valueOf(1);
//            po6 = String.valueOf(3);
//            po7 = String.valueOf(0);
//            po8 = String.valueOf(0);
//            po9 = String.valueOf(0);
//            po10 = String.valueOf(0);
//            po11 = String.valueOf(0);
//        }
//        if (string_dropdown_person_occupation.equals("Engineer")) {
//            po1 = String.valueOf(0);
//            po4 = String.valueOf(0);
//            po2 = String.valueOf(0);
//            po3 = String.valueOf(0);
//            po5 = String.valueOf(0);
//            po6 = String.valueOf(1);
//            po7 = String.valueOf(3);
//            po8 = String.valueOf(0);
//            po9 = String.valueOf(0);
//            po10 = String.valueOf(0);
//            po11 = String.valueOf(0);
//        }
//        if (string_dropdown_person_occupation.equals("Sailor")) {
//            po1 = String.valueOf(0);
//            po4 = String.valueOf(0);
//            po2 = String.valueOf(0);
//            po3 = String.valueOf(0);
//            po5 = String.valueOf(0);
//            po6 = String.valueOf(0);
//            po7 = String.valueOf(1);
//            po8 = String.valueOf(3);
//            po9 = String.valueOf(0);
//            po10 = String.valueOf(0);
//            po11 = String.valueOf(0);
//        }
//        if (string_dropdown_person_occupation.equals("Pilot")) {
//            po1 = String.valueOf(0);
//            po4 = String.valueOf(0);
//            po2 = String.valueOf(0);
//            po3 = String.valueOf(0);
//            po5 = String.valueOf(0);
//            po6 = String.valueOf(0);
//            po7 = String.valueOf(0);
//            po8 = String.valueOf(1);
//            po9 = String.valueOf(3);
//            po10 = String.valueOf(0);
//            po11 = String.valueOf(0);
//        }
//        if (string_dropdown_person_occupation.equals("Government Employee")) {
//            po1 = String.valueOf(0);
//            po4 = String.valueOf(0);
//            po2 = String.valueOf(0);
//            po3 = String.valueOf(0);
//            po5 = String.valueOf(0);
//            po6 = String.valueOf(0);
//            po7 = String.valueOf(0);
//            po8 = String.valueOf(0);
//            po9 = String.valueOf(1);
//            po10 = String.valueOf(3);
//            po11 = String.valueOf(0);
//        }
//        if (string_dropdown_person_occupation.equals("Unemployed")) {
//            po1 = String.valueOf(0);
//            po4 = String.valueOf(0);
//            po2 = String.valueOf(0);
//            po3 = String.valueOf(0);
//            po5 = String.valueOf(0);
//            po6 = String.valueOf(0);
//            po7 = String.valueOf(0);
//            po8 = String.valueOf(0);
//            po9 = String.valueOf(0);
//            po10 = String.valueOf(1);
//            po11 = String.valueOf(3);
//        }
//        if (string_dropdown_person_occupation.equals("Others")) {
//            po1 = String.valueOf(0);
//            po4 = String.valueOf(0);
//            po2 = String.valueOf(0);
//            po3 = String.valueOf(0);
//            po5 = String.valueOf(0);
//            po6 = String.valueOf(0);
//            po7 = String.valueOf(0);
//            po8 = String.valueOf(0);
//            po9 = String.valueOf(0);
//            po10 = String.valueOf(3);
//            po11 = String.valueOf(1);
//        }


        if (string_dropdown_chatting_platform.equals("Dating site/app")) {
            cp1 = String.valueOf(1);
            cp2 = String.valueOf(4);
            cp3 = String.valueOf(0);
            cp4 = String.valueOf(0);
            cp5 = String.valueOf(0);
            cp6 = String.valueOf(0);
            cp7 = String.valueOf(0);
            cp8 = String.valueOf(0);
            cp9 = String.valueOf(0);
            cp10 = String.valueOf(0);
            cp11 = String.valueOf(0);
            cp12 = String.valueOf(0);
            cp13 = String.valueOf(0);
            cp14 = String.valueOf(0);
        }
        if (string_dropdown_chatting_platform.equals("Facebook")) {
            cp1 = String.valueOf(0);
            cp2 = String.valueOf(1);
            cp3 = String.valueOf(4);
            cp4 = String.valueOf(0);
            cp5 = String.valueOf(0);
            cp6 = String.valueOf(0);
            cp7 = String.valueOf(0);
            cp8 = String.valueOf(0);
            cp9 = String.valueOf(0);
            cp10 = String.valueOf(0);
            cp11 = String.valueOf(0);
            cp12 = String.valueOf(0);
            cp13 = String.valueOf(0);
            cp14 = String.valueOf(0);
        }
        if (string_dropdown_chatting_platform.equals("WhatsApp")) {
            cp1 = String.valueOf(0);
            cp2 = String.valueOf(0);
            cp3 = String.valueOf(1);
            cp4 = String.valueOf(4);
            cp5 = String.valueOf(0);
            cp6 = String.valueOf(0);
            cp7 = String.valueOf(0);
            cp8 = String.valueOf(0);
            cp9 = String.valueOf(0);
            cp10 = String.valueOf(0);
            cp11 = String.valueOf(0);
            cp12 = String.valueOf(0);
            cp13 = String.valueOf(0);
            cp14 = String.valueOf(0);
        }
        if (string_dropdown_chatting_platform.equals("Telegram")) {
            cp1 = String.valueOf(0);
            cp2 = String.valueOf(0);
            cp3 = String.valueOf(0);
            cp4 = String.valueOf(1);
            cp5 = String.valueOf(4);
            cp6 = String.valueOf(0);
            cp7 = String.valueOf(0);
            cp8 = String.valueOf(0);
            cp9 = String.valueOf(0);
            cp10 = String.valueOf(0);
            cp11 = String.valueOf(0);
            cp12 = String.valueOf(0);
            cp13 = String.valueOf(0);
            cp14 = String.valueOf(0);
        }
        if (string_dropdown_chatting_platform.equals("Instagram")) {
            cp1 = String.valueOf(0);
            cp2 = String.valueOf(0);
            cp3 = String.valueOf(0);
            cp4 = String.valueOf(0);
            cp5 = String.valueOf(1);
            cp6 = String.valueOf(4);
            cp7 = String.valueOf(0);
            cp8 = String.valueOf(0);
            cp9 = String.valueOf(0);
            cp10 = String.valueOf(0);
            cp11 = String.valueOf(0);
            cp12 = String.valueOf(0);
            cp13 = String.valueOf(0);
            cp14 = String.valueOf(0);
        }
        if (string_dropdown_chatting_platform.equals("LinkedIn")) {
            cp1 = String.valueOf(0);
            cp2 = String.valueOf(0);
            cp3 = String.valueOf(0);
            cp4 = String.valueOf(0);
            cp5 = String.valueOf(0);
            cp6 = String.valueOf(1);
            cp7 = String.valueOf(4);
            cp8 = String.valueOf(0);
            cp9 = String.valueOf(0);
            cp10 = String.valueOf(0);
            cp11 = String.valueOf(0);
            cp12 = String.valueOf(0);
            cp13 = String.valueOf(0);
            cp14 = String.valueOf(0);
        }
        if (string_dropdown_chatting_platform.equals("Email")) {
            cp1 = String.valueOf(0);
            cp2 = String.valueOf(0);
            cp3 = String.valueOf(0);
            cp4 = String.valueOf(0);
            cp5 = String.valueOf(0);
            cp6 = String.valueOf(0);
            cp7 = String.valueOf(1);
            cp8 = String.valueOf(4);
            cp9 = String.valueOf(0);
            cp10 = String.valueOf(0);
            cp11 = String.valueOf(0);
            cp12 = String.valueOf(0);
            cp13 = String.valueOf(0);
            cp14 = String.valueOf(0);
        }
        if (string_dropdown_chatting_platform.equals("Viber")) {
            cp1 = String.valueOf(0);
            cp2 = String.valueOf(0);
            cp3 = String.valueOf(0);
            cp4 = String.valueOf(0);
            cp5 = String.valueOf(0);
            cp6 = String.valueOf(0);
            cp7 = String.valueOf(0);
            cp8 = String.valueOf(1);
            cp9 = String.valueOf(4);
            cp10 = String.valueOf(0);
            cp11 = String.valueOf(0);
            cp12 = String.valueOf(0);
            cp13 = String.valueOf(0);
            cp14 = String.valueOf(0);
        }
        if (string_dropdown_chatting_platform.equals("Snapchat")) {
            cp1 = String.valueOf(0);
            cp2 = String.valueOf(0);
            cp3 = String.valueOf(0);
            cp4 = String.valueOf(0);
            cp5 = String.valueOf(0);
            cp6 = String.valueOf(0);
            cp7 = String.valueOf(0);
            cp8 = String.valueOf(0);
            cp9 = String.valueOf(1);
            cp10 = String.valueOf(4);
            cp11 = String.valueOf(0);
            cp12 = String.valueOf(0);
            cp13 = String.valueOf(0);
            cp14 = String.valueOf(0);
        }
        if (string_dropdown_chatting_platform.equals("WeChat")) {
            cp1 = String.valueOf(0);
            cp2 = String.valueOf(0);
            cp3 = String.valueOf(0);
            cp4 = String.valueOf(0);
            cp5 = String.valueOf(0);
            cp6 = String.valueOf(0);
            cp7 = String.valueOf(0);
            cp8 = String.valueOf(0);
            cp9 = String.valueOf(0);
            cp10 = String.valueOf(1);
            cp11 = String.valueOf(4);
            cp12 = String.valueOf(0);
            cp13 = String.valueOf(0);
            cp14 = String.valueOf(0);
        }
        if (string_dropdown_chatting_platform.equals("Skype")) {
            cp1 = String.valueOf(0);
            cp2 = String.valueOf(0);
            cp3 = String.valueOf(0);
            cp4 = String.valueOf(0);
            cp5 = String.valueOf(0);
            cp6 = String.valueOf(0);
            cp7 = String.valueOf(0);
            cp8 = String.valueOf(0);
            cp9 = String.valueOf(0);
            cp10 = String.valueOf(0);
            cp11 = String.valueOf(1);
            cp12 = String.valueOf(4);
            cp13 = String.valueOf(0);
            cp14 = String.valueOf(0);
        }
        if (string_dropdown_chatting_platform.equals("Line")) {
            cp1 = String.valueOf(0);
            cp2 = String.valueOf(0);
            cp3 = String.valueOf(0);
            cp4 = String.valueOf(0);
            cp5 = String.valueOf(0);
            cp6 = String.valueOf(0);
            cp7 = String.valueOf(0);
            cp8 = String.valueOf(0);
            cp9 = String.valueOf(0);
            cp10 = String.valueOf(0);
            cp11 = String.valueOf(0);
            cp12 = String.valueOf(1);
            cp13 = String.valueOf(4);
            cp14 = String.valueOf(0);
        }
        if (string_dropdown_chatting_platform.equals("Signal")) {
            cp1 = String.valueOf(0);
            cp2 = String.valueOf(0);
            cp3 = String.valueOf(0);
            cp4 = String.valueOf(0);
            cp5 = String.valueOf(0);
            cp6 = String.valueOf(0);
            cp7 = String.valueOf(0);
            cp8 = String.valueOf(0);
            cp9 = String.valueOf(0);
            cp10 = String.valueOf(0);
            cp11 = String.valueOf(0);
            cp12 = String.valueOf(0);
            cp13 = String.valueOf(1);
            cp14 = String.valueOf(4);
        }
        if (string_dropdown_chatting_platform.equals("Others")) {
            cp1 = String.valueOf(0);
            cp2 = String.valueOf(0);
            cp3 = String.valueOf(0);
            cp4 = String.valueOf(0);
            cp5 = String.valueOf(0);
            cp6 = String.valueOf(0);
            cp7 = String.valueOf(0);
            cp8 = String.valueOf(0);
            cp9 = String.valueOf(0);
            cp10 = String.valueOf(0);
            cp11 = String.valueOf(0);
            cp12 = String.valueOf(0);
            cp13 = String.valueOf(4);
            cp14 = String.valueOf(1);
        }

        // TODO: 10/8/2019 one hot encode the data before it is sent into the model

        //Schema Definitions
        Schema schema = new Schema.Builder()

                .addColumnCategorical("chatting platform", Arrays.asList(cp1,
                        cp2, cp3, cp4, cp5, cp6, cp7, cp8, cp9,
                        cp10, cp11, cp12, cp13, cp14))
                .addColumnCategorical("distance apart", Arrays.asList(po1, po2, po3,
                        po4))
                .addColumnCategorical("payment method", Arrays.asList(pm1, pm2, pm3,
                        pm4, pm5))
                .addColumnCategorical("emotional rating", Arrays.asList(er1, er2, er3,
                        er4))
//                .addColumnInteger(string_dropdown_emotions_rating)
                .addColumnsInteger(string_dropdown_video_call_freq)
                .addColumnsInteger(string_dropdown_voice_call_freq)
                .addColumnsInteger(switch_answer_money_asked)
                .addColumnsInteger(switch_answer_picker)
                .addColumnsInteger(switch_answer_unforeseen)
                .addColumnsInteger(switch_answer_business_proposal)
                .build();

        //Schema Transformation
        TransformProcess transformProcess = new TransformProcess.Builder(schema)
                .categoricalToOneHot("chatting platform")
                .categoricalToOneHot("distance apart")
                .categoricalToOneHot("payment method")
                .categoricalToOneHot("emotional rating")
                .removeColumns("chatting platform[4]")
                .removeColumns("distance apart[3]")
                .removeColumns("payment method[5]")
                .removeColumns("emotional rating[2]")
                .build();

        //save as Schema table
        Schema transformed_input = transformProcess.getFinalSchema();

        //convert from Schema to List of String so you can remove unnecessary strings
        int i;
        List<String> new_list = new ArrayList<>();
        String elsement;
        String new_elsement;
        for (i = 0; i <= 28; i++) {
            if (i >= 23) {
                elsement = transformed_input.getName(i);
                new_elsement = elsement;
                new_list.add(new_elsement);
            } else {
                elsement = transformed_input.getName(i);
                new_elsement = elsement.substring(elsement.length() - 2, elsement.length() - 1);
                new_list.add(new_elsement);
            }
        }

        //form List of String to List of Integer
        List<Integer> ose = new ArrayList<>();
        for (String s : new_list) ose.add(Integer.valueOf(s));

        //from List of Interger to float array
        float[] bytebuffer_float = new float[ose.size()];
        for (int a = 0; a < ose.size(); a++) {
            bytebuffer_float[a] = ose.get(a);
        }
        //initializing the output
        float[][] output = new float[1][1];
        AssetManager assetManager = getAssets();
        float final_dest=0;
        float final_dest_rounded=0;
        String final_str;
        float reversea_percent = 0;
        float percent = 0;
        //loading model into the interpreter
        try (Interpreter interpreter = new Interpreter(loadModelFile(assetManager))) {
            //fixing input and output parameters into the model and running it
            interpreter.run(bytebuffer_float, output);


            for (int e=0; e<output.length; e++){
                final_dest = output[e][0];
                //call round method to round off float
               final_dest_rounded = round(final_dest,2);
                percent = final_dest_rounded * 100 ;
                reversea_percent = 100 - percent;
            }

            if(0.5 >= final_dest){
                final_str = "its not a scam";
                hideProgressDialog();
                Toast.makeText(MainActivity.this, "I'm "+ reversea_percent +"%" +" sure " + final_str, Toast.LENGTH_LONG).show();
            }else{
                final_str = " It's a Scam!";
                hideProgressDialog();
                Toast.makeText(MainActivity.this, "I'm "+ percent +"%" +" sure " + final_str, Toast.LENGTH_LONG).show();
            }

        }
    }

    //loading model as a MappedByteBuffer type
    private MappedByteBuffer loadModelFile(AssetManager assets)
            throws IOException {
        AssetFileDescriptor fileDescriptor = assets.openFd("converted_FD_weights_model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    //ahow progress dialog
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.Uploading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    //hide progress dialog
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    //from many to two decimal point
    public static Float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

}