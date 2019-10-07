package web.model;

import java.time.Instant;
import java.util.Date;

public class AccountToScheduleBean {

    private Long id;
    private ScheduleBean schedule;
    private Instant startDateTime;
    private Instant endDateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ScheduleBean getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleBean schedule) {
        this.schedule = schedule;
    }

    public Instant getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Instant startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Instant getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Instant endDateTime) {
        this.endDateTime = endDateTime;
    }
}
