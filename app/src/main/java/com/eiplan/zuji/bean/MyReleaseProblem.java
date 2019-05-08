package com.eiplan.zuji.bean;

/**
 * 我发布问题的bean类
 */
public class MyReleaseProblem {
    private ProblemInfo problemInfo;
    private String answer;

    public MyReleaseProblem() {
    }

    public MyReleaseProblem(ProblemInfo problemInfo, String answer) {
        this.problemInfo = problemInfo;
        this.answer = answer;
    }

    public ProblemInfo getProblemInfo() {
        return problemInfo;
    }

    public void setProblemInfo(ProblemInfo problemInfo) {
        this.problemInfo = problemInfo;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
