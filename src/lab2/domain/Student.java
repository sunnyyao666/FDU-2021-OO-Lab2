package lab2.domain;

import java.util.*;

public class Student {
    private final String name;
    private final String number;
    private Major major;
    private final List<String> learning = new LinkedList<>();
    public ProgressTotal progressTotal = null;

    public Student(String name, String number, Major major) {
        this.name = name;
        this.number = number;
        this.major = major;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public Major getMajor() {
        return major;
    }

    public void addLearning(String course) {
        if (learning.contains(course)) {
            return;
        }
        learning.add(course);
        if (major.getPlans().size() == 0) {
            return;
        }
        if (progressTotal == null) {
            initProgressTotal();
        } else {
            progressTotal.addLearning(course);
        }
    }

    public void outputProgressTotal(String direction) {
        if (major.getPlans().size() == 0) {
            System.out.println(major.getName() + "专业不存在培养方案！");
            return;
        }
        if (progressTotal == null) {
            initProgressTotal();
        }
        progressTotal.output(direction);
    }

    public void changeMajor(Major major) {
        this.major = major;
        if (major.getPlans().size() == 0) {
            progressTotal = null;
            return;
        }
        initProgressTotal();
    }

    private void initProgressTotal() {
        progressTotal = new ProgressTotal(this);
        for (String s : learning) {
            progressTotal.addLearning(s);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return number.equals(student.getNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}

