package com.eiplan.zuji.bean;

import java.util.List;

public class EventExpert {
    private List<ExpertInfo> experts;

    public EventExpert(List<ExpertInfo> experts) {
        this.experts = experts;
    }

    public List<ExpertInfo> getExperts() {
        return experts;
    }

    public void setExperts(List<ExpertInfo> experts) {
        this.experts = experts;
    }
}
