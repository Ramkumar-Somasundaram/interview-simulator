package com.interviewsim.dto;
import java.time.LocalDate;
import java.util.List;
public class HeatmapResponse {
    private List<DayActivity> activities; private int totalActiveDays;
    private int currentStreak; private int maxStreak; private int totalSubmissions;
    public HeatmapResponse() {}
    public List<DayActivity> getActivities() { return activities; } public void setActivities(List<DayActivity> v) { this.activities = v; }
    public int getTotalActiveDays() { return totalActiveDays; } public void setTotalActiveDays(int v) { this.totalActiveDays = v; }
    public int getCurrentStreak() { return currentStreak; } public void setCurrentStreak(int v) { this.currentStreak = v; }
    public int getMaxStreak() { return maxStreak; } public void setMaxStreak(int v) { this.maxStreak = v; }
    public int getTotalSubmissions() { return totalSubmissions; } public void setTotalSubmissions(int v) { this.totalSubmissions = v; }
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private List<DayActivity> activities; private int totalActiveDays; private int currentStreak; private int maxStreak; private int totalSubmissions;
        public Builder activities(List<DayActivity> v) { this.activities = v; return this; }
        public Builder totalActiveDays(int v) { this.totalActiveDays = v; return this; }
        public Builder currentStreak(int v) { this.currentStreak = v; return this; }
        public Builder maxStreak(int v) { this.maxStreak = v; return this; }
        public Builder totalSubmissions(int v) { this.totalSubmissions = v; return this; }
        public HeatmapResponse build() {
            HeatmapResponse r = new HeatmapResponse();
            r.activities = activities; r.totalActiveDays = totalActiveDays; r.currentStreak = currentStreak; r.maxStreak = maxStreak; r.totalSubmissions = totalSubmissions;
            return r;
        }
    }
    public static class DayActivity {
        private LocalDate date; private int count;
        public DayActivity() {}
        public LocalDate getDate() { return date; } public void setDate(LocalDate date) { this.date = date; }
        public int getCount() { return count; } public void setCount(int count) { this.count = count; }
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private LocalDate date; private int count;
            public Builder date(LocalDate date) { this.date = date; return this; }
            public Builder count(int count) { this.count = count; return this; }
            public DayActivity build() { DayActivity d = new DayActivity(); d.date = date; d.count = count; return d; }
        }
    }
}
