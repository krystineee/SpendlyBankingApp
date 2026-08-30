import userInterface.UserLogin;

public class Main {
    public static void main(String[] args) {
        System.out.println("DEBUG - password env var: " + System.getenv("SPENDLY_DB_PASSWORD"));
        new UserLogin();
    }
}