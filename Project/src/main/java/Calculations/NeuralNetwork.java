package Calculations;

import Main.Settings;
import Model.Instances.TrainingInstancesContainer;
import Model.Neurons.ThirdLayerNeuron;
import Model.Results.FirstLayerResultSet;
import Model.Instances.Instance;
import Model.Neurons.FirstLayerNeuron;
import Model.Neurons.SecondLayerNeuron;
import Model.Results.SecondLayerResultSet;
import Model.Results.ThirdLayerResultSet;
import sun.awt.image.ImageWatched;

import java.util.Collections;
import java.util.LinkedList;

public class NeuralNetwork {
    LinkedList<FirstLayerNeuron> firstLayerNeurons;
    LinkedList<SecondLayerNeuron> secondLayerNeurons;
    LinkedList<ThirdLayerNeuron> thirdLayerNeurons;

    public NeuralNetwork() {
        firstLayerNeurons = new LinkedList<FirstLayerNeuron>();
        secondLayerNeurons = new LinkedList<SecondLayerNeuron>();
        thirdLayerNeurons = new LinkedList<ThirdLayerNeuron>();

        for (int i = 0; i < Settings.numberOfNeuronsInFirstLayer; i++) {
            firstLayerNeurons.add(new FirstLayerNeuron(i));
        }

        for (int i = 0; i < Settings.numberOfNeuronsInSecondLayer; i++) {
            secondLayerNeurons.add(new SecondLayerNeuron(i));
        }

        for (int i = 0; i < 2; i++) {
            thirdLayerNeurons.add(new ThirdLayerNeuron(i));
        }

    }

    public ThirdLayerResultSet calculateResponse(Instance instance) {
        FirstLayerResultSet firstLayerResultSet = new FirstLayerResultSet();
        SecondLayerResultSet secondLayerResultSet = new SecondLayerResultSet();
        ThirdLayerResultSet thirdLayerResultSet = new ThirdLayerResultSet();

        for (int i = 0; i < firstLayerNeurons.size(); i++) {
            firstLayerResultSet.addResult(firstLayerNeurons.get(i).activate(instance));
        }

        for (int i = 0; i < secondLayerNeurons.size(); i++) {
            secondLayerResultSet.addResult(secondLayerNeurons.get(i).activate(firstLayerResultSet));
        }

        for (int i = 0; i < thirdLayerNeurons.size(); i++) {
            thirdLayerResultSet.addResult(thirdLayerNeurons.get(i).activate(secondLayerResultSet));
        }

        return thirdLayerResultSet;
    }

    public double thirdLayerErrorHelper(ThirdLayerNeuron neuron, Instance instance, SecondLayerResultSet resultSet,
                                        ThirdLayerResultSet response) {
        double sum = neuron.activate(resultSet);
        double derivative = neuron.derivative(sum);
        double difference = 0;
        if (neuron.getIndex() == 0) {
            difference = instance.getReferencePoint().getX() - response.getResultSet().get(0);
        } else if (neuron.getIndex() == 1) {
            difference = instance.getReferencePoint().getY() - response.getResultSet().get(1);
        }
        return (derivative * difference);
    }

    public double secondLayerErrorHelper(SecondLayerNeuron neuron, Instance instance, FirstLayerResultSet firstLayerResultSet,
                                         SecondLayerResultSet secondLayerResultSet, ThirdLayerResultSet response) {
        double sum = neuron.activate(firstLayerResultSet);
        double derivative = neuron.derivative(sum);
        double thirdLayerErrorHelperSum = 0;
        for (int i = 0; i < thirdLayerNeurons.size(); i++) {
            thirdLayerErrorHelperSum += thirdLayerErrorHelper(thirdLayerNeurons.get(i), instance,
                    secondLayerResultSet, response);
        }
        return (derivative * thirdLayerErrorHelperSum);
    }

    public double firstLayerErrorHelper(FirstLayerNeuron neuron, Instance instance, FirstLayerResultSet firstLayerResultSet,
                                        SecondLayerResultSet secondLayerResultSet, ThirdLayerResultSet response) {
        double sum = neuron.activate(instance);
        double derivative = neuron.derivative(sum);
        double secondLayerErrorHelperSum = 0;
        for (int i = 0; i < secondLayerNeurons.size(); i++) {
            secondLayerErrorHelperSum += secondLayerErrorHelper(secondLayerNeurons.get(i), instance,
                    firstLayerResultSet, secondLayerResultSet, response);
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

    public SecondLayerResultSet calculateSecondLayerResponse(FirstLayerResultSet resultSet) {
        SecondLayerResultSet secondLayerResultSet = new SecondLayerResultSet();

        for (int i = 0; i < secondLayerNeurons.size(); i++) {
            secondLayerResultSet.addResult(secondLayerNeurons.get(i).activate(resultSet));
        }

        return secondLayerResultSet;
    }

    public double thirdLayerWageDeltaForInstance(ThirdLayerNeuron neuron, int wageIndex, Instance instance,
                                                 ThirdLayerResultSet response) {
        FirstLayerResultSet firstLayerResultSet = calculateFirstLayerResponse(instance);
        SecondLayerResultSet secondLayerResultSet = calculateSecondLayerResponse(firstLayerResultSet);
        double errorHelper = thirdLayerErrorHelper(neuron, instance,
                secondLayerResultSet, response);
        return errorHelper * secondLayerResultSet.getResultSet().get(wageIndex);
    }

    public double secondLayerWageDeltaForInstance(SecondLayerNeuron neuron, int wageIndex, Instance instance,
                                                 ThirdLayerResultSet response) {
        FirstLayerResultSet firstLayerResultSet = calculateFirstLayerResponse(instance);
        SecondLayerResultSet secondLayerResultSet = calculateSecondLayerResponse(firstLayerResultSet);
        double errorHelper = secondLayerErrorHelper(neuron, instance,
                firstLayerResultSet, secondLayerResultSet, response);
        return errorHelper * firstLayerResultSet.getResultSet().get(wageIndex);
    }

    public double firstLayerWageDeltaForInstance(FirstLayerNeuron neuron, int wageIndex, Instance instance,
                                                 ThirdLayerResultSet response) {
        FirstLayerResultSet firstLayerResultSet = calculateFirstLayerResponse(instance);
        SecondLayerResultSet secondLayerResultSet = calculateSecondLayerResponse(firstLayerResultSet);
        double errorHelper = firstLayerErrorHelper(neuron, instance, firstLayerResultSet,
                secondLayerResultSet, response);
        double signal = 0;
        if (wageIndex % 2 == 0) {
            signal = instance.getMeasurementPoints().get(wageIndex / 2).getX();
        } else {
            signal = instance.getMeasurementPoints().get(wageIndex / 2).getY();
        }
        return errorHelper * signal;
    }

    public double thirdLayerWageDeltaForAllInstancesOffLine(ThirdLayerNeuron neuron, int wageIndex,
                                                            LinkedList<ThirdLayerResultSet> responses) {
        double deltaWage = 0;
        double numberOfTrainingInstances = TrainingInstancesContainer.trainingInstancesList.size();
        for (int i = 0; i < numberOfTrainingInstances; i++) {
            deltaWage += thirdLayerWageDeltaForInstance(neuron, wageIndex,
                    TrainingInstancesContainer.trainingInstancesList.get(i), responses.get(i));
        }
        return (deltaWage / numberOfTrainingInstances);
    }

    public double secondLayerWageDeltaForAllInstancesOffLine(SecondLayerNeuron neuron, int wageIndex,
                                                            LinkedList<ThirdLayerResultSet> responses) {
        double deltaWage = 0;
        double numberOfTrainingInstances = TrainingInstancesContainer.trainingInstancesList.size();
        for (int i = 0; i < numberOfTrainingInstances; i++) {
            deltaWage += secondLayerWageDeltaForInstance(neuron, wageIndex,
                    TrainingInstancesContainer.trainingInstancesList.get(i), responses.get(i));
        }
        return (deltaWage / numberOfTrainingInstances);
    }

    public double firstLayerWageDeltaForAllInstancesOffLine(FirstLayerNeuron neuron, int wageIndex,
                                                            LinkedList<ThirdLayerResultSet> responses) {
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

        LinkedList<ThirdLayerResultSet> responses = new LinkedList<ThirdLayerResultSet>();
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

        LinkedList<LinkedList<Double>> thirdLayerNeuronWagesDeltas = new LinkedList<LinkedList<Double>>();
        for (int i = 0; i < 2; i++) {
            LinkedList<Double> wagesDeltas = new LinkedList<Double>();
            for (int j = 0; j < thirdLayerNeurons.get(i).getWages().size(); j++) {
                wagesDeltas.add(thirdLayerNeurons.get(i).getWages().get(j) +
                        Settings.learningFactor * thirdLayerWageDeltaForAllInstancesOffLine(thirdLayerNeurons.get(i), j, responses));
            }
            thirdLayerNeuronWagesDeltas.add(wagesDeltas);
        }

        for (int i = 0; i < firstLayerNeurons.size(); i++) {
            firstLayerNeurons.get(i).setWages(firstLayerNeuronWagesDeltas.get(i));
        }

        for (int i = 0; i < secondLayerNeurons.size(); i++) {
            secondLayerNeurons.get(i).setWages(secondLayerNeuronWagesDeltas.get(i));
        }

        for (int i = 0; i < thirdLayerNeurons.size(); i++) {
            thirdLayerNeurons.get(i).setWages(thirdLayerNeuronWagesDeltas.get(i));
        }

    }


}
