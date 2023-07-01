package lab2.domain;

import java.util.*;

public class Major {
    private final String name;
    private final List<Plan> plans = new LinkedList<>();

    public Major(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addPlan(Plan plan) {
        plans.add(plan);
    }

    public List<Plan> getPlans() {
        return plans;
    }

    public void adjustPlans() {
        Plan bc = null, mc = null;
        Iterator<Plan> iterator = plans.iterator();
        while (iterator.hasNext()) {
            Plan p = iterator.next();
            if (p.getName().equals("Basic Compulsory")) {
                bc = p;
                iterator.remove();
            } else if (p.getName().equals("Major Compulsory")) {
                mc = p;
                iterator.remove();
            }
        }
        if (bc == null && mc == null) {
            return;
        }
        int number = 0;
        Set<String> courses = new HashSet<>();
        if (bc != null) {
            number += bc.getNumber();
            courses.addAll(bc.getCourses());
        }
        if (mc != null) {
            number += mc.getNumber();
            courses.addAll(mc.getCourses());
        }
        Plan plan = new Plan("Basic and Major Compulsory", number, true);
        plan.setCourses(courses);
        plans.add(0, plan);
    }
}
