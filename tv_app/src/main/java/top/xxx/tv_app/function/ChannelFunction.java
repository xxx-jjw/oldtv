package top.xxx.tv_app.function;

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
import java.util.ArrayList;
import java.util.List;

import top.xxx.tv_app.MainActivity;
import top.xxx.tv_app.pojo.Channel;
import top.xxx.tv_app.util.FileUtil;

public class ChannelFunction {

    private static final String DATA_DIR = "top_xxx_old_tv";
    private static final String CHANNEL_LIST = "channel.data";
    private static final String CUR_CHANNEL_NUM = "cur_channel_num.data";

    private static List<Channel> channelList = new ArrayList<Channel>();
    private static int curChannelNum = -1;


    public static void initChannel(MainActivity context){
        initDataFiles(context);

        channelList.add(new Channel("点播台", ""));
        channelList.add(new Channel("戏曲台", "http://ivi.bupt.edu.cn/hls/cctv11.m3u8"));
        channelList.add(new Channel("电视剧台", "http://cctvcnch5c.v.wscdns.com/live/cctv8_2/index.m3u8"));
        channelList.add(new Channel("电影台", "http://ivi.bupt.edu.cn/hls/cctv6hd.m3u8"));

        readChannelByFileAndAddChannelToList();

        ChannelFunction.readCurChannelNumByFile();

        changeChannel(context, curChannelNum);

    }

    private static void initDataFiles(Context context){//如果是首次启动应用，就把assets中的文件拷贝到内部存储，因为实际生效使用的文件是内部存储中的文件。

        if(!FileUtil.hasFilenameInSdcard(DATA_DIR))
            FileUtil.createDir(DATA_DIR);

        if(!FileUtil.hasFilenameInSdcard(DATA_DIR+"/"+CHANNEL_LIST))
            FileUtil.copyFileFromAssetsToInternalStorage(context,CHANNEL_LIST, DATA_DIR+"/"+CHANNEL_LIST);

        if(!FileUtil.hasFilenameInSdcard(DATA_DIR+"/"+CUR_CHANNEL_NUM))
            FileUtil.copyFileFromAssetsToInternalStorage(context,CUR_CHANNEL_NUM, DATA_DIR+"/"+CUR_CHANNEL_NUM);
    }

    private static void readChannelByFileAndAddChannelToList(){
        try {
            File file = FileUtil.getFileByName(DATA_DIR+"/"+CHANNEL_LIST);
            System.out.println(file.getAbsolutePath());
            InputStream is = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String str = null;
            while ((str = br.readLine()) != null) {
                System.out.println(str);
                String[] temp = str.split(" ");
                channelList.add(new Channel(temp[0], temp[1]));
            }
            is.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void readCurChannelNumByFile(){
        String curChannelNumStr = FileUtil.readOneLineStringFromFile(DATA_DIR+"/"+CUR_CHANNEL_NUM);
        System.out.println(curChannelNumStr);
        curChannelNum = Integer.parseInt(curChannelNumStr);
    }

    //isAdd为true时频道+，否则频道-
    public static void changeChannel(MainActivity context, boolean isAdd){
        if(isAdd)
            curChannelNum = (curChannelNum+1)%channelList.size();
        else
            curChannelNum = (curChannelNum-1+channelList.size())%channelList.size();
        context.playWithUriStr(channelList.get(curChannelNum).getUri());
        writeCurChannelNumToFile();
    }

    //更改频道为2参
    public static void changeChannel(MainActivity context, int channelNum){
        if(curChannelNum<channelList.size()&&curChannelNum>=0)
            context.playWithUriStr(channelList.get(curChannelNum).getUri());
        else {
//            playWithUriStr(channelList.get(4).getUri());
//            paraCurChannelNum = 4;
        }
        writeCurChannelNumToFile();
    }

    private static void writeCurChannelNumToFile(){
        FileUtil.writeStringToFile(DATA_DIR+"/"+CUR_CHANNEL_NUM, curChannelNum+"");
    }


}
