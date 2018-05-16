package xyz.honzaik.anigma;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.os.Message;

import xyz.honzaik.anigma.Tasks.StringTask;
import xyz.honzaik.anigma.Tasks.StringTaskState;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Ang";

    private Encryptor enc;

    private Button btnTextMode;
    private Button btnFileMode;
    private Button btnRndIV;
    private Button btnCopyResult;
    private Button btnCopyIV;
    private Button btnTextModeEncrypt;
    private Button btnTextModeDecrypt;
    private Button btnTextModeEncryption;
    private Button btnTextModeDecryption;
    private LinearLayout mainLinearLayout;
    private LinearLayout textModeLayout;
    private LinearLayout textModeEncryptionLayout;
    private LinearLayout textModeDecryptionLayout;
    private LinearLayout textModeIVLayout;
    private LinearLayout fileModeLayout;
    private Spinner spinnerTextAlgo;
    private EditText editTextPlaintext;
    private EditText editTextCiphertext;
    private EditText editTextTextModeEncryptionPassword;
    private EditText editTextTextModeDecryptionPassword;
    private EditText editTextTextModeIV;
    private TextView textViewResult;
    private ProgressBar progressBarEnc;
    private ProgressBar progressBarDec;

    private Handler mainHandler;

    private ClipboardManager clipboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initHandler();

        setContentView(R.layout.activity_main);

        enc = new Encryptor(this);

        mainLinearLayout = (LinearLayout) findViewById(R.id.LinearLayoutMain);

        btnTextMode = (Button) findViewById(R.id.btnTextMode);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnTextMode.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btnColor1Pressed)));
        }
        btnFileMode = (Button) findViewById(R.id.btnFileMode);


        editTextPlaintext = (EditText) findViewById(R.id.editTextPlainText);
        editTextCiphertext = (EditText) findViewById(R.id.editTextCiphertext);
        editTextTextModeEncryptionPassword = (EditText) findViewById(R.id.editTextTextModeEncryptionPassword);
        editTextTextModeDecryptionPassword = (EditText) findViewById(R.id.editTextTextModeDecryptionPassword);
        editTextTextModeIV = (EditText) findViewById(R.id.editTextTextModeIV);

        textModeLayout = (LinearLayout) findViewById(R.id.LinearLayoutTextMode);
        fileModeLayout = (LinearLayout) findViewById(R.id.LinearLayoutFileMode);

        textModeEncryptionLayout = (LinearLayout) findViewById(R.id.LinearLayoutTextModeEncryption);
        textModeDecryptionLayout = (LinearLayout) findViewById(R.id.LinearLayoutTextModeDecryption);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);

        btnTextMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textModeLayout.setVisibility(View.VISIBLE);
                fileModeLayout.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btnTextMode.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btnColor1Pressed)));
                    btnFileMode.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btnColor1)));
                }
            }
        });

        btnFileMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textModeLayout.setVisibility(View.GONE);
                fileModeLayout.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btnTextMode.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btnColor1)));
                    btnFileMode.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btnColor1Pressed)));
                }
            }
        });

        btnTextModeEncryption = (Button) findViewById(R.id.btnTextModeEncryptionMode);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnTextModeEncryption.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btnColor2Pressed)));
        }
        btnTextModeDecryption = (Button) findViewById(R.id.btnTextModeDecryptionMode);

        btnTextModeEncryption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textModeEncryptionLayout.setVisibility(View.VISIBLE);
                textModeDecryptionLayout.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btnTextModeEncryption.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btnColor2Pressed)));
                    btnTextModeDecryption.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btnColor2)));
                }
            }
        });

        btnTextModeDecryption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textModeEncryptionLayout.setVisibility(View.GONE);
                textModeDecryptionLayout.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btnTextModeEncryption.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btnColor2)));
                    btnTextModeDecryption.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btnColor2Pressed)));
                }

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
                try {
                    editTextTextModeIV.setText(enc.getRandomIV());
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        textViewResult = (TextView) findViewById(R.id.textModeResult);

        btnCopyResult = (Button) findViewById(R.id.btnCopyResult);
        btnCopyResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipboard.setPrimaryClip(ClipData.newPlainText("resultString", textViewResult.getText().toString().trim()));
            }
        });

        btnCopyIV = (Button) findViewById(R.id.btnCopyIV);
        btnCopyIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipboard.setPrimaryClip(ClipData.newPlainText("IV", editTextTextModeIV.getText().toString().trim()));
            }
        });

        progressBarEnc = (ProgressBar) findViewById(R.id.textModeProgressEnc);
        progressBarDec = (ProgressBar) findViewById(R.id.textModeProgressDec);

        btnTextModeEncrypt = (Button) findViewById(R.id.btnTextModeEncrypt);
        btnTextModeDecrypt = (Button) findViewById(R.id.btnTextModeDecrypt);

        btnTextModeEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarEnc.setVisibility(View.VISIBLE);
                startStringEncryption();
            }
        });
        btnTextModeDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarDec.setVisibility(View.VISIBLE);
                startStringDecryption();
            }
        });
    }

    private void initHandler() {
        mainHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                StringTask task = (StringTask) inputMessage.obj;
                String resultString = null;
                switch (task.getState()) {
                    case SUCCESS:
                        resultString = task.result;
                        break;
                    case ERROR_ENCRYPTION:
                        resultString = getResources().getString(R.string.text_mode_error_encryption);
                        break;
                    case ERROR_DECRYPTION:
                        resultString = getResources().getString(R.string.text_mode_error_decryption);
                        break;
                    case ERROR_DECODING:
                        resultString = getResources().getString(R.string.text_mode_error_decoding);
                        break;
                    case START:
                        resultString = getResources().getString(R.string.text_mode_error_nothing);
                        break;
                    default:
                }
                if (task.getState() != StringTaskState.SUCCESS) {
                    textViewResult.setTextColor(Color.RED);
                } else {
                    textViewResult.setTextColor(Color.BLACK);
                }
                textViewResult.setText(resultString);
                if(task.isEncrypting()){
                    progressBarEnc.setVisibility(View.GONE);
                }else{
                    progressBarDec.setVisibility(View.GONE);
                }
            }

        };
    }

    public Handler getHandler(){
        return mainHandler;
    }

    private void fillSpinners(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item);
        adapter.addAll(enc.algorithmList.keySet());
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerTextAlgo.setAdapter(adapter);
    }

    private void changeAlgorithm(String name){
        enc.setCurrentAlgoritm(name);
        if(enc.getCurrentAlgorithm().hasIV){
            textModeIVLayout.setVisibility(View.VISIBLE);
        }else{
            textModeIVLayout.setVisibility(View.GONE);
        }
    }

    private void startStringEncryption(){
        enc.getStringTask().encryptString(editTextPlaintext.getText().toString(), editTextTextModeEncryptionPassword.getText().toString(), editTextTextModeIV.getText().toString());
    }

    private void startStringDecryption(){
        enc.getStringTask().decryptString(editTextCiphertext.getText().toString(), editTextTextModeDecryptionPassword.getText().toString());
    }

}
