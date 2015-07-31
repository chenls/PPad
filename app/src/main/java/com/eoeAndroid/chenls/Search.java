package com.eoeAndroid.chenls;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Search extends ListActivity {
    private ImageView ivDeleteText;
    private EditText etSearch;
    private TextView btnSearch;
    private long index_id = 0;// 长按删除指定数据的索引
    private DiaryDbAdapter mDbHelper;
    private Cursor mDiaryCursor;
    private static final int ACTIVITY_EDIT = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mDbHelper = new DiaryDbAdapter(this);
        mDbHelper.open();
        ivDeleteText = (ImageView) findViewById(R.id.ivDeleteText);
        etSearch = (EditText) findViewById(R.id.etSearch);
        btnSearch = (TextView) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new TextOnClickListenerImpl());
        ivDeleteText.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                etSearch.setText("");
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                renderListView(etSearch.getText().toString());
                if (s.length() == 0) {
                    ivDeleteText.setVisibility(View.GONE);
                } else {
                    ivDeleteText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    // 按钮点击事件
    private class TextOnClickListenerImpl implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnSearch:
                    finish();
                    overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_buttom);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 读取数据库数据，展示在list中
     */
    private void renderListView(String data) {
        mDiaryCursor = mDbHelper.getDiary(data);
        startManagingCursor(mDiaryCursor);
        String[] from = new String[]{DiaryDbAdapter.KEY_FRIST_NAME, DiaryDbAdapter.KEY_ROOM, DiaryDbAdapter.KEY_NAME,
                DiaryDbAdapter.KEY_DATE};

        int[] to = new int[]{R.id.image, R.id.text1, R.id.text2, R.id.text3};
        SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
                R.layout.listview_item, mDiaryCursor, from, to);
        setListAdapter(notes);
        final ListView lv;
        lv = getListView();
        lv.setFocusable(false);
        // position指的是点击的这个ViewItem在当前ListView中的位置
        // 每一个和ViewItem绑定的数据，肯定都有一个id，通过这个id可以找到那条数据。
        // 需要对position和id进行一个很好的区分

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Cursor c = mDiaryCursor;
                c.moveToPosition(position);
                Intent i = new Intent(Search.this, Detail.class);
                i.putExtra(DiaryDbAdapter.KEY_ROWID, id);
                i.putExtra(DiaryDbAdapter.KEY_ROOM, c.getString(c.getColumnIndexOrThrow(DiaryDbAdapter.KEY_ROOM)));
                i.putExtra(DiaryDbAdapter.KEY_NAME, c.getString(c.getColumnIndexOrThrow(DiaryDbAdapter.KEY_NAME)));
                i.putExtra(DiaryDbAdapter.KEY_DATE, c.getString(c.getColumnIndexOrThrow(DiaryDbAdapter.KEY_DATE)));
                i.putExtra(DiaryDbAdapter.KEY_PHONE, c.getString(c.getColumnIndexOrThrow(DiaryDbAdapter.KEY_PHONE)));
                i.putExtra(DiaryDbAdapter.KEY_RENT, c.getString(c.getColumnIndexOrThrow(DiaryDbAdapter.KEY_RENT)));
                i.putExtra(DiaryDbAdapter.KEY_MONEY, c.getString(c.getColumnIndexOrThrow(DiaryDbAdapter.KEY_MONEY)));
                i.putExtra(DiaryDbAdapter.KEY_MARK, c.getString(c.getColumnIndexOrThrow(DiaryDbAdapter.KEY_MARK)));
                i.putExtra(DiaryDbAdapter.KEY_WATER, c.getString(c.getColumnIndexOrThrow(DiaryDbAdapter.KEY_WATER)));
                i.putExtra(DiaryDbAdapter.KEY_ELECTRIC, c.getString(c.getColumnIndexOrThrow(DiaryDbAdapter.KEY_ELECTRIC)));
                startActivityForResult(i, ACTIVITY_EDIT);
            }
        });
        registerForContextMenu(lv);
        // 添加长按点击,得到点中的index，即参数arg2
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(
                    AdapterView<?> arg0, View arg1,
                    int arg2, long arg3) {
                // TODO Auto-generated method stub
                index_id = arg3;
                return false;
            }
        });
    }

    @Override
    public void onCreateContextMenu(
            ContextMenu menu, View v,
            ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("确定删除？");
        menu.add(0, 0, 0, "删除");
        menu.add(0, 1, 0, "取消");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    /**
     * 长按菜单响应函数
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {
            boolean f = mDbHelper.deleteDiary(index_id);
            if (f) {
                Toast.makeText(Search.this, "删除成功", Toast.LENGTH_SHORT).show();
                renderListView(etSearch.getText().toString());//重新加载数据
            }
        }
        return super.onContextItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public boolean onKeyDown(int keyCode, KeyEvent event)//主要是对这个函数的复写
    {
        // TODO Auto-generated method stub

        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
            finish();
            overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_buttom);
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

}
