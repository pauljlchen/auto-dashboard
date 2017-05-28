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
@Table(name = "team_confidence", catalog = "dashboard")
public class TeamConfidence {
    private String id;
    private Project project;
    private Double score;
    private Timestamp createdTime;
    private String createdBy;
    private String description;

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

    @Column(name = "score")
    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
    @Column(name = "created_by", length = 128)
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "description", length = 1280)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public String toString() {
        return "TeamConfidence{" +
                "id='" + id + '\'' +
                ", score=" + score +
                ", createdTime=" + createdTime +
                ", createdBy='" + createdBy + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
