package ru.sber;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ru.sber.converter.R;
import ru.sber.converter.view.ConverterFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            return;
        }

        Fragment fragment = new ConverterFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,  fragment)
                .commitAllowingStateLoss();
    }
}
