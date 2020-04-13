package Calculations;

import Main.Settings;
import Model.Instances.TrainingInstance;
import Model.Instances.TrainingInstancesContainer;
import Model.Results.FirstLayerResultSet;
import Model.Instances.Instance;
import Model.Neurons.FirstLayerNeuron;
import Model.Neurons.SecondLayerNeuron;
import Model.Results.ResultSet;
import Model.Results.SecondLayerResultSet;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class NeuralNetwork {
    LinkedList<FirstLayerNeuron> firstLayerNeurons;
    LinkedList<SecondLayerNeuron> secondLayerNeurons;

    public NeuralNetwork() {
        firstLayerNeurons = new LinkedList<FirstLayerNeuron>();
        secondLayerNeurons = new LinkedList<SecondLayerNeuron>();

        for (int i = 0; i < Settings.numberOfNeuronsInFirstLayer; i++) {
            firstLayerNeurons.add(new FirstLayerNeuron(i));
        }

        for (int i = 0; i < Settings.numberOfNeuronsInSecondLayer; i++) {
            secondLayerNeurons.add(new SecondLayerNeuron(i));
        }

    }

    public SecondLayerResultSet calculateResponse(Instance instance) {
        FirstLayerResultSet resultSet = new FirstLayerResultSet();
        SecondLayerResultSet finalResults = new SecondLayerResultSet();

        for (int i = 0; i < firstLayerNeurons.size(); i++) {
            resultSet.addResult(firstLayerNeurons.get(i).activate(instance));
        }

        for (int i = 0; i < secondLayerNeurons.size(); i++) {
            finalResults.addResult(secondLayerNeurons.get(i).activate(resultSet));
        }

        return finalResults;
    }

    public double secondLayerErrorHelper(SecondLayerNeuron neuron, Instance instance, FirstLayerResultSet resultSet,
                                         SecondLayerResultSet response) {
        double sum = neuron.activate(resultSet);
        double derivative = neuron.derivative(sum);
        double difference = 0;
        if (neuron.getIndex() == 0) {
            difference = instance.getReferencePoint().getX() - response.getResultSet().get(0);
        } else if (neuron.getIndex() == 1) {
            difference = instance.getReferencePoint().getY() - response.getResultSet().get(1);
        }
//        difference = Errors.distance(instance, response);
        return (derivative * difference);
    }

    public double firstLayerErrorHelper(FirstLayerNeuron neuron, Instance instance, FirstLayerResultSet resultSet,
                                        SecondLayerResultSet response) {
        double sum = neuron.activate(instance);
        double derivative = neuron.derivative(sum);
        double secondLayerErrorHelperSum = 0;
        for (int i = 0; i < secondLayerNeurons.size(); i++) {
            secondLayerErrorHelperSum += secondLayerErrorHelper(secondLayerNeurons.get(i), instance, resultSet, response);
        }
        return (derivative * secondLayerErrorHelperSum);
    }

    public FirstLayerResultSet calculateFirstLayerResponse(Instance instance) {
        FirstLayerResultSet resultSet = new FirstLayerResultSet();

        for (int i = 0; i < firstLayerNeurons.size(); i++) {
            resultSet.addResult(firstLayerNeurons.get(i).activate(instance));
        }

        return resultSet;
    }

    public double secondLayerWageDeltaForInstance(SecondLayerNeuron neuron, int wageIndex, Instance instance,
                                                  SecondLayerResultSet response) {
        FirstLayerResultSet resultSet = calculateFirstLayerResponse(instance);
        double errorHelper = secondLayerErrorHelper(neuron, instance, resultSet, response);
        return errorHelper * resultSet.getResultSet().get(wageIndex);
    }

    public double firstLayerWageDeltaForInstance(FirstLayerNeuron neuron, int wageIndex, Instance instance,
                                                 SecondLayerResultSet response) {
        FirstLayerResultSet resultSet = calculateFirstLayerResponse(instance);
        double errorHelper = firstLayerErrorHelper(neuron, instance, resultSet, response);
        double signal = 0;
        if (wageIndex % 2 == 0) {
            signal = instance.getMeasurementPoints().get(wageIndex / 2).getX();
        } else {
            signal = instance.getMeasurementPoints().get(wageIndex / 2).getY();
        }
        return errorHelper * signal;
    }

    public double secondLayerWageDeltaForAllInstancesOffLine(SecondLayerNeuron neuron, int wageIndex,
                                                             LinkedList<SecondLayerResultSet> responses) {
        double deltaWage = 0;
        double numberOfTrainingInstances = TrainingInstancesContainer.trainingInstancesList.size();
        for (int i = 0; i < numberOfTrainingInstances; i++) {
            deltaWage += secondLayerWageDeltaForInstance(neuron, wageIndex,
                    TrainingInstancesContainer.trainingInstancesList.get(i), responses.get(i));
        }
        return (deltaWage / numberOfTrainingInstances);
    }

    public double firstLayerWageDeltaForAllInstancesOffLine(FirstLayerNeuron neuron, int wageIndex,
                                                            LinkedList<SecondLayerResultSet> responses) {
        double deltaWage = 0;
        double numberOfTrainingInstances = TrainingInstancesContainer.trainingInstancesList.size();
        for (int i = 0; i < numberOfTrainingInstances; i++) {
            deltaWage += firstLayerWageDeltaForInstance(neuron, wageIndex,
                    TrainingInstancesContainer.trainingInstancesList.get(i), responses.get(i));
        }
        return (deltaWage / numberOfTrainingInstances);
    }

    public void era() {

//        Collections.shuffle(TrainingInstancesContainer.trainingInstancesList);
//        for (int m = 0; m < TrainingInstancesContainer.trainingInstancesList.size(); m++) {
//
//            SecondLayerResultSet resultSet = (calculateResponse(TrainingInstancesContainer.trainingInstancesList.get(m)));
//            LinkedList<LinkedList<Double>> firstLayerNeuronWagesDeltas = new LinkedList<LinkedList<Double>>();
//            LinkedList<LinkedList<Double>> secondLayerNeuronWagesDeltas = new LinkedList<LinkedList<Double>>();
//
//            for (int i = 0; i < firstLayerNeurons.size(); i++) {
//                LinkedList<Double> wagesDeltas = new LinkedList<Double>();
//                for (int j = 0; j < firstLayerNeurons.get(i).getWages().size(); j++) {
//                    wagesDeltas.add(firstLayerNeurons.get(i).getWages().get(j) +
//                            Settings.learningFactor * firstLayerWageDeltaForInstance(firstLayerNeurons.get(i), j,
//                                    TrainingInstancesContainer.trainingInstancesList.get(m), resultSet));
//                }
//                firstLayerNeuronWagesDeltas.add(wagesDeltas);
//            }
//
//            for (int i = 0; i < secondLayerNeurons.size(); i++) {
//                LinkedList<Double> wagesDeltas = new LinkedList<Double>();
//                for (int j = 0; j < secondLayerNeurons.get(i).getWages().size(); j++) {
//                    wagesDeltas.add(secondLayerNeurons.get(i).getWages().get(j) +
//                            Settings.learningFactor * secondLayerWageDeltaForInstance(secondLayerNeurons.get(i), j,
//                                    TrainingInstancesContainer.trainingInstancesList.get(m), resultSet));
//                }
//                secondLayerNeuronWagesDeltas.add(wagesDeltas);
//            }
//
//            if (m % 1540 == 0) {
//                System.out.println("Current error: " + Errors.calculateErrorOffLine(this));
//            }
//
//            for (int i = 0; i < firstLayerNeurons.size(); i++) {
//                firstLayerNeurons.get(i).setWages(firstLayerNeuronWagesDeltas.get(i));
//            }
//
//            for (int i = 0; i < secondLayerNeurons.size(); i++) {
//                secondLayerNeurons.get(i).setWages(secondLayerNeuronWagesDeltas.get(i));
//            }
//
//        }

//        System.out.println("All data presented! \n");


        System.out.println("Current error: " + Errors.calculateErrorOffLine(this));
        Collections.shuffle(TrainingInstancesContainer.trainingInstancesList);

        LinkedList<SecondLayerResultSet> responses = new LinkedList<SecondLayerResultSet>();
        for (int m = 0; m < TrainingInstancesContainer.trainingInstancesList.size(); m++) {
            responses.add(calculateResponse(TrainingInstancesContainer.trainingInstancesList.get(m)));
        }

        LinkedList<LinkedList<Double>> firstLayerNeuronWagesDeltas = new LinkedList<LinkedList<Double>>();
        for (int i = 0; i < firstLayerNeurons.size(); i++) {
            LinkedList<Double> wagesDeltas = new LinkedList<Double>();
            for (int j = 0; j < firstLayerNeurons.get(i).getWages().size(); j++) {
                wagesDeltas.add(firstLayerNeurons.get(i).getWages().get(j) +
                        Settings.learningFactor * firstLayerWageDeltaForAllInstancesOffLine(firstLayerNeurons.get(i), j, responses));
            }
            firstLayerNeuronWagesDeltas.add(wagesDeltas);
        }

        LinkedList<LinkedList<Double>> secondLayerNeuronWagesDeltas = new LinkedList<LinkedList<Double>>();
        for (int i = 0; i < secondLayerNeurons.size(); i++) {
            LinkedList<Double> wagesDeltas = new LinkedList<Double>();
            for (int j = 0; j < secondLayerNeurons.get(i).getWages().size(); j++) {
                wagesDeltas.add(secondLayerNeurons.get(i).getWages().get(j) +
                        Settings.learningFactor * secondLayerWageDeltaForAllInstancesOffLine(secondLayerNeurons.get(i), j, responses));
            }
            secondLayerNeuronWagesDeltas.add(wagesDeltas);
        }

        for (int i = 0; i < firstLayerNeurons.size(); i++) {
            firstLayerNeurons.get(i).setWages(firstLayerNeuronWagesDeltas.get(i));
        }

        for (int i = 0; i < secondLayerNeurons.size(); i++) {
            secondLayerNeurons.get(i).setWages(secondLayerNeuronWagesDeltas.get(i));
        }

    }


}
