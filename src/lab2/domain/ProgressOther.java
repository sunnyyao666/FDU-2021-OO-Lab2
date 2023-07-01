package lab2.domain;

import java.util.*;

public class ProgressOther {
    private int curCredit;
    private int curNumber;
    private final int reqNumber;
    private final Map<Course, String> learningMap = new LinkedHashMap<>();
    private final Map<String, Integer> exceedCredits = new LinkedHashMap<>();

    public ProgressOther(int reqNumber) {
        this.reqNumber = reqNumber;
    }

    public void initExceedCredits(Collection<Progress> progresses) {
        for (Progress p : progresses) {
            exceedCredits.put(p.getName(), 0);
        }
    }

    public void updateExceedCredit(Progress progress, int credit) {
        if (exceedCredits.containsKey(progress.getName())) {
            if (progress.isCredit()) {
                exceedCredits.put(progress.getName(), credit);
            } else {
                exceedCredits.put(progress.getName(), credit + exceedCredits.get(progress.getName()));
            }
        }
    }

    public void addLearning(Course course, String note) {
        if (learningMap.containsKey(course)) {
            return;
        }
        learningMap.put(course, note);
        curCredit += course.getCredit();
        curNumber++;
    }

    public String jsonSummary(String direction) {
        int totalCredit = curCredit;
        for (Map.Entry<String, Integer> entry : exceedCredits.entrySet()) {
            if (entry.getKey().startsWith("方向课") && !entry.getKey().endsWith(direction)) {
                continue;
            }
            totalCredit += entry.getValue();
        }

        int percentage;
        if (totalCredit >= reqNumber) {
            percentage = 100;
        } else {
            percentage = (int) (100.0 * totalCredit / reqNumber);
        }
        return "{" + "\"课程类型\":\"任意选修课\"" +
                ",\"已修学分\":" + curCredit +
                ",\"已修课程数量\":" + curNumber +
                ",\"要求学分/课程数量\":" + reqNumber +
                ",\"进度情况\":\"" + percentage + "%\"" +
                '}';
    }

    public String jsonDetail(String direction) {
        StringBuilder json = new StringBuilder();
        json.append("\"任意选修课\":[");
        boolean f = false;
        int totalExceed = 0;
        for (Course c : learningMap.keySet()) {
            if (f) {
                json.append(',');
            } else {
                f = true;
            }
            json.append("{\"课程\":\"").append(c.getNumber()).append('-').append(c.getName()).append('"');
            json.append(",\"学分\":").append(c.getCredit());
            json.append(",\"备注\":\"").append(learningMap.get(c)).append("\"}");
        }
        for (Map.Entry<String, Integer> entry : exceedCredits.entrySet()) {
            if (entry.getKey().startsWith("方向课") && !entry.getKey().endsWith(direction)) {
                continue;
            }
            if (f) {
                json.append(',');
            } else {
                f = true;
            }
            json.append("{\"课程\":\"").append(entry.getKey()).append("超出学分\"");
            json.append(",\"学分\":").append(entry.getValue());
            json.append(",\"备注\":\"null\"}");
            totalExceed += entry.getValue();
        }
        if (f) {
            json.append(',');
        }

        json.append("{\"总结\":\"要求").append(reqNumber).append("学分，缺少").append(Math.max(0, reqNumber - totalExceed - curCredit)).append("学分,超出").append(totalExceed).append("学分。\"");

        int percentage;
        int totalCredit = totalExceed + curCredit - reqNumber;
        if (totalCredit >= reqNumber) {
            percentage = 100;
        } else {
            percentage = (int) (100.0 * totalCredit / reqNumber);
        }

        json.append(",\"备注\":\"").append(percentage).append("%\"}");
        json.append(']');
        return json.toString();
    }
}

