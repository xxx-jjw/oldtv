package top.xxx.tv_app;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import top.xxx.tv_app.util.FileUtil;

//单例的
public class Channel extends Observable {
    private static final String DATA_DIR = "top_xxx_old_tv";
    private static final String CHANNEL_LIST = "channel.data";
    private static final String CUR_CHANNEL_NUM = "cur_channel_num.data";

    private static Channel instance = null;
    private Channel(Context context){
        this.context = (MainActivity) context;
        initDataFiles();
        initChannel();
        setCurChannelNum(readCurChannelNumByFile());
        addObserver((MainActivity)context);
    }

    public static Channel getInstance(Context context){
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

    private List<ChannelEntry> channelEntryList = new ArrayList<ChannelEntry>();
    private int curChannelNum = -1;

    private void initChannel(){

        channelEntryList.add(new ChannelEntry("点播台", ""));
        channelEntryList.add(new ChannelEntry("戏曲台", "http://ivi.bupt.edu.cn/hls/cctv11.m3u8"));
        channelEntryList.add(new ChannelEntry("电视剧台", "http://cctvcnch5c.v.wscdns.com/live/cctv8_2/index.m3u8"));
        channelEntryList.add(new ChannelEntry("电影台", "http://ivi.bupt.edu.cn/hls/cctv6hd.m3u8"));

        readChannelByFileAndAddChannelToList();
    }

    private void initDataFiles(){//如果是首次启动应用，就把assets中的文件拷贝到内部存储，因为实际生效使用的文件是内部存储中的文件。

        if(!FileUtil.hasFilenameInSdcard(DATA_DIR))
            FileUtil.createDir(DATA_DIR);

        if(!FileUtil.hasFilenameInSdcard(DATA_DIR+"/"+CHANNEL_LIST))
            FileUtil.copyFileFromAssetsToInternalStorage(context,CHANNEL_LIST, DATA_DIR+"/"+CHANNEL_LIST);

        if(!FileUtil.hasFilenameInSdcard(DATA_DIR+"/"+CUR_CHANNEL_NUM))
            FileUtil.copyFileFromAssetsToInternalStorage(context,CUR_CHANNEL_NUM, DATA_DIR+"/"+CUR_CHANNEL_NUM);
    }

    private int readCurChannelNumByFile(){
        String curChannelNumStr = FileUtil.readOneLineStringFromFile(DATA_DIR+"/"+CUR_CHANNEL_NUM);
        System.out.println(curChannelNumStr);
        return Integer.parseInt(curChannelNumStr);
    }


    private void writeCurChannelNumToFile(){
        FileUtil.writeStringToFile(DATA_DIR+"/"+CUR_CHANNEL_NUM, curChannelNum+"");
    }


    public int getCurChannelNum() {
        return curChannelNum;
    }

    public String getCurChannelName(){
        return channelEntryList.get(curChannelNum).getName();
    }

    public void setCurChannelNum(int curChannelNum) {
        if(curChannelNum<0)
            curChannelNum=0;
        if(curChannelNum>=channelEntryList.size())
            curChannelNum=channelEntryList.size()-1;
        this.curChannelNum = curChannelNum;
        writeCurChannelNumToFile();
        context.playVideoWithUri(channelEntryList.get(curChannelNum).getUri());
        setChanged();
        notifyObservers(getCurChannelNum());
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



    public class ChannelEntry {
        String name;
        String uri;

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getUri() {
            return uri;
        }
        public void setUri(String uri) {
            this.uri = uri;
        }
        public ChannelEntry(String name, String uri) {
            this.name = name;
            this.uri = uri;
        }
    }
}
