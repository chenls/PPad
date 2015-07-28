package com.eoeAndroid.chenls;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.views.PgyerDialog;

import pgyer.Constants;

/**
 * @author jinyan
 * @download http://www.codefans.net
 */
public class ActivityMain extends ListActivity {
    private static final int ACTIVITY_EDIT = 1;
    private DiaryDbAdapter mDbHelper;
    private Cursor mDiaryCursor;
    private Button add;
    private long index_id = 0;// 长按删除指定数据的索引
    private RadioGroup bt_rg;
    private String title_data = "room";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_list);
        mDbHelper = new DiaryDbAdapter(this);
        mDbHelper.open();
        renderListView(title_data);
        add = (Button) this.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ActivityMain.this, Add_data.class);
                startActivity(intent);
            }
        });
        PgyerDialog.setDialogTitleBackgroundColor("#ff329de2");
        PgyerDialog.setDialogTitleTextColor("#ffffff");
        // 版本检测方式1：无更新回调
        PgyUpdateManager.register(this, Constants.APPID);
        //根据ID找到RadioGroup实例
        bt_rg = (RadioGroup) this.findViewById(R.id.bt_rg);
        //绑定一个匿名监听器
        bt_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                //获取变更后的选中项的ID
                int radioButtonId = arg0.getCheckedRadioButtonId();
                //根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton) ActivityMain.this.findViewById(radioButtonId);
                if (rb.getText().toString().equals("房间")) {
                    title_data = "room";
                } else {
                    title_data = "time";
                }
                renderListView(title_data);
            }
        });
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        PgyFeedbackShakeManager.register(ActivityMain.this, Constants.APPID);
        renderListView(title_data);//重新加载数据
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        PgyFeedbackShakeManager.unregister();
    }

    /**
     * 读取数据库数据，展示在list中
     */
    private void renderListView(String data) {
        mDiaryCursor = mDbHelper.getAllNotes(data);
//        mDiaryCursor = mDbHelper.getDiary("兔女");
        startManagingCursor(mDiaryCursor);
        String[] from = new String[]{DiaryDbAdapter.KEY_ROOM, DiaryDbAdapter.KEY_NAME,
                DiaryDbAdapter.KEY_DATE};

        int[] to = new int[]{R.id.text1, R.id.text2, R.id.text3};
        SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
                R.layout.listview_item, mDiaryCursor, from, to);
        setListAdapter(notes);
        final ListView lv;
        lv = getListView();
        setListViewHeight(lv);
        lv.setFocusable(false);
        // position指的是点击的这个ViewItem在当前ListView中的位置
        // 每一个和ViewItem绑定的数据，肯定都有一个id，通过这个id可以找到那条数据。
        // 需要对position和id进行一个很好的区分

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Cursor c = mDiaryCursor;
                c.moveToPosition(position);
                Intent i = new Intent(ActivityMain.this, Detail.class);
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
                Toast.makeText(ActivityMain.this, "删除成功", Toast.LENGTH_SHORT).show();
                renderListView(title_data);//重新加载数据
            }
        }
        return super.onContextItemSelected(item);
    }

    /**
     * 重新计算ListView的高度，解决ScrollView和ListView两个View都有滚动的效果，在嵌套使用时起冲突的问题
     *
     * @param listView
     */
    public void setListViewHeight(ListView listView) {

        // 获取ListView对应的Adapter

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    // 退出提示
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(ActivityMain.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
