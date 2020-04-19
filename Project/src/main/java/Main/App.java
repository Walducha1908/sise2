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
        for (int i = 0; i < 1000; i++) {
            Settings.startFileOffset = (i % 12) + 1;
            Settings.numberOfNeuronsInFirstLayer = 5 + (i % 5);
            Settings.numberOfNeuronsInSecondLayer = 5 + (i*2 % 5);
            System.out.println("\nStarted reading data...\n");
            System.out.println("File number " + Settings.startFileOffset);
            Manager.readAndPrepareTrainingData();
            Manager.createNeuralNetworkAndStartLearning();
        }
    }
}
