// TASK 3 — Insert & Search Students
import java.sql.*;
import java.util.Scanner;

class JDBCExample {
    static final String DB_URL = "jdbc:mysql://localhost/ri_db";
    static final String USER = "test";
    static final String PASS = "test123";

    public static void main(String[] args) throws Exception {
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        Class.forName("com.mysql.jdbc.Driver");
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine());

        String sql = "insert into BILL values(?,?,?,?,?)";
        PreparedStatement st = conn.prepareStatement(sql);

        for(int i=0;i<n;i++) {
            st.setInt(1, Integer.parseInt(sc.nextLine()));
            st.setString(2, sc.nextLine());
            st.setString(3, sc.nextLine());
            st.setString(4, sc.nextLine());
            st.setDouble(5, Double.parseDouble(sc.nextLine()));
            st.executeUpdate();
        }

        String search = sc.nextLine();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM BILL WHERE DNAME='" + search + "'");
        int count=0;
        while(rs.next()){
            count++;
            System.out.println(rs.getInt(1)+" "+rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4)+" "+rs.getDouble(5));
        }
        if(count == 0)
            System.out.println("No students in " + search + " department");
    }
}
