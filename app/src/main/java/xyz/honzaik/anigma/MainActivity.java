package xyz.honzaik.anigma;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.os.Message;

import java.io.File;
import java.util.ArrayList;

import xyz.honzaik.anigma.Tasks.FileTask;
import xyz.honzaik.anigma.Tasks.FileTaskState;
import xyz.honzaik.anigma.Tasks.StringTask;
import xyz.honzaik.anigma.Tasks.StringTaskState;

/**
 * Hlavní a jediná aktivita aplikace Anigma. Stará se hlavně o UI.
 */
public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Ang";
    private static final int PERMISSIONS_REQUEST = 9;

    private AlgoManager algoManager;

    private Button btnTextMode;
    private Button btnFileMode;
    private Button btnTextRndIV;
    private Button btnFileRndIV;
    private Button btnCopyResult;
    private Button btnTextCopyIV;
    private Button btnFileCopyIV;
    private Button btnTextModeEncrypt;
    private Button btnTextModeDecrypt;
    private Button btnFileModeEncrypt;
    private Button btnFileModeDecrypt;
    private Button btnTextModeEncryption;
    private Button btnTextModeDecryption;
    private Button btnFileModeEncryption;
    private Button btnFileModeDecryption;
    private LinearLayout mainLinearLayout;
    private LinearLayout textModeLayout;
    private LinearLayout textModeEncryptionLayout;
    private LinearLayout textModeDecryptionLayout;
    private LinearLayout textModeIVLayout;
    private LinearLayout fileModeLayout;
    private LinearLayout fileModeEncryptionLayout;
    private LinearLayout fileModeDecryptionLayout;
    private Spinner spinnerTextAlgo;
    private EditText editTextPlaintext;
    private EditText editTextCiphertext;
    private EditText editTextTextModeEncryptionPassword;
    private EditText editTextTextModeDecryptionPassword;
    private EditText editTextFileModeEncryptionPassword;
    private EditText editTextFileModeDecryptionPassword;
    private EditText editTextTextModeIV;
    private EditText editTextFileModeIV;
    private TextView textViewTextResult;
    private TextView textViewFileResult;
    private TextView textViewFileHelper;
    private ProgressBar progressTextEnc;
    private ProgressBar progressTextDec;
    private ProgressBar progressFileEnc;
    private ProgressBar progressFileDec;

    private Handler mainHandler;

    private ClipboardManager clipboard;

    private boolean isTextMode = true;
    private FileManager fileManager;
    private ListView filesList;
    private View oldSelectedItem;

    /**
     * Funkce aktivity onCreate. Volá se při zapnutí aplikace. Inicializuje všechny UI prvky.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);

        initHandler();

        algoManager = new AlgoManager(this);
        fileManager = new FileManager();
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        mainLinearLayout = (LinearLayout) findViewById(R.id.LinearLayoutMain);
        textModeLayout = (LinearLayout) findViewById(R.id.LinearLayoutTextMode);
        fileModeLayout = (LinearLayout) findViewById(R.id.LinearLayoutFileMode);
        textModeIVLayout = (LinearLayout) findViewById(R.id.LinearLayoutTextIV);
        textModeEncryptionLayout = (LinearLayout) findViewById(R.id.LinearLayoutTextModeEncryption);
        textModeDecryptionLayout = (LinearLayout) findViewById(R.id.LinearLayoutTextModeDecryption);
        fileModeEncryptionLayout = (LinearLayout) findViewById(R.id.LinearLayoutFileModeEncryption);
        fileModeDecryptionLayout = (LinearLayout) findViewById(R.id.LinearLayoutFileModeDecryption);

        editTextPlaintext = (EditText) findViewById(R.id.editTextPlainText);
        editTextCiphertext = (EditText) findViewById(R.id.editTextCiphertext);
        editTextTextModeEncryptionPassword = (EditText) findViewById(R.id.editTextTextModeEncryptionPassword);
        editTextTextModeDecryptionPassword = (EditText) findViewById(R.id.editTextTextModeDecryptionPassword);
        editTextFileModeEncryptionPassword = (EditText) findViewById(R.id.editTextFileModeEncryptionPassword);
        editTextFileModeDecryptionPassword = (EditText) findViewById(R.id.editTextFileModeDecryptionPassword);
        editTextTextModeIV = (EditText) findViewById(R.id.editTextTextModeIV);
        editTextFileModeIV = (EditText) findViewById(R.id.editTextFileModeIV);

        progressTextEnc = (ProgressBar) findViewById(R.id.textModeProgressEnc);
        progressTextDec = (ProgressBar) findViewById(R.id.textModeProgressDec);
        progressFileEnc = (ProgressBar) findViewById(R.id.progressBarFileEncryption);
        progressFileDec = (ProgressBar) findViewById(R.id.progressBarFileDecryption);

        textViewTextResult = (TextView) findViewById(R.id.textModeResult);
        textViewFileResult = (TextView) findViewById(R.id.fileModeResult);
        textViewFileHelper = (TextView) findViewById(R.id.textViewFileHelper);
        try{
            textViewFileHelper.setText(getResources().getText(R.string.file_mode_files_helper) + " " + fileManager.getMainFolder().getAbsolutePath());
        } catch (Exception e){
            e.printStackTrace();
        }

        filesList = (ListView) findViewById(R.id.listViewFiles);
        filesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(oldSelectedItem != null){
                    oldSelectedItem.setBackgroundColor(getResources().getColor(R.color.blond));
                }
                oldSelectedItem = view;
                view.setBackgroundColor(getResources().getColor(R.color.bitterLemon));
                final String item = (String) parent.getItemAtPosition(position);
                Log.d(TAG, item);
            }
        });

        btnTextMode = (Button) findViewById(R.id.btnTextMode);
        btnFileMode = (Button) findViewById(R.id.btnFileMode);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnTextMode.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btnColor1Pressed)));
        }

        btnTextMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMode();
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
                switchMode();
                textModeLayout.setVisibility(View.GONE);
                fileModeLayout.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btnTextMode.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btnColor1)));
                    btnFileMode.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btnColor1Pressed)));
                }
            }
        });

        btnTextModeEncryption = (Button) findViewById(R.id.btnTextModeEncryptionMode);
        btnTextModeDecryption = (Button) findViewById(R.id.btnTextModeDecryptionMode);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnTextModeEncryption.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btnColor2Pressed)));
        }

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

        btnTextRndIV = (Button) findViewById(R.id.btnTextRndIV);
        btnTextRndIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    editTextTextModeIV.setText(algoManager.getRandomIV());
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        spinnerTextAlgo = (Spinner) findViewById(R.id.spinnerTextAlgo);
        spinnerTextAlgo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s = (String) parent.getSelectedItem();
                changeAlgorithm(s);
                if(textModeLayout.getVisibility() == View.VISIBLE){ //regenerate IV
                    btnTextRndIV.performClick();
                }else{
                    btnFileRndIV.performClick();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fillSpinners();

        btnCopyResult = (Button) findViewById(R.id.btnCopyResult);
        btnCopyResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipboard.setPrimaryClip(ClipData.newPlainText("resultString", textViewTextResult.getText().toString().trim()));
            }
        });

        btnTextCopyIV = (Button) findViewById(R.id.btnCopyTextIV);
        btnTextCopyIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipboard.setPrimaryClip(ClipData.newPlainText("IV", editTextTextModeIV.getText().toString().trim()));
            }
        });

        btnTextModeEncrypt = (Button) findViewById(R.id.btnTextModeEncrypt);
        btnTextModeDecrypt = (Button) findViewById(R.id.btnTextModeDecrypt);

        btnTextModeEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressTextEnc.setVisibility(View.VISIBLE);
                startStringEncryption();
            }
        });
        btnTextModeDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressTextDec.setVisibility(View.VISIBLE);
                startStringDecryption();
            }
        });

        btnFileModeEncryption = (Button) findViewById(R.id.btnFileModeEncryptionMode);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnFileModeEncryption.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btnColor2Pressed)));
        }
        btnFileModeDecryption = (Button) findViewById(R.id.btnFileModeDecryptionMode);

        btnFileModeEncryption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileModeEncryptionLayout.setVisibility(View.VISIBLE);
                fileModeDecryptionLayout.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btnFileModeEncryption.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btnColor2Pressed)));
                    btnFileModeDecryption.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btnColor2)));
                }
            }
        });

        btnFileModeDecryption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileModeEncryptionLayout.setVisibility(View.GONE);
                fileModeDecryptionLayout.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btnFileModeEncryption.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btnColor2)));
                    btnFileModeDecryption.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btnColor2Pressed)));
                }

            }
        });

        btnFileRndIV = (Button) findViewById(R.id.btnFileRndIV);
        btnFileRndIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    editTextFileModeIV.setText(algoManager.getRandomIV());
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        btnFileCopyIV = (Button) findViewById(R.id.btnFileCopyIV);
        btnFileCopyIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipboard.setPrimaryClip(ClipData.newPlainText("IV", editTextFileModeIV.getText().toString().trim()));
            }
        });


        btnFileModeEncrypt = (Button) findViewById(R.id.btnFileModeEncrypt);
        btnFileModeDecrypt = (Button) findViewById(R.id.btnFileModeDecrypt);

        btnFileModeEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressFileEnc.setVisibility(View.VISIBLE);
                startFileEncryption();
            }
        });
        btnFileModeDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressFileDec.setVisibility(View.VISIBLE);
                startFileDecryption();
            }
        });
    }

    /**
     * Funkce aktivity onStart. Volá se při každém volání aplikace "do předu". Nemusí býr aplikace vypnutá. Zkontroluje, zda aplikace má potřebná oprávnění.
     */
    @Override
    protected void onStart() {
        super.onStart();
        checkPermissions();

    }

    /**
     * Zpracuje výsledek žádosti o oprávnění. Pokud aplikace nedostane oprávnění, tak se restartuje a žádá znovu.
     * @param requestCode Kód žádosti
     * @param permissions O jaké oprávnění bylo žádáno
     * @param grantResults Výsledek žádosti
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            finishAffinity();
        }else{
            restartApp();
        }
    }

    /**
     * Zkontroluje, zda aplikace má potřebná oprávnění, popřípadě o ně požádá. Pokud má oprávnění, tak zavolá načtení seznamu souborů.
     */
    private void checkPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSIONS_REQUEST);
        }else{
            loadFiles();
        }
    }

    /**
     * Natvrdo restartuje aplikaci. Používá se při dostání oprávnění.
     */
    private void restartApp(){
        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finishAffinity();
        android.os.Process.killProcess(android.os.Process.myPid()); //hard restart app for permissions to reload
    }

    /**
     * Vrátí handler třídy.
     * @return
     */
    public Handler getHandler(){
        return mainHandler;
    }

    /**
     * Inicializuje handler, který se používá pro zpracování informací od šifrujícího/dešifrujícího vlákna.
     */
    private void initHandler() {
        mainHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                if(inputMessage.obj instanceof StringTask){
                    handleMessageFromStringTask((StringTask)inputMessage.obj);
                }else{
                    handleMessageFromFileTask((FileTask)inputMessage.obj);
                }
            }

        };
    }

    /**
     * Zpracuje zprávu od StringTask
     * @param task Odkaz na aktuální objekt StringTask
     */
    private void handleMessageFromStringTask(StringTask task){
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
            textViewTextResult.setTextColor(Color.RED);
        } else {
            textViewTextResult.setTextColor(Color.BLACK);
        }
        textViewTextResult.setText(resultString);
        if(task.isEncrypting()){
            progressTextEnc.setVisibility(View.GONE);
        }else{
            progressTextDec.setVisibility(View.GONE);
        }
    }

    /**
     * Zpracuje zprávu od FileTask
     * @param task Odkaz na aktuální objekt FileTask
     */
    private void handleMessageFromFileTask(FileTask task){
        String resultString = null;
        switch (task.getState()) {
            case SUCCESS:
                if(task.isEncrypting()){
                    resultString = getResources().getString(R.string.file_mode_success_encryption);
                }else{
                    resultString = getResources().getString(R.string.file_mode_success_decryption);
                }
                break;
            case ERROR_ENCRYPTION:
                resultString = getResources().getString(R.string.file_mode_error_encryption);
                break;
            case ERROR_DECRYPTION:
                resultString = getResources().getString(R.string.file_mode_error_decryption);
                break;
            case ERROR_DECODING:
                resultString = getResources().getString(R.string.file_mode_error_decoding);
                break;
            case UPDATE_PROGRESS:
                if(task.isEncrypting()){
                    progressFileEnc.setProgress(task.progress);
                }else{
                    progressFileDec.setProgress(task.progress);
                }
            case START:
                resultString = getResources().getString(R.string.file_mode_error_nothing);
                break;
            default:
        }
        if(task.getState() != FileTaskState.UPDATE_PROGRESS){
            textViewFileResult.setText(resultString);
            if (task.getState() != FileTaskState.SUCCESS) {
                textViewFileResult.setTextColor(Color.RED);
                task.output.delete();
            } else {
                textViewFileResult.setTextColor(Color.BLACK);
            }
            if(task.isEncrypting()){
                progressFileEnc.setVisibility(View.GONE);
            } else {
                progressFileDec.setVisibility(View.GONE);
            }
            loadFiles(); //refresh file list
        }
    }

    /**
     * Naplní seznam dostupných algoritmů.
     */
    private void fillSpinners(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item);
        adapter.addAll(algoManager.algorithmList.keySet());
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerTextAlgo.setAdapter(adapter);
    }

    /**
     * Provede změnu aktuálního algoritmu.
     * @param name Jméno vybraného algoritmu
     */
    private void changeAlgorithm(String name){
        algoManager.setCurrentAlgoritm(name);
        if(algoManager.getCurrentAlgorithm().hasIV){
            textModeIVLayout.setVisibility(View.VISIBLE);
        }else{
            textModeIVLayout.setVisibility(View.GONE);
        }
    }

    /**
     * Načte potřebné parametry z UI. Spustí šifrování textu.
     */
    private void startStringEncryption(){
        algoManager.getStringTask().encryptString(editTextPlaintext.getText().toString(), editTextTextModeEncryptionPassword.getText().toString(), editTextTextModeIV.getText().toString());
    }

    /**
     * Načte potřebné parametry z UI. Spustí dešifrovaní textu.
     */
    private void startStringDecryption(){
        algoManager.getStringTask().decryptString(editTextCiphertext.getText().toString(), editTextTextModeDecryptionPassword.getText().toString());
    }

    /**
     * Změní aktuální mód aplikace.
     */
    private void switchMode(){
        isTextMode = !isTextMode;
        if(!isTextMode){
            loadFiles();
        }
    }

    /**
     * Načte seznam dostupných souborů v hlavní složce aplikace.
     */
    private void loadFiles(){
        Log.d(TAG, "loading files");
        ArrayList<File> files = fileManager.getFiles();
        ArrayList<String> pathList = new ArrayList<>();
        for(File f : files){
            pathList.add(f.getName());
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item);
        adapter.addAll(pathList);
        filesList.setAdapter(adapter);
    }

    /**
     * Načte potřebné parametry z UI. Spustí šifrovaní souboru.
     */
    private void startFileEncryption(){
        progressFileEnc.setProgress(0);
        String path = null;
        try{
            path = fileManager.getMainFolder() + "/" + ((TextView) oldSelectedItem).getText().toString();
        } catch (Exception e){
            e.printStackTrace();
            return;
        }
        File input = new File(path);
        File output = fileManager.getOutputFile(true, input);
        algoManager.getFileTask().encryptFile(input, output, editTextFileModeEncryptionPassword.getText().toString(), editTextFileModeIV.getText().toString());
    }

    /**
     * Načte potřebné parametry z UI. Spustí dešifrovaní souboru.
     */
    private void startFileDecryption(){
        progressFileDec.setProgress(0);
        String path = null;
        try{
            path = fileManager.getMainFolder() + "/" + ((TextView) oldSelectedItem).getText().toString();
        } catch (Exception e){
            e.printStackTrace();
            return;
        }
        File input = new File(path);
        File output = fileManager.getOutputFile(false, input);
        algoManager.getFileTask().decryptFile(input, output, editTextFileModeDecryptionPassword.getText().toString());
    }

}
