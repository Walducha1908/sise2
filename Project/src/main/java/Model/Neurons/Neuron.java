package Model.Neurons;

import Model.Results.FirstLayerResultSet;
import Model.Instances.Instance;

public interface Neuron {
    public void initializeWages();
    public double calculate(Instance instance);
    public double calculate(FirstLayerResultSet resultSet);
    public double activate(Instance instance);
    public double activate(FirstLayerResultSet resultSet);
    public double derivative(double value);
}
