package org.xbot.core.dao;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by paulc on 3/14/2017.
 */
@Entity
@Table(name = "audit_log", catalog = "dashboard")
public class AuditLog {
    private String id;
    private Timestamp createdTime=null;
    private String createdBy=null;
    private String oriValue=null;
    private String newValue=null;
    private String desc=null;

    public AuditLog() {
        this.createdTime = new Timestamp(System.currentTimeMillis());
    }
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

    @Column(name = "created_time")
    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }
    @Column(name = "created_by", length = 100)
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    @Column(name = "ori_value", length = 1024)
    public String getOriValue() {
        return oriValue;
    }

    public void setOriValue(String oriValue) {
        this.oriValue = oriValue;
    }
    @Column(name = "new_value", length = 1024)
    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
    @Column(name = "description", length = 2048)
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "AuditLog{" +
                "createdTime=" + createdTime +
                ", createdBy='" + createdBy + '\'' +
                ", oriValue='" + oriValue + '\'' +
                ", newValue='" + newValue + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
