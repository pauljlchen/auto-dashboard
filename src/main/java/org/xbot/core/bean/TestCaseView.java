package org.xbot.core.bean;

import org.apache.http.NameValuePair;
import org.xbot.core.dao.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by paulc on 2/22/2017.
 */
public class TestCaseView {
    private String projectName;
    private String PODName;
    private Integer testcaseNumber = 0;
    private Map<String, Test> executedTestcases = new HashMap<String, Test>();//this is for

    public Map<String, Test> getExecutedTestcases() {
        return executedTestcases;
    }

    public void setExecutedTestcases(Map<String, Test> executedTestcases) {
        this.executedTestcases = executedTestcases;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPODName() {
        return PODName;
    }

    public void setPODName(String PODName) {
        this.PODName = PODName;
    }

    public Integer getTestcaseNumber() {
        return testcaseNumber;
    }

    public void setTestcaseNumber(Integer testcaseNumber) {
        this.testcaseNumber = testcaseNumber;
    }

    @Override
    public String toString() {
        return "TestCaseView{" +
                "projectName='" + projectName + '\'' +
                ", PODName='" + PODName + '\'' +
                ", testcaseNumber=" + testcaseNumber +
                ", executedTestcases=" + executedTestcases +
                '}';
    }
}
