package Main;

import Model.Instances.TrainingInstancesContainer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        for (int i = 0; i < 10000; i++) {
            Settings.startFileOffset = (i % 12) + 1;
            Settings.numberOfNeuronsInFirstLayer = 6 + (i % 3);
            Settings.numberOfNeuronsInSecondLayer = 6 + (i*2 % 3);
            System.out.println("\nStarted reading data...\n");
            Manager.readAndPrepareTrainingData();
            Manager.createNeuralNetworkAndStartLearning();
        }
    }
}
