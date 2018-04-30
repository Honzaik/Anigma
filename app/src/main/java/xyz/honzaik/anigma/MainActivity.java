package xyz.honzaik.anigma;

import android.animation.LayoutTransition;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Ang";

    private Encryptor enc;

    private Button btnTextMode;
    private Button btnFileMode;
    private Button btnRndIV;
    private LinearLayout mainLinearLayout;
    private LinearLayout textModeLayout;
    private LinearLayout textModeIVLayout;
    private LinearLayout fileModeLayout;
    private Spinner spinnerTextAlgo;
    private EditText editTextPlaintext;
    private EditText editTextPassword;
    private EditText editTextIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enc = new Encryptor();

        mainLinearLayout = (LinearLayout) findViewById(R.id.LinearLayoutMain);

        LayoutTransition lt = new LayoutTransition();


        btnTextMode = (Button) findViewById(R.id.btnTextMode);
        btnFileMode = (Button) findViewById(R.id.btnFileMode);

        textModeLayout = (LinearLayout) findViewById(R.id.LinearLayoutTextMode);
        fileModeLayout = (LinearLayout) findViewById(R.id.LinearLayoutFileMode);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);

        btnTextMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textModeLayout.setVisibility(View.VISIBLE);
                fileModeLayout.setVisibility(View.GONE);
            }
        });

        btnFileMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textModeLayout.setVisibility(View.GONE);
                fileModeLayout.setVisibility(View.VISIBLE);
            }
        });

        spinnerTextAlgo = (Spinner) findViewById(R.id.spinnerTextAlgo);
        spinnerTextAlgo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s = (String) parent.getSelectedItem();
                changeAlgorithm(s);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fillSpinners();
        enc.printAvailableAlgos();

        textModeIVLayout = (LinearLayout) findViewById(R.id.LinearLayoutIV);
        editTextIV = (EditText) findViewById(R.id.editTextIV);


        btnRndIV = (Button) findViewById(R.id.btnRndIV);
        btnRndIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextIV.setText(enc.getRandomIV());
            }
        });
    }

    private void fillSpinners(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(enc.algorithmList.keySet());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTextAlgo.setAdapter(adapter);
    }

    private void changeAlgorithm(String name){
        enc.setCurrentAlgoritm(name);
        Algorithm current = enc.getCurrentAlgorithm();
        if(current.hasIV()){
            textModeIVLayout.setVisibility(View.VISIBLE);
        }else{
            textModeIVLayout.setVisibility(View.GONE);
        }
    }
}
