package org.xbot.core.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by paulc on 2/22/2017.
 */
public class CategoryView {
    private String category;
    private KPIView KpiView;
    private Map<String, KPIView> projects = new HashMap<String, KPIView>();

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Map<String, KPIView> getProjects() {
        return projects;
    }

    public void setProjects(Map<String, KPIView> projects) {
        this.projects = projects;
    }

    public KPIView getKpiView() {
        return KpiView;
    }

    public void setKpiView(KPIView kpiView) {
        KpiView = kpiView;
    }

    @Override
    public String toString() {
        return "CategoryView{" +
                "category='" + category + '\'' +
                ", projects=" + projects +
                '}';
    }
}
