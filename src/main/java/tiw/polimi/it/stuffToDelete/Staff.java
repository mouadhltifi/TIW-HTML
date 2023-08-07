package tiw.polimi.it.stuffToDelete;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Staff {


    private String name;
    private int age;
    private String[] position;              // array
    private List<String> skills;            // list
    private Map<String, BigDecimal> salary; // map
    private Integer[] integerList;

    //getters and setters


    public void setIntegerList(Integer[] integerList) {
        this.integerList = integerList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPosition(String[] position) {
        this.position = position;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public void setSalary(Map<String, BigDecimal> salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", position=" + Arrays.toString(position) +
                ", skills=" + skills +
                ", salary=" + salary +
                ", integerList=" + Arrays.toString(integerList) +
                '}';
    }
}

