package Data;

import Main.Settings;
import Model.Instances.TrainingInstance;
import Model.Instances.TrainingInstancesContainer;
import Model.MeasurementPoint;
import Model.ReferencePoint;

import java.util.LinkedList;

public class DataExtractor {
    public DataExtractor() {}
    /*
        I decided to ignore first n training points where n is number of points considered.
        That's why we have "int i = Settings.numberOfPointsConsidered - 1" in the loop.
         */
    public boolean extractAllTrainingData(
            LinkedList<MeasurementPoint> readMeasurementPoints,
            LinkedList<ReferencePoint> readReferencePoints) {

        TrainingInstancesContainer trainingInstancesContainer = new TrainingInstancesContainer();
        for (int i = Settings.numberOfPointsConsidered - 1; i < readMeasurementPoints.size(); i++) {
            TrainingInstance trainingInstance = new TrainingInstance();

            // Firstly, add n measurement points.
            for (int j = Settings.numberOfPointsConsidered - 1; j >= 0; j--) {
                if (!trainingInstance.addMeasurementPoint(readMeasurementPoints.get(i - j))) {
                    System.out.println("Too many measurement points! We have reached max measurements points capacity!");
                    return false;
                }
            }

            // Secondly, add reference point.
            trainingInstance.setReferencePoint(readReferencePoints.get(i));

            // Store training instance in container
            TrainingInstancesContainer.trainingInstancesList.add(trainingInstance);
        }
        return true;
    }
}
