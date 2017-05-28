package org.xbot.core.dao;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**

 */
@Entity
@Table(name = "record", catalog = "dashboard")
public class Record {


    public enum Result{Success, Fail, Other}

    private String id;

    private Test test;

    private String source;

    private Timestamp startTime;
    private Timestamp endTime;
    private Result result;
    private Long manualExecutionTime;
    private String token;

    private String ip;

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
    @Column(name = "start_time")
    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    /**
     * test case id, assigned by dashboard system or registered automatically
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id")
    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }




    /**
     * The source system that the data came from
     * @return
     */
    @Column(name = "source", length = 128)
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Column(name = "end_time")
    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    /**
     * Type of the record
     * @return
     */
    @Column(name = "result", length = 10)
    @Enumerated(EnumType.STRING)
    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }


    /**
     * The token id to match the start and end records
     * @return
     */
    @Column(name = "token_id", length = 36)
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Column(name = "manual_execution_time", length = 19)
    public Long getManualExecutionTime() {
        return manualExecutionTime;
    }

    public void setManualExecutionTime(Long manualExecutionTime) {
        this.manualExecutionTime = manualExecutionTime;
    }
    @Column(name = "ip", length = 64)
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id='" + id + '\'' +

                ", source='" + source + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", result=" + result +
                ", token='" + token + '\'' +
                '}';
    }
}
