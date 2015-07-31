package com.eoeAndroid.chenls;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.eoeAndroid.chenls.R.*;

public class Detail extends Activity {
    private Button buttonBack;
    private TextView title;
    private TextView text0;
    private TextView room;
    private TextView text1;
    private TextView name;
    private TextView text11;
    private TextView date;
    private TextView text111;
    private TextView phone;
    private TextView text2;
    private TextView rent;
    private TextView text22;
    private TextView money;
    private TextView text3;
    private TextView water;
    private TextView text4;
    private TextView electric;
    private TextView text_mark;
    private TextView mark;

    private void assignViews() {
        buttonBack = (Button) findViewById(id.button_back);
        title = (TextView) findViewById(id.title);
        text0 = (TextView) findViewById(id.text0);
        room = (TextView) findViewById(id.room);
        text1 = (TextView) findViewById(id.text1);
        name = (TextView) findViewById(id.name);
        text11 = (TextView) findViewById(id.text11);
        date = (TextView) findViewById(id.date);
        text111 = (TextView) findViewById(id.text111);
        phone = (TextView) findViewById(id.phone);
        text2 = (TextView) findViewById(id.text2);
        rent = (TextView) findViewById(id.rent);
        text22 = (TextView) findViewById(id.text22);
        money = (TextView) findViewById(id.money);
        text3 = (TextView) findViewById(id.text3);
        water = (TextView) findViewById(id.water);
        text4 = (TextView) findViewById(id.text4);
        electric = (TextView) findViewById(id.electric);
        text_mark = (TextView) findViewById(id.text_mark);
        mark = (TextView) findViewById(id.mark);
    }

    private DiaryDbAdapter mDbHelper;
    private Long mRowId;
    private String phone_str;
    private String room_str;
    private String name_str;
    private String date_str;
    private String rent_str;
    private String money_str;
    private String mark_str;
    private String water_str;
    private String electric_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_detail);
        assignViews();
        mDbHelper = new DiaryDbAdapter(this);
        mDbHelper.open();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mRowId = extras.getLong(DiaryDbAdapter.KEY_ROWID);
            room_str = extras.getString(DiaryDbAdapter.KEY_ROOM);
            name_str = extras.getString(DiaryDbAdapter.KEY_NAME);
            date_str = extras.getString(DiaryDbAdapter.KEY_DATE);
            phone_str = extras.getString(DiaryDbAdapter.KEY_PHONE);
            rent_str = extras.getString(DiaryDbAdapter.KEY_RENT);
            money_str = extras.getString(DiaryDbAdapter.KEY_MONEY);
            mark_str = extras.getString(DiaryDbAdapter.KEY_MARK);
            water_str = extras.getString(DiaryDbAdapter.KEY_WATER);
            electric_str = extras.getString(DiaryDbAdapter.KEY_ELECTRIC);
            room.setText(room_str);
            if (room_str == null || room_str.equals("")) {
                room.setText("空");
            }
            name.setText(name_str);
            if (name_str == null || name_str.equals("")) {
                name.setText("空");
            }
            date.setText(date_str);
            if (date_str == null || date_str.equals("")) {
                date.setText("空");
            }
            phone.setText(phone_str);
            if (phone_str == null || phone_str.equals("")) {
                phone.setText("空");
            }
            rent.setText(rent_str);
            if (rent_str == null || rent_str.equals("")) {
                rent.setText("空");
            }
            money.setText(money_str);
            if (money_str == null || money_str.equals("")) {
                money.setText("空");
            }
            mark.setText(mark_str);
            if (mark_str == null || mark_str.equals("")) {
                mark.setText("空");
            }
            water.setText(water_str);
            if (water_str == null || water_str.equals("")) {
                water.setText("空");
            }
            electric.setText(electric_str);
            if (electric_str == null || electric_str.equals("")) {
                electric.setText("空");
            }
        }


        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Detail.this.finish();
            }
        });
        text0.setOnClickListener(new OnClickListenerImpl());
        text1.setOnClickListener(new OnClickListenerImpl());
        text11.setOnClickListener(new OnClickListenerImpl());
        text111.setOnClickListener(new OnClickListenerImpl());
        text2.setOnClickListener(new OnClickListenerImpl());
        text22.setOnClickListener(new OnClickListenerImpl());
        text_mark.setOnClickListener(new OnClickListenerImpl());
        text3.setOnClickListener(new OnClickListenerImpl());
        text4.setOnClickListener(new OnClickListenerImpl());
        mark.setOnClickListener(new OnClickListenerImpl());
        water.setOnClickListener(new OnClickListenerImpl());
        electric.setOnClickListener(new OnClickListenerImpl());
        image(text0, drawable.room, drawable.ic_arrow_right);
        image(text1, drawable.name, drawable.ic_arrow_right);
        image(text11, drawable.date, drawable.ic_arrow_right);
        image(text111, drawable.phone, drawable.ic_arrow_right);
        image(text2, drawable.rent, drawable.ic_arrow_right);
        image(text22, drawable.money, drawable.ic_arrow_right);
        image(text_mark, drawable.mark, drawable.ic_arrow_up);
        image(text3, drawable.water, drawable.ic_arrow_up);
        image(text4, drawable.electric, drawable.ic_arrow_up);
    }

    public void image(TextView text, int x, int y) {
        Drawable drawable = getResources().getDrawable(x);
        drawable.setBounds(0, 0, drawable.getMinimumWidth() / 3,
                drawable.getMinimumHeight() / 3);

        Drawable drawable2 = getResources().getDrawable(y);
        drawable2.setBounds(0, 0, drawable2.getMinimumWidth(),
                drawable2.getMinimumHeight());
        text.setCompoundDrawables(drawable, null, drawable2, null);
    }

    boolean f = false;
    boolean f2 = false;

    boolean f3 = false;

    private class OnClickListenerImpl implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case id.text0:
                    Intent intent = new Intent(
                            Detail.this, Amend.class);
                    intent.putExtra("id", mRowId + "");
                    intent.putExtra("title", "房间名:");
                    intent.putExtra("title1", "room");
                    intent.putExtra("date", room.getText().toString());
                    startActivityForResult(intent, 1);
                    break;
                case id.text1:
                    Intent intent1 = new Intent(
                            Detail.this, Amend.class);
                    intent1.putExtra("id", mRowId + "");
                    intent1.putExtra("title", "姓名:");
                    intent1.putExtra("title1", "name");
                    intent1.putExtra("date", name.getText().toString());
                    startActivityForResult(intent1, 2);
                    break;
                case id.text11:
                    Intent intent11 = new Intent(
                            Detail.this, Amend.class);
                    intent11.putExtra("id", mRowId + "");
                    intent11.putExtra("title", "入住时间:");
                    intent11.putExtra("title1", "date");
                    intent11.putExtra("date", date.getText().toString());
                    startActivityForResult(intent11, 3);
                    break;
                case id.text111:
                    Intent intent111 = new Intent(
                            Detail.this, Amend.class);
                    intent111.putExtra("id", mRowId + "");
                    intent111.putExtra("title", "电话:");
                    intent111.putExtra("title1", "phone");
                    intent111.putExtra("date", phone.getText().toString());
                    startActivityForResult(intent111, 4);
                    break;
                case id.text2:
                    Intent intent2 = new Intent(
                            Detail.this, Amend.class);
                    intent2.putExtra("id", mRowId + "");
                    intent2.putExtra("title", "房租:");
                    intent2.putExtra("title1", "rent");
                    intent2.putExtra("date", rent.getText().toString());
                    startActivityForResult(intent2, 5);
                    break;
                case id.text22:
                    Intent intent22 = new Intent(
                            Detail.this, Amend.class);
                    intent22.putExtra("id", mRowId + "");
                    intent22.putExtra("title", "押金:");
                    intent22.putExtra("title1", "money");
                    intent22.putExtra("date", money.getText().toString());
                    startActivityForResult(intent22, 6);
                    break;
                case id.mark:
                    Intent intent_mark = new Intent(
                            Detail.this, Amend.class);
                    intent_mark.putExtra("id", mRowId + "");
                    intent_mark.putExtra("title", "备注:");
                    intent_mark.putExtra("title1", "mark");
                    intent_mark.putExtra("date", mark.getText().toString());
                    startActivityForResult(intent_mark, 7);
                    break;
                case id.water:
                    Intent intent_water = new Intent(
                            Detail.this, Amend.class);
                    intent_water.putExtra("id", mRowId + "");
                    intent_water.putExtra("title", "水费:");
                    intent_water.putExtra("title1", "water");
                    intent_water.putExtra("date", water.getText().toString());
                    startActivityForResult(intent_water, 8);
                    break;
                case id.electric:
                    Intent intent_electric = new Intent(
                            Detail.this, Amend.class);
                    intent_electric.putExtra("id", mRowId + "");
                    intent_electric.putExtra("title", "电费:");
                    intent_electric.putExtra("title1", "electric");
                    intent_electric.putExtra("date", electric.getText().toString());
                    startActivityForResult(intent_electric, 9);
                    break;


                case id.text_mark:
                    if (f3) {
                        mark.setVisibility(View.VISIBLE);
                        f3 = !f3;
                        image(text_mark, drawable.mark, drawable.ic_arrow_up);
                    } else {
                        mark.setVisibility(View.GONE);
                        f3 = !f3;
                        image(text_mark, drawable.mark, drawable.ic_arrow_right);
                    }
                    break;
                case id.text3:
                    if (f) {
                        water.setVisibility(View.VISIBLE);
                        f = !f;
                        image(text3, drawable.water, drawable.ic_arrow_up);
                    } else {
                        water.setVisibility(View.GONE);
                        f = !f;
                        image(text3, drawable.water, drawable.ic_arrow_right);
                    }
                    break;
                case id.text4:
                    if (f2) {
                        electric.setVisibility(View.VISIBLE);
                        f2 = !f2;
                        image(text4, drawable.electric, drawable.ic_arrow_up);
                    } else {
                        electric.setVisibility(View.GONE);
                        f2 = !f2;
                        image(text4, drawable.electric, drawable.ic_arrow_right);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    room.setText(data.getExtras().getString("data"));
                    break;
                case 2:
                    name.setText(data.getExtras().getString("data"));
                    break;
                case 3:
                    date.setText(data.getExtras().getString("data"));
                    break;
                case 4:
                    phone.setText(data.getExtras().getString("data"));
                    break;
                case 5:
                    rent.setText(data.getExtras().getString("data"));
                    break;
                case 6:
                    money.setText(data.getExtras().getString("data"));
                    break;
                case 7:
                    mark.setText(data.getExtras().getString("data"));
                    break;
                case 8:
                    water.setText(data.getExtras().getString("data"));
                    break;
                case 9:
                    electric.setText(data.getExtras().getString("data"));
                    break;
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
