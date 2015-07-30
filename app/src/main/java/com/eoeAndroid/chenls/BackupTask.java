package com.eoeAndroid.chenls;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class BackupTask extends AsyncTask<String, Void, String> {
    private static final String COMMAND_BACKUP = "backupDatabase";
    public static final String COMMAND_RESTORE = "restroeDatabase";
    private Context mContext;
    private String result = "";

    public BackupTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub
        // 获得正在使用的数据库路径，我的是 sdcard 目录下的 /dlion/db_dlion.db
        // 默认路径是 /data/data/(包名)/databases/*.db
//        File dbFile = mContext.getDatabasePath(Environment
//                .getExternalStorageDirectory().getAbsolutePath()
//                + "/dlion/db_dlion.db");
        File dbFile = mContext.getDatabasePath("database");
        File exportDir = new File(Environment.getExternalStorageDirectory(),
                "ZUKE-Backup");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File backup = new File(exportDir, dbFile.getName());
        String command = params[0];
        if (command.equals(COMMAND_BACKUP)) {
            try {
                backup.createNewFile();
                fileCopy(dbFile, backup);
                result = "备份成功";
                return result;
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                result = "备份失败";
                return result;
            }
        } else if (command.equals(COMMAND_RESTORE)) {
            try {
                fileCopy(backup, dbFile);
                result = "恢复成功";
                return result;
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                result = "恢复失败";
                return result;
            }
        } else {
            return null;
        }
    }

    private void fileCopy(File dbFile, File backup) throws IOException {
        // TODO Auto-generated method stub
        FileChannel inChannel = new FileInputStream(dbFile).getChannel();
        FileChannel outChannel = new FileOutputStream(backup).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

    //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
        ActivityMain.refresh();

    }
}