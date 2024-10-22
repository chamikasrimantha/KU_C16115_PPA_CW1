package lk.auroraskincare.clinic.entity;

import java.time.LocalTime;

public enum ConsultationTime {
    MONDAY("Monday: 10:00am - 01:00pm", LocalTime.of(10, 0), LocalTime.of(13, 0)),
    WEDNESDAY("Wednesday: 02:00pm - 05:00pm", LocalTime.of(14, 0), LocalTime.of(17, 0)),
    FRIDAY("Friday: 04:00pm - 08:00pm", LocalTime.of(16, 0), LocalTime.of(20, 0)),
    SATURDAY("Saturday: 09:00am - 01:00pm", LocalTime.of(9, 0), LocalTime.of(13, 0));

    private String timeSlot;
    private LocalTime startTime;
    private LocalTime endTime;

    ConsultationTime(String timeSlot, LocalTime startTime, LocalTime endTime) {
        this.timeSlot = timeSlot;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}

