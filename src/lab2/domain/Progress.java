package lab2.domain;

import java.util.LinkedHashMap;
import java.util.Map;

public class Progress {
    private final String name;
    private int curCredit;
    private int curNumber;
    private final int reqNumber;
    private int percentage;
    private final boolean isCredit;
    private final Map<Course, String> learningMap = new LinkedHashMap<>();

    public Progress(String name, int reqNumber, boolean isCredit) {
        this.name = name;
        this.reqNumber = reqNumber;
        this.isCredit = isCredit;
    }

    public String getName() {
        return name;
    }

    public boolean isCredit() {
        return isCredit;
    }

    public int addLearning(Course course, String note) {
        if (learningMap.containsKey(course)) {
            return 0;
        }
        learningMap.put(course, note);
        curCredit += course.getCredit();
        curNumber++;
        if (isCredit) {
            if (curCredit >= reqNumber) {
                percentage = 100;
                return curCredit - reqNumber;
            } else {
                percentage = (int) (100.0 * curCredit / reqNumber);
                return 0;
            }
        } else {
            if (curNumber > reqNumber) {
                percentage = 100;
                return course.getCredit();
            } else {
                percentage = (int) (100.0 * curNumber / reqNumber);
                return 0;
            }
        }
    }

    public String jsonSummary() {
        return "{" + "\"课程类型\":\"" + name + '"' +
                ",\"已修学分\":" + curCredit +
                ",\"已修课程数量\":" + curNumber +
                ",\"要求学分/课程数量\":" + reqNumber +
                ",\"进度情况\":\"" + percentage + "%\"" +
                '}';
    }

    public String jsonDetail() {
        StringBuilder json = new StringBuilder();
        json.append('"').append(name).append("\":[");
        boolean f = false;
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
        if (f) {
            json.append(',');
        }
        if (isCredit) {
            json.append("{\"总结\":\"要求").append(reqNumber).append("学分，缺少").append(Math.max(0, reqNumber - curCredit)).append("学分。\"");
        } else {
            json.append("{\"总结\":\"要求").append(reqNumber).append("门课，缺少").append(Math.max(0, reqNumber - curNumber)).append("门课。\"");
        }
        json.append(",\"备注\":\"").append(percentage).append("%\"}");
        json.append(']');
        return json.toString();
    }
}

