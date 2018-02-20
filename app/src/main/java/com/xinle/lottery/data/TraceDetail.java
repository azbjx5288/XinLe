package com.xinle.lottery.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ACE-PC on 2016/6/1.
 */
public class TraceDetail {

    /**
     * id : 287
     * user_id : 284
     * terminal_id : null
     * serial_number : 284574C0E8478D1A4.92453597
     * prize_group : 1950
     * lottery_id : 1
     * total_issues : 5
     * finished_issues : 1
     * canceled_issues : 0
     * stop_on_won : 1
     * start_issue : 160530072
     * way_id : 26
     * way : 后四组选24
     * bet_number : 4567
     * coefficient : 0.010
     * single_amount : 0.0200
     * amount : 0.5000
     * prize : 0.0000
     * status : 0
     * bought_at : 2016-05-30 17:57:23
     * updated_at : 2016-05-30 17:57:24
     */

    @SerializedName("basic")
    private TraceDetailBasic basic;
    /**
     * id : 18307
     * issue : 160530072
     * multiple : 5
     * amount : 0.1000
     * project_id : 21838
     * status : 1
     * bought_at : 2016-05-30 17:57:24
     * canceled_at : null
     */

    @SerializedName("issues")
    private List<TraceDetailIssues> issues;

    public TraceDetailBasic getBasic() {
        return basic;
    }

    public void setBasic(TraceDetailBasic basic) {
        this.basic = basic;
    }

    public List<TraceDetailIssues> getIssues() {
        return issues;
    }

    public void setIssues(List<TraceDetailIssues> issues) {
        this.issues = issues;
    }
}
