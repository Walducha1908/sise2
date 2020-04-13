package Calculations;

import Model.Instances.Instance;
import Model.Instances.TrainingInstancesContainer;
import Model.Results.SecondLayerResultSet;

public class Errors {
    public static double calculateErrorOffLine(NeuralNetwork network) {
        double error = 0;
        double numberOfTrainingInstances = TrainingInstancesContainer.trainingInstancesList.size();
        for (int i = 0; i < numberOfTrainingInstances; i++) {
            SecondLayerResultSet response = network.calculateResponse(TrainingInstancesContainer.trainingInstancesList.get(i));
            double err = 0;
            err += Math.pow((TrainingInstancesContainer.trainingInstancesList.get(i).getReferencePoint().getX() -
                        response.getResultSet().get(0)), 2);
            err += Math.pow((TrainingInstancesContainer.trainingInstancesList.get(i).getReferencePoint().getY() -
                    response.getResultSet().get(1)), 2);
            error+= Math.sqrt(err);
        }
        error = (error / (numberOfTrainingInstances));
        return error;
    }

    public static double distance (Instance instance, SecondLayerResultSet resultSet) {
        double result = 0;
        result += Math.pow(instance.getReferencePoint().getX() - resultSet.getResultSet().get(0), 2);
        result += Math.pow(instance.getReferencePoint().getY() - resultSet.getResultSet().get(1), 2);
        return Math.sqrt(result);
    }
}
