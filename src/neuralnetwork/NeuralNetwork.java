package neuralnetwork;

/**
 *
 * @author tnc
 */
public class NeuralNetwork
{
    private final Layer[] layers;
    private Matrix target;
    
    private final double learningRate;
    private double loss;
    private final LossType lossType;
    
    public NeuralNetwork(
        int[] topology,
        ActivationType[] actTypes,
        LossType lossType,
        double learningRate
    ){
        layers = new Layer[topology.length - 1];
        this.lossType = lossType;
        this.learningRate = learningRate;
        
        for (int i = 0; i < layers.length; i++)
        {
            layers[i] = new Layer(topology[i], topology[i + 1], actTypes[i]);
            layers[i].setNetwork(this);
            
            if (i > 0)
            {
                layers[i].setInputLayer(layers[i - 1]);
                layers[i - 1].setOutputLayer(layers[i]);
            }
        }
    }
    
    // Copy constructor.
    public NeuralNetwork(NeuralNetwork other)
    {
        layers = new Layer[other.layers.length];
        learningRate = other.learningRate;
        lossType = other.lossType;
        
        for(int i = 0; i < other.layers.length; ++i)
        {
            layers[i] = new Layer(other.layers[i]);
            layers[i].setNetwork(this);
                    
            if (i > 0)
            {
                layers[i].setInputLayer(layers[i - 1]);
                layers[i - 1].setOutputLayer(layers[i]);
            }
        }
    }
    
    public void feedForward()
    {
        for(int i = 0; i < layers.length; i++)
        {
            layers[i].feedForward();
        }
    }
    
    public void backpropagation()
    {
        for(int i = layers.length - 1; i >= 0; i--)
        {
            layers[i].backpropagation();
        }
    }
    
    public void calcLoss()
    {
        Matrix output = layers[layers.length - 1].getOutput();
        
        switch(lossType)
        {
            case MSE:
                loss = LossFunction.mse(output, target);
                break;
            case CROSS_ENTROPY:
                loss = LossFunction.crossEntropy(output, target);
                break;
            default:
                System.err.println("Error: Undefined loss type, cannot calculate loss.");
                loss = -1.0;
                break;
        }
    }
    
    public void setInput(Matrix input) {
        layers[0].setInput(new Matrix(input));
    }

    public void setTarget(Matrix target) {
        this.target = new Matrix(target);
    }
    
    public Matrix getOutput() {
        return layers[layers.length - 1].getOutput();
    }
    
    public double getLoss() {
        return loss;
    }
    
    public Matrix getTarget() {
        return target;
    }
    
    public Layer[] getLayers() {
        return layers;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public LossType getLossType() {
        return lossType;
    }    
}
