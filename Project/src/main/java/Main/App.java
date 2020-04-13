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
        System.out.println( "Started reading data...\n" );
        Manager.readAndPrepareTrainingData();
        Manager.createNeuralNetworkAndStartLearning();
    }
}
