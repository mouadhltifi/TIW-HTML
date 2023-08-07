package tiw.polimi.it.stuffToDelete;

import com.google.gson.Gson;
import tiw.polimi.it.beans.User;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class exampleMain {
    public static void main(String[] args) throws IOException {

        Gson gson = new Gson();
        User user = new User(
                123,
                "senza",
                "nome",
                "mail",
                "pswrd",
                "casa mia"
        );

        String jsonString = gson.toJson(user);
        gson.toJson(user,new FileWriter("staff.json"));

        System.out.println("prova di json : " + jsonString);

        User user1 = gson.fromJson(jsonString,User.class);

        System.out.println(user1);

        Staff staff = createStaffObject();

        String staffString = gson.toJson(staff);

        System.out.println("stuff string : " + staffString);

        Staff staff1 = gson.fromJson(staffString,Staff.class);

        System.out.println(staff1);

    }

    private static Staff createStaffObject() {

       // List<Integer> temp = new int[]{1, 2, 3, 4};
        Staff staff = new Staff();

        staff.setName("mkyong");
        staff.setAge(35);
        staff.setPosition(new String[]{"Founder", "CTO", "Writer"});
        Map<String, BigDecimal> salary = new HashMap() {{
            put("2010", new BigDecimal(10000));
            put("2012", new BigDecimal(12000));
            put("2018", new BigDecimal(14000));
        }};
        staff.setSalary(salary);
        staff.setSkills(Arrays.asList("java", "python", "node", "kotlin"));
        staff.setIntegerList(new Integer[] {1,2,3,4});
        return staff;

    }



}
