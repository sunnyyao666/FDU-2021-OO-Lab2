package lab2.domain;

import java.util.HashSet;
import java.util.Set;

public class Plan {
    private final String name;
    private final int number;
    private final boolean isCredit;
    private Set<String> courses = new HashSet<>();

    public Plan(String name, int number, boolean isCredit) {
        this.name = name;
        this.number = number;
        this.isCredit = isCredit;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public boolean isCredit() {
        return isCredit;
    }

    public void addCourse(String number) {
        courses.add(number);
    }

    public Set<String> getCourses() {
        return courses;
    }

    public void setCourses(Set<String> courses) {
        this.courses = courses;
    }
}
