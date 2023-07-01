package lab2.util;

import lab2.domain.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileReader {
    private final static FileReader instance = new FileReader();
    private final Database database = Database.getInstance();

    private FileReader() {
    }

    public static FileReader getInstance() {
        return instance;
    }

    public void importStudents(String path) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                String[] info = s.split(", ");
                Major major = database.findMajor(info[2]);
                Student student = new Student(info[1], info[0], major);
                database.addStudent(student);
            }

        } catch (IOException e) {
            System.out.println("Failed to load file '" + path + "'!");
        }
    }

    public void importCourses(String path) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                String[] info = s.split(", ");
                Course course;
                if (info.length > 3 && "exchange".equals(info[3])) {
                    course = new Course(info[1], info[0], Integer.parseInt(info[2]), true);
                } else {
                    course = new Course(info[1], info[0], Integer.parseInt(info[2]), false);
                    if (info.length > 3 && !"none".equals(info[3])) {
                        database.addExchange(info[3], info[0]);
                    }
                }
                database.addCourse(course);
            }

        } catch (IOException e) {
            System.out.println("Failed to load file '" + path + "'!");
        }
    }

    public void importPlans(String majorName, String path) {
        Major major = database.findMajor(majorName);
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            String s;
            Plan plan = null;
            while ((s = bufferedReader.readLine()) != null) {
                if (s.charAt(0) == '[') {
                    String[] info = s.replace('[', ' ').trim().split("] ");
                    boolean isCredit = !info[0].startsWith("Module");
                    plan = new Plan(info[0], Integer.parseInt(info[1]), isCredit);
                    major.addPlan(plan);
                } else {
                    assert plan != null;
                    plan.addCourse(s);
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to load file '" + path + "'!");
        }
        major.adjustPlans();
    }

    public void importLearning(String path) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                String[] info = s.split(", ");
                Student student = database.findStudent(info[0]);
                if (student == null) {
                    System.out.println("学号为" + info[0] + "的学生不存在！");
                    continue;
                }
                student.addLearning(info[1]);
            }

        } catch (IOException e) {
            System.out.println("Failed to load file '" + path + "'!");
        }
    }
}

