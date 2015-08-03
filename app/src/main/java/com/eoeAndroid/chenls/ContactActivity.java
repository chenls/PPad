package com.eoeAndroid.chenls;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author test 调用通讯录公用界面
 */
public class ContactActivity extends Activity {

    private String usernumber;
    private List<String> list;

    protected EditText edtSelectedContact;

    /**
     * 启动通讯录界面
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public void startContact() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 0);
        list = new ArrayList<String>();
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 0) {
            return;
        }
        if (resultCode == Activity.RESULT_OK) {
            ContentResolver reContentResolverol = getContentResolver();
            Uri contactData = data.getData();
            @SuppressWarnings("deprecation")
            Cursor cursor = managedQuery(contactData, null, null, null, null);
            cursor.moveToFirst();
            String contactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = reContentResolverol.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                            + contactId, null, null);
            while (phone.moveToNext()) {
                //获取联系人（字符串）
                usernumber = phone
                        .getString(phone
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                list.add(splitePhoneNumber(usernumber));
            }
            getNumber(list);
            list = null;
            phone.close();
        }
    }

    /**
     * 获取并显示手机号码
     *
     * @param list
     */
    public void getNumber(List<String> list) {
        if (list != null && list.size() > 0) {
            try {
                // 循环遍历判断是否是手机号码
                for (String str : list) {
                    if (!isMobileNO(str)) {
                        list.remove(str);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            int size = list.size();
            if (size >= 1) {// 该联系人下只有一个电话号码
                // edt_input.setText(list.toString());
                for (String str : list) {
                    edtSelectedContact.setText(str);
                    // 设置光标位置
                    edtSelectedContact.setSelection(str.length());
                }
            } else {
                Toast.makeText(ContactActivity.this, "请选择正确的手机号码", Toast.LENGTH_SHORT).show();
                edtSelectedContact.setText("");
            }
        } else {
            Toast.makeText(ContactActivity.this, "该联系人下没有手机号码", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 截取手机号码 用户手机号码前加了17951或者86
     *
     * @param number
     * @return
     */
    private String splitePhoneNumber(String number) {
        String mobile = "";
        number = number.replaceAll(" ", "");
        if (number != null && number.length() >= 11) {
            mobile = number.substring(number.length() - 11, number.length());

        }
        return mobile;
    }

    /**
     * 验证手机号码格式是否正确
     *
     * @param mobiles
     * @return
     */
    private boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

}
