package com.ljw.crawler.parse;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * @Description: 正则抽取
 * @Author: langjianwei
 * @DateTime: 2021/6/9 15:52
 * @Version: 1.0.0
 */
@ParseType(name = "Regex")
public class RegexParse implements Parse {

    @Override
    public <T> T parse(String text, String path) {
        Matcher matcher = compile(path).matcher(text);
        while (matcher.find()) {
            if (matcher.groupCount() > 0) {
                return (T) matcher.group(1);
            }
            return (T) matcher.group(0);
        }
        return (T) "";
    }

    @Override
    public <T> List<T> parseList(String text, String path) {
        return null;
    }

    public static void main(String[] args) {
        final String regex = "<title>(.*?)</title>";
        final String string = "\n"
                + "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"utf-8\"><meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\"><meta name=\"viewport\" content=\"width=device-width,initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no\"/><meta name=\"referrer\" content=\"origin\"><title>阳光城潜入广州茶滘旧改 重返华南背后的大湾区野望</title><link r";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(string);

        while (matcher.find()) {
            System.out.println("完整匹配: " + matcher.group(0));
            for (int i = 1; i <= matcher.groupCount(); i++) {
                System.out.println("组 " + i + ": " + matcher.group(i));
            }
        }
    }
}
