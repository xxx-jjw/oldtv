package top.xxx.tv_app.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import top.xxx.tv_app.MainActivity;

//需要读写外部存储的权限，未写申请权限的代码，需要自己在设置中手动开启存储权限。
public class FileUtil {
    public static File _sdcard = null;

    //获取sd卡跟目录，字符串后不带"/"
    public static String getSdcardRootPath(){
        return _sdcard.getAbsolutePath();
    }

    //把文件从assets拷贝到内存卡，assets只可读
    // 目的文件名为 "/sdcard/" + fileNameInInternalStorage, 注意 “/”的有无
    public static void copyFileFromAssetsToInternalStorage(Context context, String fileNameInAssets, String fileNameInInternalStorage) {
        try {
            InputStream fis = context.getResources().getAssets().open(fileNameInAssets);
            InputStreamReader isr = new InputStreamReader(fis);
            char[] input = new char[fis.available()];  //available()用于获取filename内容的长度
            isr.read(input);  //读取并存储到input中
            isr.close();
            fis.close();//读取完成后关闭

            File file = new File(_sdcard, fileNameInInternalStorage);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(input);
            osw.flush();
            fos.flush();  //flush是为了输出缓冲区中所有的内容
            osw.close();
            fos.close();  //写入完成后，将两个输出关闭
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 实际被判断的文件名为 "/sdcard/" + filename, 注意 “/”的有无
    public static boolean hasFilenameInSdcard(String filename){
        return getFileByName(filename).exists();
    }

    public static File getFileByName(String filename){
        return new File(_sdcard, filename);
    }

    public static void writeStringToFile(String filename, String content){
        try {
            File file = FileUtil.getFileByName(filename);
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(content);
            osw.flush();
            fos.flush();  //flush是为了输出缓冲区中所有的内容
            osw.close();
            fos.close();  //写入完成后，将两个输出关闭
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String readOneLineStringFromFile(String filename){
        String str = null;
        try {
            File file = FileUtil.getFileByName(filename);
            InputStream is = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            str = br.readLine();
            is.close();
            br.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }

    //受安卓系统限制，只能一级一级地创建目录
    public static void createDir(String dirName){
        File file = FileUtil.getFileByName(dirName);
        file.mkdirs();
        System.out.println(file.getAbsoluteFile().toString()+"...");
    }
}
