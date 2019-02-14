package giampy.simon.customcalculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RandomNumber extends Activity {

    Button ok;

    EditText min;
    EditText max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_random);

        max = (EditText) findViewById(R.id.max);
        min = (EditText) findViewById(R.id.min);

        ok = (Button) findViewById(R.id.finish);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (min.getText().toString().equals("") || max.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.random_absent, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("MAX", max.getText().toString());
                    intent.putExtra("MIN", min.getText().toString());
                    setResult(1, intent);
                    finish();
                }
            }
        });
    }

}

