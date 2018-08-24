package datamining;

import java.util.Scanner;
import java.io.File;
import neuralnetwork.ActivationType;
import neuralnetwork.LossType;
import neuralnetwork.Matrix;
import neuralnetwork.NeuralNetwork;

/**
 *
 * @author tnc
 */
public class DataMining
{
    // TODO:
    // -Train each team w.r.t their last 5 matches. So store that statistic
    // in the team class.
    
    
    // Data Path
    static final String DATA_PATH = System.getProperty("user.home") + File.separator +
            "Desktop" + File.separator + "data" + File.separator;
    
    // League Paths
    static final String ENG_PATH = DATA_PATH + "eng" + File.separator;
    static final String ITA_PATH = DATA_PATH + "ita" + File.separator;
    static final String TUR_PATH = DATA_PATH + "tur" + File.separator;
    static final String SPA_PATH = DATA_PATH + "spa" + File.separator;
    static final String GER_PATH = DATA_PATH + "ger" + File.separator;
    
    League lg = CSV_Reader.read(GER_PATH);
    NeuralNetwork nn;
    Trainer trainer;
    
    public static void main(String[] args)
    {        
        DataMining dt = new DataMining();
        dt.train();
        
        while(true)
            dt.test();
    }
    
    public void train()
    {
        nn = new NeuralNetwork(
            new int[]{9, 10, 42, 3},
            new ActivationType[]{
                ActivationType.TANH,
                ActivationType.TANH,
                ActivationType.SOFTMAX
            },
            LossType.CROSS_ENTROPY,
            0.0007
        );
        
        trainer = new Trainer(nn, lg, 0.1f, true);
        trainer.train(100);      
        trainer.test();
    }
    
    public void test()
    {
        Scanner sc = new Scanner(System.in);
        Team[] teams = lg.getTeams();
        
        for(int i = 0; i < teams.length; i++)
            System.out.println(String.format("%-15s -> %d", teams[i].name, i));

        System.out.print("\nHome Team: ");
        Team homeTeam = teams[sc.nextInt()];
        System.out.print("Away Team: ");
        Team awayTeam = teams[sc.nextInt()];
        System.out.print("B365H: ");
        double B365H = sc.nextDouble();
        System.out.print("B365D: ");
        double B365D = sc.nextDouble();
        System.out.print("B365A: ");
        double B365A = sc.nextDouble();
        
        Matrix inputs;
        inputs = new Matrix(9, 1);
            
        // Home team features
        //inputs.data[0][0] = Trainer.normalize(lg.minGD, lg.maxGD, homeTeam.GD);
        //inputs.data[1][0] = homeTeam.getPercentageOfWin();
        //inputs.data[2][0] = homeTeam.getPercentageOfDraw();
        //inputs.data[3][0] = homeTeam.getPercentageOfLose();
        inputs.data[3][0] = homeTeam.getPercentageOfHomeWin();
        inputs.data[4][0] = homeTeam.getPercentageOfHomeDraw();
        inputs.data[5][0] = homeTeam.getPercentageOfHomeLose();

        // Away team features
        //inputs.data[7][0] = Trainer.normalize(lg.minGD, lg.maxGD, awayTeam.GD);
        //inputs.data[8][0] = awayTeam.getPercentageOfWin();
        //inputs.data[9][0] = awayTeam.getPercentageOfDraw();
        //inputs.data[10][0] = awayTeam.getPercentageOfLose();
        inputs.data[6][0] = awayTeam.getPercentageOfAwayWin();
        inputs.data[7][0] = awayTeam.getPercentageOfAwayDraw();
        inputs.data[8][0] = awayTeam.getPercentageOfAwayLose();

        // Bet365 odds
        inputs.data[0][0] = Trainer.normalize(lg.minB365H, lg.maxB365H, B365H);
        inputs.data[1][0] = Trainer.normalize(lg.minB365D, lg.maxB365D, B365D);
        inputs.data[2][0] = Trainer.normalize(lg.minB365A, lg.maxB365A, B365A);
        
        nn.setInput(inputs);
        nn.feedForward();
        
        System.out.println(homeTeam.name + " - " + awayTeam.name);
        System.out.println(nn.getOutput());
    }
}
