package com.yingzi.gmall.realtime.utils;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yingzi
 * @Date: 2022/7/25 21:45
 * @Version 1.0
 */
public class KeywordUtil {

    public static List<String> splitKeyWord(String keyWord) throws IOException {
        //创建集合用于存放结果数据
        ArrayList<String> resultList = new ArrayList<>();

        StringReader reader = new StringReader(keyWord);
        IKSegmenter ikSegmenter = new IKSegmenter(reader, false);

        while (true) {
            Lexeme next = ikSegmenter.next();
            if (next != null) {
                String word = next.getLexemeText();
                resultList.add(word);
            } else {
                break;
            }
        }

        //返回结果数据
        return resultList;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(splitKeyWord("湖南农业大学之四个公寓"));
    }
}
