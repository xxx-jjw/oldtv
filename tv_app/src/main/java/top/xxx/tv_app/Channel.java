package top.xxx.tv_app;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import top.xxx.tv_app.pojo.ChannelEntry;
import top.xxx.tv_app.util.FileUtil;

//单例的
public class Channel {

    private static Channel instance = null;
    private Channel(MainActivity context){
        this.context = context;
        initDataFiles();
        initChannel();
    }
    public static Channel getInstance(MainActivity context){
        if(null==instance) {
            synchronized (Channel.class) {
                if (null == instance) {
                    instance = new Channel(context);
                }
            }
        }
        return instance;
    }

    private MainActivity context = null;

    private static final String DATA_DIR = "top_xxx_old_tv";
    private static final String CHANNEL_LIST = "channel.data";
    private static final String CUR_CHANNEL_NUM = "cur_channel_num.data";

    private List<ChannelEntry> channelEntryList = new ArrayList<ChannelEntry>();
    private int curChannelNum = -1;

    private void initChannel(){

        channelEntryList.add(new ChannelEntry("点播台", ""));
        channelEntryList.add(new ChannelEntry("戏曲台", "http://ivi.bupt.edu.cn/hls/cctv11.m3u8"));
        channelEntryList.add(new ChannelEntry("电视剧台", "http://cctvcnch5c.v.wscdns.com/live/cctv8_2/index.m3u8"));
        channelEntryList.add(new ChannelEntry("电影台", "http://ivi.bupt.edu.cn/hls/cctv6hd.m3u8"));

        readChannelByFileAndAddChannelToList();

        readCurChannelNumByFile();

        changeChannel(curChannelNum);
    }

    private void initDataFiles(){//如果是首次启动应用，就把assets中的文件拷贝到内部存储，因为实际生效使用的文件是内部存储中的文件。

        if(!FileUtil.hasFilenameInSdcard(DATA_DIR))
            FileUtil.createDir(DATA_DIR);

        if(!FileUtil.hasFilenameInSdcard(DATA_DIR+"/"+CHANNEL_LIST))
            FileUtil.copyFileFromAssetsToInternalStorage(context,CHANNEL_LIST, DATA_DIR+"/"+CHANNEL_LIST);

        if(!FileUtil.hasFilenameInSdcard(DATA_DIR+"/"+CUR_CHANNEL_NUM))
            FileUtil.copyFileFromAssetsToInternalStorage(context,CUR_CHANNEL_NUM, DATA_DIR+"/"+CUR_CHANNEL_NUM);
    }

    private void readChannelByFileAndAddChannelToList(){
        try {
            File file = FileUtil.getFileByName(DATA_DIR+"/"+CHANNEL_LIST);
            System.out.println(file.getAbsolutePath());
            InputStream is = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String str = null;
            while ((str = br.readLine()) != null) {
                System.out.println(str);
                String[] temp = str.split(" ");
                channelEntryList.add(new ChannelEntry(temp[0], temp[1]));
            }
            is.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readCurChannelNumByFile(){
        String curChannelNumStr = FileUtil.readOneLineStringFromFile(DATA_DIR+"/"+CUR_CHANNEL_NUM);
        System.out.println(curChannelNumStr);
        curChannelNum = Integer.parseInt(curChannelNumStr);
    }

    //isAdd为true时频道+，否则频道-
    public void changeChannel(boolean isAdd){
        if(isAdd)
            curChannelNum = (curChannelNum+1)% channelEntryList.size();
        else
            curChannelNum = (curChannelNum-1+ channelEntryList.size())% channelEntryList.size();
        context.playWithUriStr(channelEntryList.get(curChannelNum).getUri());
        writeCurChannelNumToFile();
    }

    //更改频道为2参
    public void changeChannel(int channelNum){
        if(curChannelNum< channelEntryList.size()&&curChannelNum>=0)
            context.playWithUriStr(channelEntryList.get(curChannelNum).getUri());
        else {
//            playWithUriStr(channelList.get(4).getUri());
//            paraCurChannelNum = 4;
        }
        writeCurChannelNumToFile();
    }

    private void writeCurChannelNumToFile(){
        FileUtil.writeStringToFile(DATA_DIR+"/"+CUR_CHANNEL_NUM, curChannelNum+"");
    }


}
