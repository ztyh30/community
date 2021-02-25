package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author ztyh
 * @Description 敏感词过滤器工具类
 * @Date 2021/2/23 20:32
 */
@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    //敏感词替换词
    private static final String REPLACE = "***";

    private TrieNode rootNode = new TrieNode();

    //初始化敏感词前缀树
    @PostConstruct
    public void init() {
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is))
        ) {
            String word;
            while ((word = reader.readLine()) != null) {
                addWord(word);
            }
        } catch (Exception e) {
            logger.error("读取文件异常 ：{}", e.getMessage());
        }
    }

    //将一个敏感词加入到前缀树中
    public void addWord(String word) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (tempNode.getChildNode(c) == null) {
                tempNode.addChildNode(c);
            }
            tempNode = tempNode.getChildNode(c);
            if (i == word.length() - 1) {
                tempNode.setEnd(true);
            }
        }
    }

    /**
     * @param text，输入文本
     * @return 过滤后的文本
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        TrieNode trieNode = rootNode;
        int head = 0;
        int position = 0;
        while (head < text.length()) {
            char c = text.charAt(position);
            if (isSymbol(c)) {
                if (trieNode == rootNode) {
                    sb.append(c);
                    head++;
                    position++;
                    continue;
                } else {
                    position++;
                    continue;
                }
            }
            if (trieNode.getChildNode(c) != null) {
                trieNode = trieNode.getChildNode(c);
                if (trieNode.isEnd) {
                    sb.append(REPLACE);
                    head = ++position;
                    trieNode = rootNode;
                } else {
                    position++;
                }
            } else {
                sb.append(text.substring(head, position + 1));
                head = ++position;
                trieNode = rootNode;
            }
        }
        return sb.toString();
    }

    //判断是否为特殊字符
    public boolean isSymbol(Character c) {
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    //内部类，前缀树数据结构
    private class TrieNode {

        //当前节点是否为叶子节点
        private boolean isEnd = false;
        //子节点
        private Map<Character, TrieNode> childNodes = new HashMap<>();

        public boolean isEnd() {
            return isEnd;
        }

        public void setEnd(boolean end) {
            isEnd = end;
        }

        public void addChildNode(Character c) {
            childNodes.put(c, new TrieNode());
        }

        public TrieNode getChildNode(Character c) {
            return childNodes.get(c);
        }
    }
}
