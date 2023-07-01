package lab2.domain;

import java.util.Objects;

public class Course {
    private final String name;
    private final String number;
    private final int credit;
    private final boolean isExchange;

    public Course(String name, String number, int credit, boolean isExchange) {
        this.name = name;
        this.number = number;
        this.credit = credit;
        this.isExchange = isExchange;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public int getCredit() {
        return credit;
    }

    public boolean isExchange() {
        return isExchange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return number.equals(course.getNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
