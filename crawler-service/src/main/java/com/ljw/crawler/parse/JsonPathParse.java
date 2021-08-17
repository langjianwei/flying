package com.ljw.crawler.parse;

import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.JsonPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: 郎建伟
 * @Description: Json抽取
 * @Date: Created in 2020/6/30 10:55
 */
@ParseType(name = "JsonPath")
public class JsonPathParse implements Parse {


    @Override
    public String parse(String text, String path) {
        Object object = JsonPath.read(text, path);
        if (object == null) {
            return null;
        }
        if (object instanceof List) {
            List list = (List) object;
            if (list != null && list.size() > 0) {
                return toString(list.iterator().next());
            }
        }
        return object.toString();
    }

    @Override
    public List<String> parseList(String text, String path) {
        List<String> list = new ArrayList<String>();
        Object object = JsonPath.read(text, path);
        if (object == null) {
            return list;
        }
        if (object instanceof List) {
            List<Object> items = (List<Object>) object;
            for (Object item : items) {
                list.add(toString(item));
            }
        } else {
            list.add(toString(object));
        }
        return list;
    }

    private String toString(Object object) {
        if (object instanceof Map) {
            return JSON.toJSONString(object);
        } else {
            return String.valueOf(object);
        }
    }

    public static void main(String[] args) {
        String json = "{ \"store\": {" +
                "    \"book\": [" +
                "      { \"category\": \"reference\"," +
                "        \"author\": \"Nigel Rees\"," +
                "        \"title\": \"Sayings of the Century\"," +
                "        \"price\": 8.95" +
                "      }," +
                "      { \"category\": \"fiction\"," +
                "        \"author\": \"Evelyn Waugh\"," +
                "        \"title\": \"Sword of Honour\"," +
                "        \"price\": 12.99," +
                "        \"isbn\": \"0-553-21311-3\"" +
                "      }" +
                "    ]," +
                "    \"bicycle\": {" +
                "      \"color\": \"red\"," +
                "      \"price\": 19.95" +
                "    }" +
                "  }" +
                "}";
        JsonPathParse selector = new JsonPathParse();
        List<String> list = selector.parseList(json, "$.store.book");
        for (String s : list) {
            System.out.println(s);
            String select = selector.parse(s, "$.author");
            System.out.println(select);
        }
//        String result = JsonPath.read(json,"$.store.book");
//        System.out.println(result);
    }
}
