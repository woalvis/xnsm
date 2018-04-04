package tech.xinong.xnsm.views.entity;

/**
 * Created by xiao on 2018/3/7.
 */

public class Navigation {
    private int position;
    private String url;
    private String title;


    public Navigation(int position, String url, String title) {
        this.position = position;
        this.url = url;
        title = title;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
