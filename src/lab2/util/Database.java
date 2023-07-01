package lab2.util;

import lab2.domain.Course;
import lab2.domain.Major;
import lab2.domain.Student;

import java.util.*;

public class Database {
    private final static Database instance = new Database();
    private final Map<String, Major> majors = new HashMap<>();
    private final Map<String, Student> students = new HashMap<>();
    private final Map<String, Course> courses = new HashMap<>();
    private final Map<String, String> exchangeMap = new HashMap<>();

    private Database() {
    }

    public static Database getInstance() {
        return instance;
    }

    public Major findMajor(String name) {
        Major m = majors.get(name);
        if (m != null) {
            return m;
        }
        m = new Major(name);
        majors.put(name, m);
        return m;
    }

    public void addStudent(Student student) {
        students.put(student.getNumber(), student);
    }

    public Student findStudent(String number) {
        return students.get(number);
    }

    public void addCourse(Course course) {
        courses.put(course.getNumber(), course);
    }

    public Course findCourse(String number) {
        return courses.get(number);
    }

    public void addExchange(String exchange, String origin) {
        exchangeMap.put(exchange, origin);
    }

    public Course findOrigin(String exchange) {
        return courses.get(exchangeMap.get(exchange));
    }
}
