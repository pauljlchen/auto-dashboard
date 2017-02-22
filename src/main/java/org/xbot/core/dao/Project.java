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
    private String projectCode=null;
    private String projectName=null;
    private String leader=null;
    private String category=null;
    private String region=null;
    private String country=null;
    private Timestamp createdTime=null;
    private Set<Test> tests = new HashSet<Test>();

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
     * test project code, user defined
     */
    @Column(name = "project_code", length = 36)
    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    /**
     * Project name
     * @return
     */
    @Column(name = "project_name", length = 128)
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Column(name = "leader", length = 128)
    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
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
     * The category of project
     * @return
     */
    @Column(name = "category", length = 36)
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Column(name = "region", length = 20)
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
    @Column(name = "country", length = 20)
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

    @Override
    public String toString() {
       return "Project{" +
                "id='" + id + '\'' +
                ", status=" + status +
                ", projectCode='" + projectCode + '\'' +
                ", projectName='" + projectName + '\'' +
                ", leader='" + leader + '\'' +
                ", category='" + category + '\'' +
                ", region='" + region + '\'' +
                ", country='" + country + '\'' +
                ", createdTime=" + createdTime +
                '}';
    }
}