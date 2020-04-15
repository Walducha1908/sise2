package Model.Instances;

public class TestingInstance extends Instance {
    public TestingInstance() {
        super();
    }

    @Override
    public String toString() {
        return "TestingInstance{" + "\n" +
                "measurementPoints=" + measurementPoints + "\n" +
                ", referencePoint=" + referencePoint +
                '}';
    }
}
