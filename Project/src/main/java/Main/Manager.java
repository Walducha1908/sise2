package Main;

import Calculations.NeuralNetwork;
import Data.DataExtractor;
import Data.DataReader;
import Model.Instances.TrainingInstancesContainer;
import Model.MeasurementPoint;
import Model.ReferencePoint;

import java.util.LinkedList;

public class Manager {

    public static void readAndPrepareTrainingData() {
        DataReader dataReader = new DataReader();
        dataReader.readAllTrainingData();
        System.out.println(dataReader.getMeasurementPointsList().size() + " measurement points have been read.");
        System.out.println(dataReader.getReferencePointsList().size() + " reference points have been read.");
        extractTrainingData(dataReader.getMeasurementPointsList(), dataReader.getReferencePointsList());
    }

    public static void extractTrainingData(
            LinkedList<MeasurementPoint> readMeasurementPoints,
            LinkedList<ReferencePoint> readReferencePoints) {
        DataExtractor dataExtractor = new DataExtractor();
        if (dataExtractor.extractAllTrainingData(readMeasurementPoints, readReferencePoints)) {
            System.out.println(TrainingInstancesContainer.trainingInstancesList.size() + " training instances have been prepared.");
        } else {
            System.out.println("Error occured while preparing training instances.");
        }
    }

    public static void createNeuralNetworkAndStartLearning() {
        NeuralNetwork neuralNetwork = new NeuralNetwork();

        for (int i=0; i<Settings.maxEras; i++) {
            neuralNetwork.era();
        }
    }

}
