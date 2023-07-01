package lab2;

import lab2.domain.Major;
import lab2.domain.Student;
import lab2.util.Database;
import lab2.util.FileReader;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        handleInput();
    }

    private static void handleInput() {
        FileReader fileReader = FileReader.getInstance();
        Database database = Database.getInstance();
        boolean first = true;
        Scanner input = new Scanner(System.in);
        while (true) {
            if (first) {
                first = false;
                printHelp();
            }
            String s = input.nextLine();
            if ("1".equals(s)) {
                System.out.println("请输入学生数据文件名（例：./Students_Info.txt）");
                s = input.nextLine();
                fileReader.importStudents(s);
                System.out.println("Done!");
            } else if ("2".equals(s)) {
                System.out.println("请输入课程数据文件名（例：./Courses_Info.txt）");
                s = input.nextLine();
                fileReader.importCourses(s);
                System.out.println("Done!");
            } else if ("3".equals(s)) {
                System.out.println("请输入专业名（例：Software Engineering）");
                String majorName = input.nextLine();
                System.out.println("请输入培养方案文件名（例：./Software Engineering.txt）");
                s = input.nextLine();
                fileReader.importPlans(majorName, s);
                System.out.println("Done!");
            } else if ("4".equals(s)) {
                System.out.println("请输入修读方案文件名（例：./Learning.txt）");
                s = input.nextLine();
                fileReader.importLearning(s);
                System.out.println("Done!");
            } else if ("5".equals(s)) {
                System.out.println("请输入学生学号（例：18302010993）");
                s = input.nextLine();
                Student student = database.findStudent(s);
                if (student == null) {
                    System.out.println("该学生不存在！");
                    continue;
                }
                System.out.println("请输入方向，大小写敏感（例：A），输入无效方向将输出基础进度报告");
                s = input.nextLine();
                student.outputProgressTotal(s);
                System.out.println("Done!");
            } else if ("6".equals(s)) {
                System.out.println("请输入学生学号（例：18302010993）");
                s = input.nextLine();
                Student student = database.findStudent(s);
                if (student == null) {
                    System.out.println("该学生不存在！");
                    continue;
                }
                System.out.println("请输入专业名（例：Software Engineering）");
                s = input.nextLine();
                Major major = database.findMajor(s);
                student.changeMajor(major);
                System.out.println("Done!");
            } else if ("0".equals(s)) {
                break;
            } else {
                printHelp();
            }
        }
    }

    private static void printHelp() {
        System.out.println("用法：");
        System.out.println("键入          用途");
        System.out.println(" 1           导入学生信息");
        System.out.println(" 2           导入课程信息");
        System.out.println(" 3           导入培养方案");
        System.out.println(" 4           导入修读信息");
        System.out.println(" 5           输出进度报告");
        System.out.println(" 6           切换学生专业");
        System.out.println(" 0           退出");
    }
}

