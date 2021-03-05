package com.example.lifestyle4;

public class NutritionCalcsUtil {

    /**
     * Calculates BMI
     * @param heightFeet user's height in feet rounded down to whole number
     * @param heightInches user's remaining height in inches
     * @param weightPounds user's weight in pounds
     * @return BMI
     */
    public static double getBmi(int heightFeet, int heightInches, int weightPounds) {

        double kg = NutritionCalcsUtil.toKilograms(weightPounds);
        double meters = NutritionCalcsUtil.toMeters(heightFeet, heightInches);
        return kg/(meters*meters);
    }

    /**
     * Calculates basal metabolic rate using Harris-Benedict Equation
     * @param isFemale user's sex
     * @param weightPounds user's weight in pounds
     * @param heightFeet user's height in feet rounded down to whole number
     * @param heightInches user's remaining height in inches
     * @param age user's age in years
     * @return user's BMR
     */
    public static double getBmr(boolean isFemale, int weightPounds, int heightFeet, int heightInches, int age){

        double kg = NutritionCalcsUtil.toKilograms(weightPounds);
        double cm = NutritionCalcsUtil.toMeters(heightFeet, heightInches)*100;
        if (isFemale)
            return 447.593 + (9.247*kg) + (3.098*cm) - (4.330*age);

        else
            return 88.362 + (13.397*kg) + (4.799*cm) - (5.677*age);
    }

    private static double toMeters(int feet, int inches) {
        int totalInches = inches + (feet*12);
        double centimeters = totalInches * 2.54;
        return centimeters/100;
    }

    private static double toKilograms(int pounds) {
        return pounds*0.453592;
    }

    /**
     * Calculates daily caloric needs as whole number
     * @param bmr user's BMR. See getBmr()
     * @param isActive user's activity level, defined as at least 30 minutes of strenuous exercise per day
     * @param poundsMod user's desire to increase weight, maintain weight, or lose weight. {-2 <= poundsMod <= 2} for sustainable caloric goal
     * @return user's daily caloric needs
     */
    public static int getDailyCalories(double bmr, boolean isActive, double poundsMod) {

        if (isActive)
            return (int) (1.725*bmr+(poundsMod*500));
        else
            return (int) ((1.2*bmr) + (poundsMod*500));
    }
}
