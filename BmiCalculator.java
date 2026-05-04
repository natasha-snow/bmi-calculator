import java.util.Scanner;
import java.time.LocalDate;

public class BmiCalculator {
	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char choice;

        do {
            System.out.print("Enter your name: ");
            String name = scanner.next();

            System.out.print("Enter your surname: ");
            String surname = scanner.next();

            System.out.print("Enter your ID number: ");
            String id = scanner.next();
			
			int age = calculateAge(id);

            System.out.print("Enter your weight (kg): ");
            double weight = scanner.nextDouble();

            System.out.print("Enter your height (m): ");
            double height = scanner.nextDouble();

            double bmi = calculateBMI(weight, height);

            System.out.println("\n==============================");
            System.out.println("       BMI RESULTS");
            System.out.println("==============================");
            System.out.println("Name: " + name + " " + surname);
            System.out.println("ID Number: " + id);
			System.out.println("Age: " + age);
            System.out.printf("BMI: %.2f\n", bmi);
            System.out.println("Category: " + determineBMICategory(bmi));

            if (bmi >= 35) {
                System.out.println("Health Risk: Please consult a healthcare professional.");
            }

            System.out.println("==============================");

            System.out.print("\nDo you want to calculate again? (y/n): ");
            choice = scanner.next().charAt(0);

        } while (choice == 'y' || choice == 'Y');

        System.out.println("Thank you for using the BMI Calculator!");
        scanner.close();
    }
	
    public static double calculateBMI(double weight, double height) {
        return weight / (height * height);
    }

    public static String determineBMICategory(double bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 25) {
            return "Normal weight";
        } else if (bmi < 30) {
            return "Overweight";
        } else if (bmi < 35) {
            return "Obese";
        } else {
            return "Severely obese";
        }
    }
	
	public static int calculateAge(String id) {
        int year = Integer.parseInt(id.substring(0, 2));
        int month = Integer.parseInt(id.substring(2, 4));
        int day = Integer.parseInt(id.substring(4, 6));

        int currentYear = LocalDate.now().getYear() % 100;
        int fullYear;

        if (year <= currentYear) {
            fullYear = 2000 + year;
        } else {
            fullYear = 1900 + year;
        }

        LocalDate birthDate = LocalDate.of(fullYear, month, day);
        LocalDate today = LocalDate.now();

        int age = today.getYear() - birthDate.getYear();

        if (today.getDayOfYear() < birthDate.getDayOfYear()) {
            age--;
        }

        return age;
    }
}
