package e.kja.report02_01_20160943;

import java.io.Serializable;

public class NaverBlogDto implements Serializable { //해당 맛집 블로그 정보를 위한 dto

    private int _Id;
    private String blogtitle;
    private String bloggername;
    private String bloggerlink;


    public String getBlogtitle() { return blogtitle; }

    public void setBlogtitle(String blogtitle) { this.blogtitle = blogtitle; }

    public int get_Id() { return _Id; }

    public void set_Id(int _id) {this._Id = _id;}

    public String getBloggername() { return bloggername; }

    public void setBloggername(String bloggername) { this.bloggername = bloggername; }

    public String getBloggerlink() { return bloggerlink; }

    public void setBloggerlink(String bloggerlink) { this.bloggerlink = bloggerlink; }
}
