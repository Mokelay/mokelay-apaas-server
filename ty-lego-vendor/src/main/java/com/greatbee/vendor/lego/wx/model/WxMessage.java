package com.greatbee.vendor.lego.wx.model;

/**
 * WxMessage
 *
 * * 1个消息结构
 * 微信消息结构
 *
 * @author xiaobc
 * @date 18/9/10
 */
public class WxMessage{
    private String imageUrl;//封面图地址 网络地址
    private String imageMediaId;//封面图微信 mediaId
    private String author;//作者
    private String title;//标题
    private String contentSourceUrl;//点击阅读详情 后的跳转地址，可以使appstore
    private String content;//消息内容 支持HTML标签
    private int showCoverPic;//是否显示封面

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageMediaId() {
        return imageMediaId;
    }

    public void setImageMediaId(String imageMediaId) {
        this.imageMediaId = imageMediaId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentSourceUrl() {
        return contentSourceUrl;
    }

    public void setContentSourceUrl(String contentSourceUrl) {
        this.contentSourceUrl = contentSourceUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getShowCoverPic() {
        return showCoverPic;
    }

    public void setShowCoverPic(int showCoverPic) {
        this.showCoverPic = showCoverPic;
    }
}
