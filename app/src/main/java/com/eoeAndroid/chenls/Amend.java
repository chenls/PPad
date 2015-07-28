package com.eoeAndroid.chenls;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.eoeAndroid.chenls.R.*;

public class Amend extends Activity {
    private Button buttonBack;
    private TextView title;
    private TextView textTitle;
    private EditText textBoby;
    private Button sure;
    private Intent intent;
    private DiaryDbAdapter mDbHelper;

    private void assignViews() {
        buttonBack = (Button) findViewById(id.button_back);
        title = (TextView) findViewById(id.title);
        textTitle = (TextView) findViewById(id.text_title);
        textBoby = (EditText) findViewById(id.text_boby);
        sure = (Button) findViewById(id.sure);
    }

    private String mRowId;
    private String title_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_amend);
        assignViews();
        mDbHelper = new DiaryDbAdapter(this);
        mDbHelper.open();
        sure.setOnClickListener(new OnClickListenerImpl());
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amend.this.finish();
            }
        });
        intent = getIntent();
        mRowId = intent.getStringExtra("id");
        String date_str = intent.getStringExtra("date");
        String title_str = intent.getStringExtra("title");
        title_data = intent.getStringExtra("title1");
        textTitle.setText(title_str);
        textBoby.setText(date_str);
    }

    private class OnClickListenerImpl implements View.OnClickListener {
        @SuppressWarnings("deprecation")
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case id.sure:
                    boolean f = mDbHelper.updateDiary(mRowId, title_data, textBoby.getText().toString());
                    if (f) {
                        Toast.makeText(Amend.this, "修改成功", Toast.LENGTH_SHORT).show();
                        intent = new Intent(Amend.this, Detail.class);
                        Bundle b = new Bundle();
                        b.putString("data", textBoby.getText().toString());
                        intent.putExtras(b);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    break;
            }
        }
    }
}
