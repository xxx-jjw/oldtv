package top.xxx.tv_app.pojo;

public class Channel {
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

    public Channel(String name, String uri) {
        this.name = name;
        this.uri = uri;
    }
}
