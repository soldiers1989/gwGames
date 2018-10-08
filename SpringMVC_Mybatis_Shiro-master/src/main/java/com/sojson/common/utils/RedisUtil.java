package com.sojson.common.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisDataException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lx on 2018/8/21.
 */
public class RedisUtil {

    static Map<String,String> jedis = new ConcurrentHashMap<String,String>();


    /**
     * 新增
     * @param sessionId
     * @param userId
     */
    public static void save(String sessionId, String userId){
        jedis.put(sessionId, userId);
    }

    /**
     * 删除指定sessionId
     * @param sessionId
     */
    public static void delete(String sessionId){
        jedis.remove(sessionId);
    }

    /**
     * 获取redis中所有键值对
     * @return map
     */
    public static Map<String,String> getAll(){
        Map<String,String> map = new HashMap<String,String>();

        Set<String> keys = jedis.keySet();
        for(String key : keys){
            String val = jedis.get(key);
            map.put(key, val);
        }

        return map;
    }

    public static String get(String key){
        String value = "";
        try {
            value = jedis.get(key);
        }catch (Exception e){
            return "";
        }
        return value;
    }

    public static BufferedImage drawTranslucentStringPic(int width, int height, Integer fontHeight, String drawStr)
    {
        try
        {
            BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D gd = buffImg.createGraphics();
            //设置透明  start
            buffImg = gd.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
            gd=buffImg.createGraphics();
            //设置透明  end
            gd.setFont(new Font("微软雅黑", Font.PLAIN, fontHeight)); //设置字体
            gd.setColor(Color.ORANGE); //设置颜色
            gd.drawRect(0, 0, width - 1, height - 1); //画边框
            gd.drawString(drawStr, width/2-fontHeight*drawStr.length()/2,fontHeight); //输出文字（中文横向居中）
            return buffImg;
        } catch (Exception e) {
            return null;
        }
    }



//    public static void main(String[] args) {
//        BufferedImage imgMap = drawTranslucentStringPic(400, 80, 36,"AT123321312");
//        File imgFile=new File("D://aaa.png");
//        try
//        {
//            ImageIO.write(imgMap, "PNG", imgFile);
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//        System.out.println("生成完成");
//    }



}
