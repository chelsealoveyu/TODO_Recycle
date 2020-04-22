package com.joseph.todo_recycle;

import java.io.Serializable;

public class Memo implements Serializable {

    int seq;
    String matintext;   // 메모 내용
    String subtext;     // 날짜
    int isdone;         // 완료 여부



    public Memo(int seq, String matintext, String subtext, int isdone) {
        this.seq = seq;
        this.matintext = matintext;
        this.subtext = subtext;
        this.isdone = isdone;
    }

    public Memo(String maintext, String subtext, int isdone) {
        this.matintext = maintext;
        this.subtext = subtext;
        this.isdone = isdone;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getMatintext() {
        return matintext;
    }

    public void setMatintext(String matintext) {
        this.matintext = matintext;
    }

    public String getSubtext() {
        return subtext;
    }

    public void setSubtext(String subtext) {
        this.subtext = subtext;
    }

    public int getIsdone() {
        return isdone;
    }

    public void setIsdone(int isdone) {
        this.isdone = isdone;
    }


}
