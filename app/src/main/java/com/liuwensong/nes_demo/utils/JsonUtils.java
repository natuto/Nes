package com.liuwensong.nes_demo.utils;

import android.util.Log;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.liuwensong.nes_demo.bean.NewsBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by luo-pc on 2016/5/26.
 */
public class JsonUtils {
    static final String TAG = "JsonUtils";
    /**
     * 将获取到的json转换为新闻列表对象
     *
     * @param res
     * @param value
     * @return
     */
    public static ArrayList<NewsBean> readJsonNewsBean(String res, String value) {

        ArrayList<NewsBean> beans = new ArrayList<NewsBean>();
        try {
            JsonParser parser = new JsonParser();
            JsonObject jsonObj = parser.parse(res).getAsJsonObject();
            JsonElement jsonElement = jsonObj.get(value);
            if (jsonElement == null) {
                return null;
            }
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (int i = 1; i < jsonArray.size(); i++) {
                JsonObject jo = jsonArray.get(i).getAsJsonObject();
                if (jo.has("skipType") && "special".equals(jo.get("skipType").getAsString())) {
                    continue;
                }
                if (jo.has("TAGS") && !jo.has("TAG")) {
                    continue;
                }


                if (!jo.has("imgextra") && jo.has("url")) {
                    NewsBean news = GsonUtils.deserialize(jo, NewsBean.class);
                    beans.add(news);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        return beans;
    }

    /**
     * 因为weather接口返回的字段比较奇怪，所以采用Android自带的Json工具解析
     */




}
