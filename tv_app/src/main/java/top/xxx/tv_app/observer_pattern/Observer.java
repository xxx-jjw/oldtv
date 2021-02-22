package top.xxx.tv_app.observer_pattern;

public interface Observer {
//    此处吧subject放在实现Observer接口的类中作为一个成员。
//    protected Subject subject;

    public abstract void update();
}
