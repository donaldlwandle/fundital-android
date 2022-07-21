package com.example.infinity.downloads;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.infinity.R;

import java.util.ArrayList;

public class DownloadedFragment extends Fragment {
    private static final String TAG = "DownloadedFragment";
    private View notificationsView;

    /*Widgets*/
    private EditText inputText ;
    private Button executeButton;
    private TextView displayText;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        notificationsView = inflater.inflate(R.layout.fragment_downloaded, container , false);

        /*Initialize widgets 1st*/
        initialize();

        /*execute code on click*/
        executeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkInputField()){
                    executeCode();
                }
            }
        });



        return  notificationsView;
    }


    /*Execute method */
    
    private void executeCode() {

        long upperLimit = Long.parseLong(inputText.getText().toString());

        long largestPrime = upperLimit ;

        for (int j = 2 ; j <= Math.sqrt(upperLimit) ; j ++){
            if (upperLimit % j == 0 && isPrime(j)){
                largestPrime = j ;
            }
        }

        displayText.setText(String.valueOf(largestPrime) );
    }

    /*Initialize widgets*/
    private void initialize(){
        inputText = notificationsView.findViewById(R.id.value_input);
        executeButton = notificationsView.findViewById(R.id.execute_button);
        displayText = notificationsView.findViewById(R.id.display_results);
    }

    private boolean isPrime(int factor){
        for (int i = 3 ; i < Math.sqrt(factor) ; i += 2){
            if (factor % i == 0 && i != factor){
                return  false;
            }
        }
        return true;
    }



    private boolean checkInputField(){

        if (inputText.length() == 0){
            inputText.setError("Please input a value!");
            return false;
        }
        return true;
    }



    

}
