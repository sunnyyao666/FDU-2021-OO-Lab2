package lab2.domain;

import lab2.util.Database;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ProgressTotal {
    private final Student student;
    private final Major major;
    private ProgressOther progressOther = null;
    private final Map<String, Progress> progressMap = new LinkedHashMap<>();
    private final Set<String> directions = new HashSet<>();

    public ProgressTotal(Student student) {
        this.student = student;
        major = student.getMajor();
        for (Plan p : major.getPlans()) {
            String name = "";
            if ("Basic and Major Compulsory".equals(p.getName())) {
                name = "必修的基础课与专业基础课";
            } else if ("Major Elective".equals(p.getName())) {
                name = "专业选修课";
            } else if ("Other Elective".equals(p.getName())) {
                progressOther = new ProgressOther(p.getNumber());
            } else if (p.getName().startsWith("Module")) {
                String[] t = p.getName().split(" ");
                name = "模块课 " + t[1];
            } else if (p.getName().startsWith("Direction")) {
                String[] t = p.getName().split(" ");
                name = "方向课 " + t[1];
                directions.add(t[1]);
            }
            if (name.equals("")) {
                continue;
            }
            Progress progress = new Progress(name, p.getNumber(), p.isCredit());
            progressMap.put(p.getName(), progress);
        }
        if (progressOther != null) {
            progressOther.initExceedCredits(progressMap.values());
        }
    }

    public void addLearning(String courseNumber) {
        Database database = Database.getInstance();
        Course course = database.findCourse(courseNumber);
        if (course == null) {
            System.out.println(courseNumber + "不存在课程信息！");
            return;
        }
        String note = "null";
        if (course.isExchange()) {
            Course origin = database.findOrigin(courseNumber);
            if (origin != null) {
                note = courseNumber + "-" + course.getName();
                course = origin;
            }
        }
        String type = null;
        for (Plan p : major.getPlans()) {
            if (p.getCourses().contains(course.getNumber())) {
                type = p.getName();
                break;
            }
        }
        if (type != null) {
            Progress progress = progressMap.get(type);
            int exceed = progress.addLearning(course, note);
            progressOther.updateExceedCredit(progress, exceed);
        } else {
            progressOther.addLearning(course, note);
        }
    }

    public void output(String direction) {
        boolean isDirection = directions.contains(direction);
        StringBuilder json = new StringBuilder("{");
        json.append("\"学号\":\"").append(student.getNumber()).append('"');
        json.append(",\"学生名\":\"").append(student.getName()).append('"');
        json.append(",\"专业\":\"").append(major.getName()).append('"');
        if (isDirection) {
            json.append(",\"方向\":\"").append(direction).append('"');
        }
        json.append(",\"进度汇总\":[");
        boolean f = false;
        for (Progress p : progressMap.values()) {
            if (p.getName().startsWith("方向课") && !p.getName().endsWith(direction)) {
                continue;
            }
            if (f) {
                json.append(',');
            } else {
                f = true;
            }
            json.append(p.jsonSummary());
        }
        if (progressOther != null) {
            if (f) {
                json.append(',');
            }
            json.append(progressOther.jsonSummary(direction));
        }

        json.append(']');
        json.append(",\"进度详情\":{");
        f = false;
        for (Progress p : progressMap.values()) {
            if (p.getName().startsWith("方向课") && !p.getName().endsWith(direction)) {
                continue;
            }
            if (f) {
                json.append(',');
            } else {
                f = true;
            }
            json.append(p.jsonDetail());
        }
        if (progressOther != null) {
            if (f) {
                json.append(',');
            }
            json.append(progressOther.jsonDetail(direction));
        }
        json.append("}}");

        String path;
        if (isDirection) {
            path = "./" + student.getNumber() + "_" + student.getName() + "_" + direction + ".json";
        } else {
            path = "./" + student.getNumber() + "_" + student.getName() + ".json";
        }
        System.out.println("Write to " + path);
        try (FileWriter fileWriter = new FileWriter(path)) {
            fileWriter.append(json.toString());
        } catch (IOException e) {
            System.out.println("Fail to write json!");
        }
    }
}
