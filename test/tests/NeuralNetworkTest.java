package tests;

import java.util.Random;
import neuralnetwork.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tnc
 */
public class NeuralNetworkTest {
    
    public NeuralNetworkTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    
    @Test
    public void testNeuralNetwork_XOR()
    {
        Matrix[] inputs = new Matrix[4];
        inputs[0] = new Matrix(new double[][]{ {0.0},{0.0} });
        inputs[1] = new Matrix(new double[][]{ {0.0},{1.0} });
        inputs[2] = new Matrix(new double[][]{ {1.0},{0.0} });
        inputs[3] = new Matrix(new double[][]{ {1.0},{1.0} });
        
        Matrix[] targets = new Matrix[4];
        targets[0] = new Matrix(new double[][]{ {0.0} });
        targets[1] = new Matrix(new double[][]{ {1.0} });
        targets[2] = new Matrix(new double[][]{ {1.0} });
        targets[3] = new Matrix(new double[][]{ {0.0} });

        Random rnd = new Random();
        
        NeuralNetwork nn = new NeuralNetwork(
            new int[]{2, 2, 1},
            new ActivationType[]{
                ActivationType.SIGMOID,
                ActivationType.SIGMOID
            },
            LossType.MSE,
            0.1
        );
        
        // Train randomly
        for(int i = 0; i < 50000; i++)
        {
            int index = rnd.nextInt(4);
            nn.setInput(inputs[index]);
            nn.setTarget(targets[index]);
            nn.feedForward();
            nn.backpropagation();
        }
        
        // Test
        for(int i = 0; i < 4; i++)
        {
            nn.setInput(inputs[i]);
            nn.setTarget(targets[i]);
            nn.feedForward();

            System.out.print("Output: " + nn.getOutput());
            System.out.println(String.format("Error: %.10f\n", nn.getLoss()));
        }
    }
    
    @Test
    public void testActivationSigmoid()
    {
        Matrix m = new Matrix(new double[][] {
            { 0.14311,  0.0,                -1.0 },
            { 1.0,      Double.MAX_VALUE,   -Double.MAX_VALUE }
        });
        
        Matrix sigmoid = new Matrix(new double[][] {
            { 0.5357165631243429,   0.5,  0.2689414213699951 },
            { 0.7310585786300049,   1.0,  0.0 }
        });
        
        Matrix derSigmoid = new Matrix(new double[][] {
            { 0.248724327118584,   0.25,    0.196611933241481 },
            { 0.196611933241481,   0.0,     0.0 }
        });
        
        assertEquals(sigmoid, ActivationFunc.sigmoid(m));
        assertEquals(derSigmoid, ActivationFunc.derSigmoid(sigmoid));
    }
    
    @Test
    public void testActivationTanh()
    {
        Matrix m = new Matrix(new double[][] {
            { -0.91809,     0.0,                -1.0 },
            { 1.0,          Double.MAX_VALUE,   -Double.MAX_VALUE }
        });

        Matrix tanh = new Matrix(new double[][] {
            { -0.724992592123833,   0.0,    -0.761594155955764 },
            { 0.761594155955764,   1.0,     -1.0 }
        });
        
        Matrix derTanh = new Matrix(new double[][] {
            { 0.47438574136557,   1.0,     0.41997434161403 },
            { 0.41997434161403,   0.0,     0.0 }
        });
                
        assertEquals(tanh, ActivationFunc.tanh(m));
        assertEquals(derTanh, ActivationFunc.derTanh(tanh));
    }
}