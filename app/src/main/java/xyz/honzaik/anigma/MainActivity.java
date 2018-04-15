package xyz.honzaik.anigma;

import android.animation.LayoutTransition;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Ang";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    }
}
