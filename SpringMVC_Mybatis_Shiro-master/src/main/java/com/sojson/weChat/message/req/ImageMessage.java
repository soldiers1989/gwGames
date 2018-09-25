package com.sojson.weChat.message.req;

/**
 * ClassName: ImageMessage
 * @Description: 图片消息
 * @author hwj
 */
public class ImageMessage extends BaseMessage {

    // 图片链接
    private String PicUrl;

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }
}
