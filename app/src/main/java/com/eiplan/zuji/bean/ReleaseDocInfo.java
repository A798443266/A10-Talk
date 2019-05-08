package com.eiplan.zuji.bean;

/**
 * 发布文档的bean类  有已发布和审核中两种类型
 */
public class ReleaseDocInfo {
    private DocInfo docInfo;
    private int type; //1 已发布  0审核中

    public ReleaseDocInfo() {
    }

    public ReleaseDocInfo(DocInfo docInfo, int type) {
        this.docInfo = docInfo;
        this.type = type;
    }

    public DocInfo getDocInfo() {
        return docInfo;
    }

    public void setDocInfo(DocInfo docInfo) {
        this.docInfo = docInfo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
