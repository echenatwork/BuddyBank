package web.model;

import java.util.List;

public class ScheduleBean {

    String code;
    String name;
    List<ScheduleBucketBean> buckets;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ScheduleBucketBean> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<ScheduleBucketBean> buckets) {
        this.buckets = buckets;
    }
}
