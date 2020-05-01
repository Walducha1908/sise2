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
        for (int i = 11; i >=0; i--) {
            Settings.startFileOffset = (i % 12);
//            Settings.numberOfNeuronsInFirstLayer = 6 + (i % 4);
//            Settings.numberOfNeuronsInSecondLayer = 6 + (i*2 % 4);
            System.out.println("\nStarted reading data...\n");
            System.out.println("File number " + Settings.startFileOffset);
            Manager.readAndPrepareTrainingData();
            Manager.createNeuralNetworkAndStartLearning();
        }
    }
}
