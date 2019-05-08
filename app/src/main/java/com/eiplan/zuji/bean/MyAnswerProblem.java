package com.eiplan.zuji.bean;

/**
 * 我回答的问题bean类
 */
public class MyAnswerProblem {
    private String problemName;//问题名称
    private int problemPoint;//问题积分
    private ProblemCommentInfo problemCommentInfo;

    public MyAnswerProblem() {
    }

    public MyAnswerProblem(String problemName, ProblemCommentInfo problemCommentInfo) {
        this.problemName = problemName;
        this.problemCommentInfo = problemCommentInfo;
    }

    public String getProblemName() {
        return problemName;
    }


    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public ProblemCommentInfo getProblemCommentInfo() {
        return problemCommentInfo;
    }

    public void setProblemCommentInfo(ProblemCommentInfo problemCommentInfo) {
        this.problemCommentInfo = problemCommentInfo;
    }

    public int getProblemPoint() {
        return problemPoint;
    }

    public void setProblemPoint(int problemPoint) {
        this.problemPoint = problemPoint;
    }
}
