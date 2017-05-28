package org.xbot.core.bean;

import org.xbot.core.dao.TeamConfidence;

/**
 * Created by paulc on 3/15/2017.
 */
public class TeamConfidenceView {
    private Double score;
    private String description;
    private String createdBy;


    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    @Override
    public TeamConfidenceView clone(){
        TeamConfidenceView result = new TeamConfidenceView();
        result.setScore(this.getScore());
        result.setDescription(this.getDescription());
        result.setCreatedBy(this.getCreatedBy());
        return result;
    }
}
