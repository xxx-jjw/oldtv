#old tv

[toc]

#### 角色1：电视端app。
    
    功能：
    1. 注册为桌面软件，电视开机后自动打开本软件。
    2. 频道0为点播频道，切换到频道0时，看电视者给协助者打电话，说我要看 xxx电影/戏曲/电视剧。看电视者发送指令控制电视0频道播放指定节目。
    3. 频道1为戏曲频道，轮播本地预设戏曲。/或直接访问公开api。
    4. 频道2为电视剧频道，轮播本地预设电视剧。/或直接访问公开api。
    5. 频道3为电影频道，轮播本地预设电影。/或直接访问公开api。
    6. 其余频道为电视台直播频道，其数据源为公开api。
    7. 电视具有守护程序，可接收并执行协助者指令，以播放看电视者要求协助者指定的节目/频道，或处理预期之外的情况。
        预设守护程序流程
            1）协助者把操作编码为短信
            2）发送给看电视者
            3）看电视者的遥控器app接收到短信，解码为指令
            4）遥控器app把解码后的指令发给tv
            5）tv执行指令，处理例外情况。
            注：把短信解码为指令的操作视情况可交给遥控器app或电视端app。
            
    附注：
    应添加转到系统桌面功能。
    
#### 角色2：遥控器app，看电视者(老人)使用。
    
    界面：按钮0-9，+/-频道，+/-音量
    
    功能：
    1. 更改频道/音量。
    2. 通过短信接收协助者发送的指令，并解码后(或不解码直接)发送给tv端app。
    
    附注：
    应添加组合键，可转到系统桌面
    
#### 角色3：协助者，一般是老人(看电视者)的家人，且由于各种原因不在老人身边。
    
    功能：
    1. 看电视者想看指定电影/电视剧/戏曲时，远程发送指令控制电视播放指定节目。
    2. 看电视者想看指定频道时，远程发送指令控制电视播放指定频道。
    3. 电视播放直播的公开api(url)失效时，远程发送指令控制电视更换正确url。
    
    附加：
    出现问题时发送指令协助解决问题。