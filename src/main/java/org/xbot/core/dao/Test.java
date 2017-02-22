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
@Table(name = "test", catalog = "dashboard")
public class Test {


    private String id;
    private String name;
    private String description;
    private Long manualExecutionTime;
    private Timestamp createdTime;

    private Project project;
    private Set<Record> records= new HashSet<Record>();

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
     * test case id, assigned by dashboard system or registered automatically
     */
    @Column(name = "name", length = 128)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", length = 1280)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * How much time required to complete this test case manually, in minute
     * @return
     */
    @Column(name = "manual_execution_time", length = 19)
    public Long getManualExecutionTime() {
        return manualExecutionTime;
    }

    public void setManualExecutionTime(Long manualExecutionTime) {
        this.manualExecutionTime = manualExecutionTime;
    }

    /**
     * map to the project id
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "test")
    public Set<Record> getRecords() {
        return records;
    }

    public void setRecords(Set<Record> records) {
        this.records = records;
    }


    @Override
    public String toString() {
        return "Test{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", manualExecutionTime=" + manualExecutionTime +
                ", createdTime=" + createdTime+

                '}';
    }
}
