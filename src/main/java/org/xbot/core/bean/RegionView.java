package org.xbot.core.bean;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by paulc on 2/22/2017.
 */
public class RegionView {
    private String region;
    private KPIView KpiView;
    private Map<String, KPIView> countries = new HashMap<String, KPIView>();
    private Map<String, KPIView> months = new LinkedHashMap<>();
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Map<String, KPIView> getCountries() {
        return countries;
    }

    public void setCountries(Map<String, KPIView> countries) {
        this.countries = countries;
    }

    public KPIView getKpiView() {
        return KpiView;
    }

    public void setKpiView(KPIView kpiView) {
        KpiView = kpiView;
    }

    public Map<String, KPIView> getMonths() {
        return months;
    }

    public void setMonths(Map<String, KPIView> months) {
        this.months = months;
    }

    @Override
    public String toString() {
        return "RegionView{" +
                "region='" + region + '\'' +
                ", KpiView=" + KpiView +
                ", countries=" + countries +
                ", months=" + months +
                '}';
    }
}
