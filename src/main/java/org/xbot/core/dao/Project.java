package org.xbot.core.dao;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * The TaskRequest contains a lot of Tasks. Task can be assigned to individual Agent to complete. The Agent create the task
 * Created by Paul Chan on 2016/12/22.
 */
@Entity
@Table(name = "project", catalog = "dashboard")
public class Project {
    public enum STATUS{Active, Inactive, ToBeDeleted}
    private String id=null;
    private STATUS status=null;
    private String productCode=null;
    private String productName=null;
    private String manager=null;
    private String leader=null;
    private String category=null;
    private String region=null;
    private String country=null;
    private String pod=null;
    private String testingTools=null;
    private Integer targetTestcaseNumber = null;
    private Timestamp createdTime=null;
    private Integer phaseTestcaseNumber = null;
    private Boolean isAllTestCaseAutomated = null;
    private Set<Test> tests = new HashSet<Test>();
    private Set<TeamConfidence> teamConfidence = new HashSet<>();

    @GenericGenerator(name = "generator", strategy = "uuid2")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", unique = true, nullable = false, length = 36)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * The time that this record created
     * @return
     */
    @Column(name = "created_time")
    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }


    /**
     * test product code, user defined
     */
    @Column(name = "project_code", length = 36)
    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    /**
     * Product name
     * @return
     */
    @Column(name = "project_name", length = 256)
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Column(name = "leader", length = 256)
    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    @Column(name = "manager", length = 256)
    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    @Column(name = "testing_tools", length = 1024)
    public String getTestingTools() {
        return testingTools;
    }

    public void setTestingTools(String testingTools) {
        this.testingTools = testingTools;
    }

    @Column(name = "status", length = 16)
    @Enumerated(EnumType.STRING)
    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    /**
     * The category of product
     * @return
     */
    @Column(name = "category", length = 50)
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Column(name = "region", length = 50)
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
    @Column(name = "country", length = 50)
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "project")
    public Set<Test> getTests() {
        return tests;
    }

    public void setTests(Set<Test> tests) {
        this.tests = tests;
    }

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "project")
    public Set<TeamConfidence> getTeamConfidence() {
        return teamConfidence;
    }

    public void setTeamConfidence(Set<TeamConfidence> teamConfidence) {
        this.teamConfidence = teamConfidence;
    }

    @Column(name = "pod", length = 100)
    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    @Column(name = "target_testcase_number")
    public Integer getTargetTestcaseNumber() {
        return targetTestcaseNumber;
    }

    public void setTargetTestcaseNumber(Integer targetTestcaseNumber) {
        this.targetTestcaseNumber = targetTestcaseNumber;
    }

    @Column(name = "phase_testcase_number")
    public Integer getPhaseTestcaseNumber() {
        return phaseTestcaseNumber;
    }

    public void setPhaseTestcaseNumber(Integer phaseTestcaseNumber) {
        this.phaseTestcaseNumber = phaseTestcaseNumber;
    }
    @Column(name = "is_all_testcase_automated")
    public Boolean isAllTestCaseAutomated() {
        return isAllTestCaseAutomated;
    }

    public void setAllTestCaseAutomated(Boolean allTestCaseAutomated) {
        isAllTestCaseAutomated = allTestCaseAutomated;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", status=" + status +
                ", productCode='" + productCode + '\'' +
                ", productName='" + productName + '\'' +
                ", manager='" + manager + '\'' +
                ", leader='" + leader + '\'' +
                ", category='" + category + '\'' +
                ", region='" + region + '\'' +
                ", country='" + country + '\'' +
                ", pod='" + pod + '\'' +
                ", testingTools='" + testingTools + '\'' +
                ", targetTestcaseNumber=" + targetTestcaseNumber +
                ", createdTime=" + createdTime +
                ", phaseTestcaseNumber=" + phaseTestcaseNumber +
                ", isAllTestCaseAutomated=" + isAllTestCaseAutomated +
              //  ", tests=" + tests +
               // ", teamConfidence=" + teamConfidence +
                '}';
    }
}
