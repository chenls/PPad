package com.eoeAndroid.chenls;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Calendar;

public class Add_data extends Activity {
    private Button buttonBack;
    private AutoCompleteTextView room;
    private EditText name;
    private EditText date;
    private Button timeButton;
    private EditText phone;
    private EditText money;
    private EditText rent;
    private EditText water;
    private EditText electric;
    private EditText mark;
    private Button sure;

    private void assignViews() {
        buttonBack = (Button) findViewById(R.id.button_back);
        room = (AutoCompleteTextView) findViewById(R.id.room);
        name = (EditText) findViewById(R.id.name);
        date = (EditText) findViewById(R.id.date);
        timeButton = (Button) findViewById(R.id.time_button);
        phone = (EditText) findViewById(R.id.phone);
        money = (EditText) findViewById(R.id.money);
        rent = (EditText) findViewById(R.id.rent);
        water = (EditText) findViewById(R.id.water);
        electric = (EditText) findViewById(R.id.electric);
        mark = (EditText) findViewById(R.id.mark);
        sure = (Button) findViewById(R.id.sure);
    }

    private Dialog mdialog;
    private DatePicker dp;
    private Calendar calendar = null;
    private DiaryDbAdapter mDbHelper;
    private Context mcontext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        mDbHelper = new DiaryDbAdapter(this);
        mDbHelper.open();
        assignViews();
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add_data.this.finish();
            }
        });
        timeButton.setOnClickListener(new OnClickListenerImpl());
        sure.setOnClickListener(new OnClickListenerImpl());
        String[] countries = getResources().getStringArray(R.array.countries_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, countries);//配置Adaptor
        room.setAdapter(adapter);
    }

    // 按钮点击事件
    private class OnClickListenerImpl implements View.OnClickListener {
        @SuppressWarnings("deprecation")
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.sure:

                    String room_str = room.getText().toString();
                    String name_str = name.getText().toString();
                    String date_str = date.getText().toString();
                    String phone_str = phone.getText().toString();
                    String money_str = money.getText().toString();
                    String rent_str = rent.getText().toString();
                    String water_str = water.getText().toString();
                    String electric_str = electric.getText().toString();
                    String mark_str = mark.getText().toString();

                    String estring = "输入信息不能为空";
                    ForegroundColorSpan fg = new ForegroundColorSpan(Color.BLACK);
                    SpannableStringBuilder ss = new SpannableStringBuilder(estring);
                    ss.setSpan(fg, 0, estring.length(), 0);
                    if (room_str.equals("")) {
                        room.setError(ss);
                        return;
                    }
                    if (name_str.equals("")) {
                        name.setError(ss);
                        return;
                    }
                    if (date_str.equals("")) {
                        date.setError(ss);
                        return;
                    }
                    //该条记录的id，如果插入失败会返回-1。
                    long re = mDbHelper.createDiary(room_str, name_str, date_str, phone_str, money_str, rent_str, water_str, electric_str, mark_str);
                    if (re != -1) {
                        finish();
                        Toast.makeText(Add_data.this, "添加成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Add_data.this, "添加失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.time_button:
                    showDialog(0);// 日期弹出框
                    @SuppressWarnings("static-access")
                    int SDKVersion = Add_data.this
                            .getSDKVersionNumber();// 获取系统版本
                    System.out.println("SDKVersion = " + SDKVersion);
                    dp = findDatePicker((ViewGroup) mdialog.getWindow()
                            .getDecorView());// 设置弹出年月日
                    // dp.updateDate(2015, 0, 1);
                    if (dp != null) {

                        if (SDKVersion < 11) {
                            ((ViewGroup) dp.getChildAt(0)).getChildAt(0)
                                    .setVisibility(View.GONE);
                        } else if (SDKVersion > 14) {
                            ((ViewGroup) ((ViewGroup) dp.getChildAt(0))
                                    .getChildAt(1)).getChildAt(0).setVisibility(
                                    View.GONE);
                        }
                    }
                    break;
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static int getSDKVersionNumber() {
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion;
    }

    private DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;
    }

    protected Dialog onCreateDialog(int id) { // 对应上面的showDialog(0);//日期弹出框
        mdialog = null;
        switch (id) {
            case 0:
                calendar = Calendar.getInstance();
                mdialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                date.setText(year + "-" + (monthOfYear + 1)
                                        + "-" + dayOfMonth);
                            }
                        }, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                break;
        }
        return mdialog;
    }

}
