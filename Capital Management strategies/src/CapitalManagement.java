import java.util.*;

public class CapitalManagement {

    private static final int profitablePercent = 60;
    private static final int lockedProfit = 8;
    private static final double rewardToRisk = 2;
    private static final double unitToCapital = 0.02;         // of capital
    private static final int numberOfTrades = 1500;
    private static final int numberOfTests = 100000;
    private static final double totalCapital = 100.0;
    private static final double TP = 0.3;                    // of capital



    private static double Strategy1(){
        double totalProfit = 0.0;
        int j = 0;
        double base = unitToCapital * (totalCapital + totalProfit);


        Random r = new Random();
        for(int i = 0; i < numberOfTrades; i++){

                if(mode(r.nextInt()) > profitablePercent){

                    totalProfit -= base * TP / rewardToRisk;
                    if(totalProfit < -totalCapital){
                        System.out.println("bankrupt Strategy1!");
                        return totalProfit;
                    }

                    j = 0;
                    base = unitToCapital * (totalCapital + totalProfit);

                }else {

                    totalProfit += base * TP;
                    j += 1;
                    base += unitToCapital * (totalCapital + totalProfit) / rewardToRisk ;

                    if(j > lockedProfit){
                        j = 0;
                        base = unitToCapital * (totalCapital + totalProfit);
                    }
                }
            if(base > totalCapital + totalProfit)
                base = totalProfit + totalCapital;
            }
//        System.out.println("Strategy1:\n trade numbers = " + tradeNumbers);
//        System.out.println("Total Profit = " + totalProfit);
//        System.out.println();
        return totalProfit;
    }

    private static double StrategyMartingale(){
        double totalProfit = 0.0;
        double base = unitToCapital * (totalCapital + totalProfit);

        Random r = new Random();
        for(int i = 0; i < numberOfTrades; i++){

            if(mode(r.nextInt()) > profitablePercent){

                totalProfit -= base * TP / rewardToRisk;

                base = base * 2;

                if(totalProfit <= -totalCapital) {
                   //     System.out.println("bankrupt Martingale!");
                        return (-totalCapital - 1);
                    }

            }else {

                totalProfit += base *  TP;
                base = unitToCapital * (totalCapital + totalProfit);
            }
            if(base > totalCapital + totalProfit)
                base = totalProfit + totalCapital;
        }
//        System.out.println("StrategyMartingale:\n trade numbers = " + tradeNumbers);
//        System.out.println("Total Profit = " + totalProfit);
//        System.out.println();
        return totalProfit;
    }

    private static double StrategyAgent(){
        double totalProfit = 0.0;
        int j = 0;
        double base = unitToCapital * (totalCapital + totalProfit);

        Random r = new Random();
        for(int i = 0; i < numberOfTrades; i++){

            if(mode(r.nextInt()) > profitablePercent){

                totalProfit -= base * TP / rewardToRisk;

                base = base * 2;

                if(totalProfit < -totalCapital) {
                    System.out.println("bankrupt Agent!");
                    return (-totalCapital - 1);
                }

            }else {
                totalProfit += base *  TP;

                j += 1;
                base = (j + 1) * unitToCapital * (totalCapital + totalProfit);

                if(j > lockedProfit) {
                    j = 0;
                    base = unitToCapital * (totalCapital + totalProfit);
                }
            }

            if(base > (totalCapital + totalProfit))
                base = (totalProfit + totalCapital);
        }
//        System.out.println("StrategyMartingale:\n trade numbers = " + tradeNumbers);
//        System.out.println("Total Profit = " + totalProfit);
//        System.out.println();
        return totalProfit;
    }

    private static double StrategyMehrad(){
        double totalProfit = 0.0;
        int j = 0;
        double base = unitToCapital * (totalCapital + totalProfit);

        Random r = new Random();
        for(int i = 0; i < numberOfTrades; i++){
            if(mode(r.nextInt()) > profitablePercent){

                totalProfit -= base * TP / rewardToRisk;

                j -= 1/rewardToRisk;
                base -= unitToCapital * (totalCapital + totalProfit) / rewardToRisk;

                if(totalProfit < -totalCapital) {
                    System.out.println("bankrupt Mehrad!");
                    return (-totalCapital - 1);
                }

                if(j < 0){
                    j = 0;
                    base = unitToCapital * (totalCapital + totalProfit);
                }

            }else {

                totalProfit += base * TP;

                j += 1;
                base +=  unitToCapital * (totalCapital + totalProfit);

                if(j > lockedProfit){
                    j = 0;
                    base = unitToCapital * (totalCapital + totalProfit);
                }
            }
            if(base > (totalCapital + totalProfit) * unitToCapital * lockedProfit)
                base = (totalProfit + totalCapital) * unitToCapital * lockedProfit;
        }

        return totalProfit;
    }

    private static double StrategyMehrad1(){
        double totalProfit = 0.0;
        int j = 0;
        int profitablePercent = 70;
        double base = unitToCapital * (totalCapital + totalProfit);

        Random r = new Random();
        for(int i = 0; i < numberOfTrades; i++){
            if(mode(r.nextInt()) > profitablePercent){

                totalProfit -= base * TP / rewardToRisk;

                j -= 1/rewardToRisk;
                base -= unitToCapital * (totalCapital + totalProfit) / rewardToRisk;

                if(totalProfit < -totalCapital) {
//                    System.out.println("bankrupt Mehrad!");
                    return (-totalCapital - 1);
                }

                if(j < 0){
                    j = 0;
                    base = unitToCapital * (totalCapital + totalProfit);
                }

            }else {

                totalProfit += base * TP;

                j += 1;
                base +=  unitToCapital * (totalCapital + totalProfit);

                if(j > lockedProfit){
                    j = 0;
                    base = unitToCapital * (totalCapital + totalProfit);
                }
            }
            if(base > (totalCapital + totalProfit) * unitToCapital * lockedProfit)
                base = (totalProfit + totalCapital) * unitToCapital * lockedProfit;
        }

        return totalProfit;
    }

    private static double StrategyMehrad2(){
        double totalProfit = 0.0;
        int profitablePercent = 50;
        int j = 0;
        double base = unitToCapital * (totalCapital + totalProfit);

        Random r = new Random();
        for(int i = 0; i < numberOfTrades * 2.25; i++){
            if(mode(r.nextInt()) > profitablePercent){

                totalProfit -= base * TP / rewardToRisk;

                j -= 1/rewardToRisk;
                base -= unitToCapital * (totalCapital + totalProfit) / rewardToRisk;

                if(totalProfit < -totalCapital) {
//                    System.out.println("bankrupt Mehrad!");
                    return (-totalCapital - 1);
                }

                if(j < 0){
                    j = 0;
                    base = unitToCapital * (totalCapital + totalProfit);
                }

            }else {

                totalProfit += base * TP;

                j += 1;
                base +=  unitToCapital * (totalCapital + totalProfit);

                if(j > lockedProfit){
                    j = 0;
                    base = unitToCapital * (totalCapital + totalProfit);
                }
            }
            if(base > (totalCapital + totalProfit) * unitToCapital * lockedProfit)
                base = (totalProfit + totalCapital) * unitToCapital * lockedProfit;
        }

        return totalProfit;
    }

    private static double RiskFreeAgent(){
        double totalProfit = 0.0;
        int j = 0;
        int dead = 0;
        double base = unitToCapital * (totalCapital + totalProfit);

        Random r = new Random();
        for(int i = 0; i < numberOfTrades; i++){

            if(mode(r.nextInt()) > profitablePercent){

                dead -= 1/rewardToRisk;
                totalProfit -= base * TP / rewardToRisk;

                base = base * 2;

                if(totalProfit < -totalCapital) {
                    System.out.println("bankrupt RiskFreeStrategy!");
                    return (-totalCapital - 1);
                }

            }else {
                totalProfit += base *  TP;

                j  += dead + 1;
                base = (j + 1) * unitToCapital * (totalCapital + totalProfit);

                if(j < 1){
                    j = 1;
                    base = 2 * unitToCapital * (totalCapital + totalProfit);
                }

                if(j > lockedProfit) {
                    j = 0;
                    base = unitToCapital * (totalCapital + totalProfit);
                }

                dead = 0;
            }

            if(base > (totalCapital + totalProfit) * unitToCapital * lockedProfit)
                base = (totalProfit + totalCapital) * unitToCapital * lockedProfit;
        }
//        System.out.println("StrategyMartingale:\n trade numbers = " + tradeNumbers);
//        System.out.println("Total Profit = " + totalProfit);
//        System.out.println();
        return totalProfit;
    }

    private static double RiskFreeAgent2(){
        double totalProfit = 0.0;
        int j = 0;
        double base = unitToCapital * (totalCapital + totalProfit);

        Random r = new Random();
        for(int i = 0; i < numberOfTrades; i++){

            if(mode(r.nextInt()) > profitablePercent){

                totalProfit -= base * TP / rewardToRisk;

                base = base * 2;

                if(totalProfit < -totalCapital) {
                    System.out.println("bankrupt RiskFreeStrategy!");
                    return (-totalCapital - 1);
                }

            }else {
                totalProfit += base *  TP;

                j  += 1;
                base = (j + 1) * unitToCapital * (totalCapital + totalProfit);

                if(j > lockedProfit) {
                    j = 0;
                    base = unitToCapital * (totalCapital + totalProfit);
                }
            }

            if(base > 2 * (totalCapital + totalProfit) * unitToCapital * lockedProfit)
                base = 2 * (totalProfit + totalCapital) * unitToCapital * lockedProfit;
        }
//        System.out.println("StrategyMartingale:\n trade numbers = " + tradeNumbers);
//        System.out.println("Total Profit = " + totalProfit);
//        System.out.println();
        return totalProfit;
    }



    private static int mode(int a){
        return a % 100 < 0 ? a % 100 + 100 : a % 100;
    }

    public static void main(String[] args){
//        printResult("agent");
//        printResult("riskfreeagent");
//        printResult("riskfreeagent2");
//        printResult("mehrad");
//        printResult("secondary");
//        printResult("martingale");
        printResult("mehrad1");
        printResult("mehrad2");
    }

    public static void printResult(String strategyName){
        double[] a = new double[numberOfTests];
        double avrg1 = 0;
        double min1 = 1000 * totalCapital;
        double max1 = -totalCapital * 1000;
        int loss = 0;

        for (int i = 0; i < numberOfTests; i++) {

            switch (strategyName){
                case "mehrad":
                    a[i] = StrategyMehrad();
                    break;

                case "martingale":
                    a[i] = StrategyMartingale();
                    break;

                case "secondary":
                    a[i] = Strategy1();
                    break;

                case "agent":
                    a[i] = StrategyAgent();
                    break;

                case "riskfreeagent":
                    a[i] = RiskFreeAgent();
                    break;

                case "riskfreeagent2":
                    a[i] = RiskFreeAgent2();
                    break;

                case "mehrad1":
                    a[i] = StrategyMehrad1();
                    break;
                case "mehrad2":
                    a[i] = StrategyMehrad2();
            }

            avrg1 += a[i];

            if(a[i] > max1) max1 = a[i];
            if(a[i] < min1) min1 = a[i];

            if(a[i] < 0) loss++;
        }

        avrg1 = avrg1 / numberOfTests;

        double var1 = 0;
        for (int i = 0; i < numberOfTests; i++) {
            var1 += Math.pow(a[i] - avrg1, 2)/numberOfTests;
        }

        switch (strategyName){
            case "mehrad":
                System.out.println("Mehrad:");
                break;

            case "martingale":
                System.out.println("Martingale:");
                break;

            case "secondary":
                System.out.println("Secondary:");
                break;

            case "agent":
                System.out.println("Agent:");
                break;

            case "riskfreeagent":
                System.out.println("RiskFreeAgent:");
                break;

            case "riskfreeagent2":
                System.out.println("RiskFreeAgent2:");
                break;

            case "mehrad1":
                System.out.println("Strategy1:");
                break;

            case "mehrad2":
                System.out.println("Strategy2:");
                break;
        }
        System.out.println("Avr:" + avrg1);
        System.out.println("Var: " + var1);
        System.out.println("Max: " + max1 + " Min: " + min1);
        System.out.println("Lost trades: " + loss + "\n");
    }
}

class YearTest{
    public static void main(String[] args) {
        Strategy2();
    }

    public static void Strategy1(){
        Scanner scanner = new Scanner(System.in);
        String input = "";
        List<Double> profits = new ArrayList<>();

        Double totalCapital = 100.0;
        Double baseUnit = 0.02;
        Double currentUnit = baseUnit;
        Double currentProfit = 0.0;

        Integer level = 0;
        Integer maxLevel = 8;
        Integer leverage = 20;

        List<Double> positive = new ArrayList<>();
        List<Double> negative = new ArrayList<>();

        double positiveSum = 0.0, negativeSum = 0.0;

        int index = 0;

        while(!input.equals("exit")){
            input = scanner.nextLine();
            if(input.equals("exit")) break;
            currentProfit = Double.parseDouble(input);
            if(index%2 == 0) {
                profits.add(currentProfit / 100);
                if(currentProfit < 0) {
                    negative.add(currentProfit);
                    negativeSum += currentProfit;
                }
                else {
                    positive.add(currentProfit);
                    positiveSum += currentProfit;
                }
            }
            index++;
        }

        Collections.reverse(profits);

        for (Double profit: profits) {

            if(level >= maxLevel ){

                level = 0;
                currentUnit = baseUnit;
            }

            if(profit >= 0){
                totalCapital += totalCapital * (currentUnit) * profit * leverage;
                currentUnit += baseUnit;
                level++;
            }
            if(profit < 0) {
                totalCapital += totalCapital * (currentUnit) * profit * leverage;
//                currentUnit = baseUnit;
//                level = 0;
//
//                if(level < 0){
//                    level = 0;
//                    currentUnit = baseUnit;
//                }
            }

            System.out.println(totalCapital);

            System.out.println("Unit: " + currentUnit + " Level: " + level);
        }

        System.out.println("total Capital: " + totalCapital);
        System.out.println("Positive average: " + positiveSum/positive.size() );
        System.out.println("Negative average: " + negativeSum/negative.size() );
    }

    public static void Strategy2(){
        Scanner scanner = new Scanner(System.in);
        String input = "";
        List<Double> profits = new ArrayList<>();

        Double totalCapital = 100.0;
        Double baseUnit = 0.001;
        Double currentUnit = baseUnit;
        Double currentProfit = 0.0;

        Integer level = 0;
        Integer maxLevel = 8;
        Integer leverage = 100;

        List<Double> positive = new ArrayList<>();
        List<Double> negative = new ArrayList<>();

        double positiveSum = 0.0, negativeSum = 0.0;

        int index = 0;

        while(!input.equals("exit")){
            input = scanner.nextLine();
            if(input.equals("exit")) break;
            currentProfit = Double.parseDouble(input);
            if(index%2 == 0) {
                profits.add(currentProfit / 100);
                if(currentProfit < 0) {
                    negative.add(currentProfit);
                    negativeSum += currentProfit;
                }
                else {
                    positive.add(currentProfit);
                    positiveSum += currentProfit;
                }
            }
            index++;
        }

        Collections.reverse(profits);

        for (Double profit: profits) {

            if(profit >= 0){
                totalCapital += totalCapital * (currentUnit) * profit * leverage;
                currentUnit = baseUnit;
            }
            if(profit < 0) {
                totalCapital += Math.max(totalCapital * (currentUnit) * profit * leverage, -totalCapital * (currentUnit));
                currentUnit = currentUnit *2;
                if(currentUnit > 1) {
                    System.out.println("Bankrupt :))");
                    return;
                }
//                currentUnit = baseUnit;
//                level = 0;
//
//                if(level < 0){
//                    level = 0;
//                    currentUnit = baseUnit;
//                }
            }

            System.out.println(totalCapital);

            System.out.println("Unit: " + currentUnit + " Level: " + level);
        }

        System.out.println("total Capital: " + totalCapital);
        System.out.println("Positive average: " + positiveSum/positive.size() );
        System.out.println("Negative average: " + negativeSum/negative.size() );
    }
}