package Data;

import Main.Settings;
import Model.MeasurementPoint;
import Model.ReferencePoint;

import java.util.LinkedList;

public class DataCleaner {
    public static void repairMeasurementPoint(LinkedList<MeasurementPoint> measurementPoints) {
        int size = measurementPoints.size();
        double medianX = 300 / Settings.dataDivisor;
        double medianY = 400 / Settings.dataDivisor;

        int numberOfChanged = 0;
        for (int i = 1; i < measurementPoints.size() - 1; i++) {
            if (Math.abs(measurementPoints.get(i+1).getX() - measurementPoints.get(i).getX()) > medianX &&
                    Math.abs(measurementPoints.get(i-1).getX() - measurementPoints.get(i).getX()) > medianX) {
                measurementPoints.get(i).setX((measurementPoints.get(i+1).getX() + measurementPoints.get(i-1).getX())/2);
                numberOfChanged++;
            }
            if (Math.abs(measurementPoints.get(i+1).getY() - measurementPoints.get(i).getY()) > medianY &&
                    Math.abs(measurementPoints.get(i-1).getY() - measurementPoints.get(i).getY()) > medianY) {
                measurementPoints.get(i).setY((measurementPoints.get(i+1).getY() + measurementPoints.get(i-1).getY())/2);
                numberOfChanged++;
            }
        }
        System.out.println("Repaired " + numberOfChanged + " measurement points!");

    }
}
