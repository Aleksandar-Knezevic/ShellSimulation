import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Scanner;


public class Login {

    private static String bytesToHex(byte[] bytes)
    {
        BigInteger number = new BigInteger(1, bytes);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while(hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }

    private static String calculateHash(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes());
        return bytesToHex(hash);
    }


    public static HashMap<String, String> loadAccounts() throws IOException {
        String str;
        HashMap<String, String> accounts = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(new File("hashedAccounts.txt")));
        while((str=br.readLine())!=null)
        {
            String[] row = str.split("-");
            accounts.put(row[0], row[1]);
        }
        return accounts;
    }


    public static boolean verifyLogin(String username, String password) throws IOException, NoSuchAlgorithmException {
        HashMap<String, String> accounts = loadAccounts();
        String hash = calculateHash(password);
        String map = accounts.get(username);
        if(!accounts.containsKey(username) || !accounts.get(username).equals(calculateHash(password))) {
            System.out.println("Account does not exist or wrong username/password combination");
            return false;
        }
        System.out.println("Login successful");
        return true;
    }

    public static void login() {
        try {
            //Console console = System.console();
            Scanner console = new Scanner(System.in);
            while (true) {
                String command = console.nextLine();
                if (command.equals("login")) {
                    System.out.print("Username: ");
                    String username = console.nextLine();
                    System.out.print("Password: ");
                    //String password = new String(console.readPassword());
                    String password = console.nextLine();
                    if (Login.verifyLogin(username, password)) {

                        File file = new File(System.getProperty("user.dir") + File.separator + username);
                        if (!file.exists())
                            file.mkdir();
                        System.setProperty("user.dir", file.getAbsolutePath());
                        System.out.println(System.getProperty("user.dir"));
                        break;
                    }
                } else
                    System.out.println("You have to log in first!");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
       Commands.input();
    }

}
