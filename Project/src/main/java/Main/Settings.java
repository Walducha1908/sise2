package Main;

public class Settings {
    public static String pathToData = "../Data/";
    public static String pathToReports = "../Reports/";
    public static String creationFile = "../BestNetworks/LessThan200.xlsx";
    public static boolean recreateFromFile = false;
    public static int numberOfPointsConsidered = 4;
    public static int numberOfNeuronsInFirstLayer = 7;
    public static int numberOfNeuronsInSecondLayer = 7;
    public static double learningFactor = 0.05;
    public static double stopDifference = 0.01;
    public static int maxEras = 100000;
    public static double dataDivisor = 6943;
    public static int filesToRead = 1;
    public static int startFileOffset = 1;
}
