package xyz.honzaik.anigma;

import android.animation.LayoutTransition;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Ang";

    private Encryptor enc;

    private Spinner spinnerTextAlgo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enc = new Encryptor();

        LinearLayout mainLinearLayout = (LinearLayout) findViewById(R.id.LinearLayoutMain);

        LayoutTransition lt = new LayoutTransition();


        Button btnTextMode = (Button) findViewById(R.id.btnTextMode);
        Button btnFileMode = (Button) findViewById(R.id.btnFileMode);

        final LinearLayout textModeLayout = (LinearLayout) findViewById(R.id.LinearLayoutTextMode);
        final LinearLayout fileModeLayout = (LinearLayout) findViewById(R.id.LinearLayoutFileMode);

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
        fillSpinners();

    }

    private void fillSpinners(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(enc.algorithmList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTextAlgo.setAdapter(adapter);
    }
}
