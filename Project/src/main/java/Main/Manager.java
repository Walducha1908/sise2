package Main;

import Calculations.Errors;
import Calculations.NeuralNetwork;
import Data.DataExtractor;
import Data.DataReader;
import Data.DataWriter;
import Data.NetworkCreator;
import Model.Instances.TestingInstance;
import Model.Instances.TestingInstancesContainer;
import Model.Instances.TrainingInstancesContainer;
import Model.MeasurementPoint;
import Model.ReferencePoint;

import java.util.LinkedList;

public class Manager {

    public static void readAndPrepareTrainingData() {
        DataReader dataReader = new DataReader();
        dataReader.readAllTrainingData();
        dataReader.readAllTestingData();
        System.out.println(dataReader.getMeasurementPointsList().size() + " measurement points have been read.");
        System.out.println(dataReader.getReferencePointsList().size() + " reference points have been read.");
        extractTrainingData(dataReader.getMeasurementPointsList(), dataReader.getReferencePointsList(),
                dataReader.getTestingMeasurementPointsList(), dataReader.getTestingReferencePointsList());
    }

    public static void extractTrainingData(
            LinkedList<MeasurementPoint> readMeasurementPoints,
            LinkedList<ReferencePoint> readReferencePoints,
            LinkedList<MeasurementPoint> readTestingMeasurementPoints,
            LinkedList<ReferencePoint> readTestingReferencePoints) {
        DataExtractor dataExtractor = new DataExtractor();
        if (dataExtractor.extractAllTrainingData(readMeasurementPoints, readReferencePoints)) {
            System.out.println(TrainingInstancesContainer.trainingInstancesList.size() + " training instances have been prepared.");
        } else {
            System.out.println("Error occured while preparing training instances.");
        }

        if (dataExtractor.extractAllTestingData(readTestingMeasurementPoints, readTestingReferencePoints)) {
            System.out.println(TestingInstancesContainer.testingInstancesList.size() + " testing instances have been prepared.");
        } else {
            System.out.println("Error occured while preparing testing instances.");
        }
    }

    public static void createNeuralNetworkAndStartLearning() {
        NeuralNetwork neuralNetwork;
        NeuralNetwork startingNeuralNetwork;

        if (Settings.recreateFromFile) {
            neuralNetwork = NetworkCreator.recreateNetworkFromFile();
        } else {
            neuralNetwork = new NeuralNetwork();
        }
        double lastError = 1000000000;

        startingNeuralNetwork = new NeuralNetwork(neuralNetwork);

        for (int i=0; i<Settings.maxEras; i++) {
            double currentError = Errors.calculateErrorOffLine(neuralNetwork);
            double testingError = Errors.calculateTestingError(neuralNetwork);
            double drop = lastError - currentError;
            System.out.println("Current error: " + currentError + " Testing Error: " + testingError + " Drop: " + drop);

            if ((Math.abs(drop) < Settings.stopDifference) || (currentError > 20000)) {
                System.out.println("Testing Error: " + Errors.calculateTestingError(neuralNetwork));
                if (testingError < 300) {
                    DataWriter.saveResults(neuralNetwork, startingNeuralNetwork, i);
                }
                break;
            }

            if (i > 200 && currentError > 1000) {
                break;
            }

            lastError = Errors.calculateErrorOffLine(neuralNetwork);
            neuralNetwork.era();
        }
    }

}
