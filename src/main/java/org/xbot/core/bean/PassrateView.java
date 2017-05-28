package org.xbot.core.bean;

/**
 * Created by paulc on 3/15/2017.
 */
public class PassrateView {
    private int passRate;//0-100
    private int executionRate;//0-100

    public int getPassRate() {
        return passRate;
    }

    public void setPassRate(int passRate) {
        this.passRate = passRate;
    }

    public int getExecutionRate() {
        return executionRate;
    }

    public void setExecutionRate(int executionRate) {
        this.executionRate = executionRate;
    }

    @Override
    public PassrateView clone(){
        PassrateView result = new PassrateView();
        result.setPassRate(this.getPassRate());
        result.setExecutionRate(this.getExecutionRate());
        return result;
    }

    @Override
    public String toString() {
        return "PassrateView{" +
                "passRate=" + passRate +
                ", executionRate=" + executionRate +
                '}';
    }
}
