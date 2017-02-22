package org.xbot.core.bean;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * Created by paulc on 2/22/2017.
 */
public class KPIView {
    private long manualExecutionTime;//in minutes
    private long autoExecutionTime;//in minutes
    private int testcaseNumber;

    private double successRate;
    private int successTestcaseNumber;
    private List<NameValuePair> teamConfidence;
    private List<NameValuePair> defectNumner;

    public long getManualExecutionTime() {
        return manualExecutionTime;
    }

    public void setManualExecutionTime(long manualExecutionTime) {
        this.manualExecutionTime = manualExecutionTime;
    }

    public long getAutoExecutionTime() {
        return autoExecutionTime;
    }

    public void setAutoExecutionTime(long autoExecutionTime) {
        this.autoExecutionTime = autoExecutionTime;
    }

    public int getTestcaseNumber() {
        return testcaseNumber;
    }

    public void setTestcaseNumber(int testcaseNumber) {
        this.testcaseNumber = testcaseNumber;
    }

    public List<NameValuePair> getTeamConfidence() {
        return teamConfidence;
    }

    public void setTeamConfidence(List<NameValuePair> teamConfidence) {
        this.teamConfidence = teamConfidence;
    }

    public List<NameValuePair> getDefectNumner() {
        return defectNumner;
    }

    public void setDefectNumner(List<NameValuePair> defectNumner) {
        this.defectNumner = defectNumner;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public int getSuccessTestcaseNumber() {
        return successTestcaseNumber;
    }

    public void setSuccessTestcaseNumber(int successTestcaseNumber) {
        this.successTestcaseNumber = successTestcaseNumber;
    }

    public void setSuccessRate(double successRate) {
        this.successRate = successRate;
    }

    @Override
    public String toString() {
        return "KPIView{" +
                "manualExecutionTime=" + manualExecutionTime +
                ", autoExecutionTime=" + autoExecutionTime +
                ", testcaseNumber=" + testcaseNumber +
                ", successRate=" + successRate +
                ", successTestcaseNumber=" + successTestcaseNumber +
                ", teamConfidence=" + teamConfidence +
                ", defectNumner=" + defectNumner +
                '}';
    }
}
