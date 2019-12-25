package com.example.ihelp.SafetyKnowledge.Model;

import java.util.List;

/**
 * Created by 朱继涛 2019/11/2
 * 和json对应的实体类，用于祝转换json数据
 */

public class Information {
    /*
    {
        "title": "消防安全知识",
        "content": "",
        "more": "https://baijiahao.baidu.com/s?id=1629666573028406797&wfr=spider&for=pc"
    }
     */

    private String title;
    private String more;
    private String content;

    public String getMore() {
        return more;
    }

    public String getTitle() {
        return title;
    }

    public void setMore(String more) {
        this.more = more;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
