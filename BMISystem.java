import java.util.*;
import java.io.*;
import java.time.LocalDate;

public class BMISystem {
    static Scanner sc = new Scanner(System.in);

    // ========= USER SYSTEM =========
    public static boolean userExists(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.split(",")[0].equals(username)) return true;
            }
        } catch (IOException e) {}
        return false;
    }
	
    public static void registerUser() {
        try {
            System.out.print("Create username: ");
            String user = sc.nextLine();

            if (userExists(user)) {
                System.out.println("Username already exists.");
                return;
            }

            System.out.print("Create password: ");
            String pass = sc.nextLine();

            if (pass.length() < 4) {
                System.out.println("Password too short.");
                return;
            }

            FileWriter fw = new FileWriter("users.txt", true);
            fw.write(user + "," + pass + "\n");
            fw.close();
            System.out.println("Registered!");
        } catch (IOException e) {
			System.out.println("Error.");
        }
    }

    public static String loginUser() {
        System.out.print("Username: ");
        String user = sc.nextLine();

        System.out.print("Password: ");
        String pass = sc.nextLine();

        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d[0].equals(user) && d[1].equals(pass)) {
                    System.out.println("Login successful!");
                    return user;
                }
            }
        } catch (IOException e) {}

        System.out.println("Invalid login.");
        return null;
    }

    // ========= ID =========
    public static boolean isValidSAID(String id) {
        if (id.length() != 13 || !id.matches("\\d+")) return false;
        int sum = 0;
        for (int i = 0; i < 12; i += 2)
            sum += id.charAt(i) - '0';

        String even = "";
        for (int i = 1; i < 12; i += 2)
            even += id.charAt(i);

        int evenNum = Integer.parseInt(even) * 2;
		
        for (char c : String.valueOf(evenNum).toCharArray())
            sum += c - '0';

        int check = (10 - (sum % 10)) % 10;

        return check == id.charAt(12) - '0';
    }

    public static int getAge(String id) {
        int year = Integer.parseInt(id.substring(0, 2));
        int current = LocalDate.now().getYear() % 100;
        int fullYear = (year <= current) ? 2000 + year : 1900 + year;

        LocalDate birth = LocalDate.of(fullYear,
                Integer.parseInt(id.substring(2,4)),
                Integer.parseInt(id.substring(4,6)));

        int age = LocalDate.now().getYear() - birth.getYear();
        if (LocalDate.now().getDayOfYear() < birth.getDayOfYear()) age--;
        return age;
    }

    public static String getGender(String id) {
        return (Integer.parseInt(id.substring(6,10)) < 5000) ? "Female" : "Male";
    }

    public static String getCitizen(String id) {
        return (id.charAt(10) == '0') ? "SA Citizen" : "Resident";
    }

    // ========= BMI =========
    public static String getCategory(double bmi) {
        if (bmi < 18.5) return "Underweight";
        else if (bmi < 25) return "Normal";
        else if (bmi < 30) return "Overweight";
        else if (bmi < 35) return "Obese";
        else return "Severely Obese";
    }

    public static int getRisk(double bmi) {
        return Math.min((int)(Math.abs(bmi - 21.7) * 5), 100);
    }

    public static String getWorkout(String cat) {
        switch(cat) {
            case "Underweight": return "Strength training + high protein";
            case "Normal": return "Balanced workouts";
            case "Overweight": return "Cardio + light strength";
            case "Obese": return "Low-impact cardio";
            default: return "Consult doctor";
        }
    }

    // ========= PROGRESS =========
    public static void saveProgress(String user, double w, double bmi, String cat) {
        try {
            FileWriter fw = new FileWriter(user + "_progress.txt", true);
            fw.write(LocalDate.now() + " | " + w + "kg | BMI "
                    + String.format("%.2f", bmi) + " | " + cat + "\n");
            fw.close();
        } catch (IOException e) {}
    }

    public static void viewProgress(String user) {
        try (BufferedReader br = new BufferedReader(new FileReader(user + "_progress.txt"))) {
            String line;
            System.out.println("\nProgress:");
            while ((line = br.readLine()) != null)
                System.out.println(line);
        } catch (IOException e) {
            System.out.println("No history.");
        }
    }

    // ========= SAVE REPORT =========
    public static void saveReport(String user, double bmi, String cat, String workout) {
        try {
            String file = user + "_" + System.currentTimeMillis() + ".txt";
            FileWriter fw = new FileWriter(file);
            fw.write("BMI REPORT\nUser: " + user +
                    "\nBMI: " + bmi +
                    "\nCategory: " + cat +
                    "\nWorkout: " + workout);
            fw.close();
            System.out.println("Saved: " + file);
        } catch (IOException e) {}
    }

    // ========= MAIN =========
    public static void main(String[] args) {
        String currentUser = null;
		
        // LOGIN LOOP
        while (currentUser == null) {
            System.out.println("\n1. Register\n2. Login\n3. Exit");
            int ch = sc.nextInt(); sc.nextLine();
            if (ch == 1) registerUser();
            else if (ch == 2) currentUser = loginUser();
            else System.exit(0);
        }
		
        // MAIN MENU
        int op;
        do {
            System.out.println("\n1. Calculate BMI\n2. View Progress\n3. Logout");
            op = sc.nextInt(); sc.nextLine();

            if (op == 1) {
                System.out.print("ID: ");
                String id;
                do {
                    id = sc.nextLine();
                    if (!isValidSAID(id)) System.out.print("Invalid ID. Try again: ");
                } while (!isValidSAID(id));

                int age = getAge(id);
                String gender = getGender(id);

                System.out.print("Weight: ");
                double w = sc.nextDouble();

                System.out.print("Height: ");
                double h = sc.nextDouble();

                double bmi = w / (h*h);
                String cat = getCategory(bmi);

                int risk = getRisk(bmi);
                String workout = getWorkout(cat);

                saveProgress(currentUser, w, bmi, cat);

                System.out.println("\nBMI: " + bmi + " (" + cat + ")");
                System.out.println("Age: " + age + " | " + gender);
                System.out.println("Risk: " + risk + "/100");
                System.out.println("Workout: " + workout);
                System.out.print("Save report? (y/n): ");

                if (sc.next().equalsIgnoreCase("y"))
                    saveReport(currentUser, bmi, cat, workout);
            } else if (op == 2) {
                viewProgress(currentUser);
            }
        } while (op != 3);
        System.out.println("Logged out.");
    }
}