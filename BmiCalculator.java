import java.util.Scanner;

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

            System.out.print("Enter your weight (kg): ");
            double weight = scanner.nextDouble();

            System.out.print("Enter your height (m): ");
            double height = scanner.nextDouble();

            double bmi = calculateBMI(unitChoice, weight, height);

            System.out.println("\n==============================");
            System.out.println("       BMI RESULTS");
            System.out.println("==============================");

            System.out.println("Name: " + name + " " + surname);
            System.out.println("ID Number: " + id);

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
	
    public static double calculateBMI(int unitChoice, double weight, double height) {
        double totalBMI;
		
		if(unitChoice == 1) {
			totalBMI =  weight / (height * height);
		} else {
			totalBMI = (700 * weight) / (height/ height);
		}
		
		return totalBMI;
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
}