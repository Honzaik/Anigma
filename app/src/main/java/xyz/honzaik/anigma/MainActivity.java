package xyz.honzaik.anigma;

import android.animation.LayoutTransition;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Ang";

    private Encryptor enc;

    private Button btnTextMode;
    private Button btnFileMode;
    private Button btnRndIV;
    private Button btnCopyResult;
    private Button btnCopyIV;
    private Button btnTextModeEncrypt;
    private LinearLayout mainLinearLayout;
    private LinearLayout textModeLayout;
    private LinearLayout textModeIVLayout;
    private LinearLayout fileModeLayout;
    private Spinner spinnerTextAlgo;
    private EditText editTextPlaintext;
    private EditText editTextPassword;
    private EditText editTextIV;
    private TextView textViewResult;

    private ClipboardManager clipboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enc = new Encryptor();

        mainLinearLayout = (LinearLayout) findViewById(R.id.LinearLayoutMain);

        LayoutTransition lt = new LayoutTransition();


        btnTextMode = (Button) findViewById(R.id.btnTextMode);
        btnFileMode = (Button) findViewById(R.id.btnFileMode);


        editTextPlaintext = (EditText) findViewById(R.id.editTextPlainText);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextIV = (EditText) findViewById(R.id.editTextIV);

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


        btnRndIV = (Button) findViewById(R.id.btnRndIV);
        btnRndIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextIV.setText(enc.getRandomIV());
            }
        });

        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        textViewResult = (TextView) findViewById(R.id.textViewTextModeResult);

        btnCopyResult = (Button) findViewById(R.id.btnCopyResult);
        btnCopyResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipboard.setPrimaryClip(ClipData.newPlainText("encryptedString", textViewResult.getText()));
            }
        });

        btnCopyIV = (Button) findViewById(R.id.btnCopyIV);
        btnCopyIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipboard.setPrimaryClip(ClipData.newPlainText("IV", editTextIV.getText()));
            }
        });

        btnTextModeEncrypt = (Button) findViewById(R.id.btnTextModeEncrypt);
        btnTextModeEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStringEncryption();
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

    private void startStringEncryption(){
        String result = enc.encryptString(editTextPlaintext.getText().toString(), editTextPassword.getText().toString(), editTextIV.getText().toString());
        textViewResult.setText(result);
    }
}
