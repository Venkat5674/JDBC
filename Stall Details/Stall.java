// TASK 2 — Stall Details
import java.sql.*;
import java.util.Scanner;

class Stall {
    int id;
    String name;
    Double deposit;
}

class StallBO {
    public Stall getStall(int id) throws Exception {
        String url="jdbc:mysql://localhost/ri_db";
        String username ="test";
        String password ="test123";
        Class.forName("com.mysql.jdbc.Driver");

        Connection con = DriverManager.getConnection(url,username,password);
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("select * from stall where id=" + id);
        rs.next();

        Stall s = new Stall();
        s.id = rs.getInt(1);
        s.name = rs.getString(2);
        s.deposit = rs.getDouble(3);
        return s;
    }
}

class Main {
    public static void main(String args[]) throws Exception {
        StallBO sbo = new StallBO();
        Scanner sc = new Scanner(System.in);
        int id = Integer.parseInt(sc.nextLine());
        Stall S1 = sbo.getStall(id);

        System.out.format("%-10s %-10s %-10s\n","ID","Name","Deposit");
        System.out.format("%-10s %-10s %-10s\n",S1.id,S1.name,S1.deposit);
    }
}
