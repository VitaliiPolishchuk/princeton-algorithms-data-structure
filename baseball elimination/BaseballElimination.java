import edu.princeton.cs.algorithms.Bag;
import edu.princeton.cs.algorithms.FlowEdge;
import edu.princeton.cs.algorithms.FlowNetwork;
import edu.princeton.cs.algorithms.FordFulkerson;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class BaseballElimination {
    int[] w,l,r; //win, lose, remain games
    int[][] g; //remain games left to play against j team
    ArrayList<String> teams;


    public BaseballElimination(String filename)                    // create a baseball division from given filename in format specified below
    {
        Charset charset = Charset.forName("US-ASCII");

        try (BufferedReader reader = (BufferedReader) Files.newBufferedReader(Paths.get(filename), charset)) {
            String strNumTeam = reader.readLine();
            int numTeam = Integer.parseInt(strNumTeam);

            w = new int[numTeam];
            l = new int[numTeam];
            r = new int[numTeam];
            g = new int[numTeam][];
            for(int i = 0; i < numTeam; i++){
                g[i] = new int[numTeam];
            }
            teams = new ArrayList<String>();

            int counterTeam = 0;

            String teamDetailsLine = null;
            while ((teamDetailsLine = reader.readLine()) != null) {
                String[] arrTeamDetailsLine = teamDetailsLine.split("\\s+");

                String teamName = arrTeamDetailsLine[0];
                int teamWin = Integer.parseInt(arrTeamDetailsLine[1]);
                int teamLose = Integer.parseInt(arrTeamDetailsLine[2]);
                int teamLeft = Integer.parseInt(arrTeamDetailsLine[3]);

                teams.add(teamName);
                w[counterTeam] = teamWin;
                l[counterTeam] = teamLose;
                r[counterTeam] = teamLeft;

                for(int i = 0; i < numTeam; i++){
                    int gamesBetweenTeams = Integer.parseInt(arrTeamDetailsLine[i + 4]);

                    g[counterTeam][i] = gamesBetweenTeams;
                }
                counterTeam++;
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        };
    }
    public              int numberOfTeams()                        // number of teams
    {
        return teams.size();
    }
    public Iterable<String> teams()                                // all teams
    {
        return teams;
    }

    public              int wins(String team)                      // number of wins for given team
    {
        int indexTeam = teams.indexOf((team));

        if(indexTeam == -1){
            throw new IllegalArgumentException("Team does not exist");
        }

        return w[indexTeam];
    }
    public              int losses(String team)                    // number of losses for given team
    {
        int indexTeam = teams.indexOf((team));

        if(indexTeam == -1){
            throw new IllegalArgumentException("Team does not exist");
        }

        return l[indexTeam];
    }

    public              int remaining(String team)                 // number of remaining games for given team
    {
        int indexTeam = teams.indexOf((team));

        if(indexTeam == -1){
            throw new IllegalArgumentException("Team does not exist");
        }

        return r[indexTeam];
    }
    public              int against(String team1, String team2)    // number of remaining games between team1 and team2
    {
        int indexTeam1 = teams.indexOf((team1));
        int indexTeam2 = teams.indexOf((team2));

        if(indexTeam1 == -1 || indexTeam2 == -1){
            throw new IllegalArgumentException("Team does not exist");
        }

        return g[indexTeam1][indexTeam2];
    }

    public          boolean isEliminated(String team)              // is given team eliminated?
    {
        int indexTeam = teams.indexOf((team));

        if(indexTeam == -1){
            throw new IllegalArgumentException("Team does not exist");
        }

        for(int i = 0; i < teams.size(); i++){
            if(i == indexTeam) continue;
            if(w[indexTeam] + r[indexTeam] < w[i]){
                return true;  // return true if team eliminated by primary check
            }
        }

        FlowNetwork flowNetwork = createFlowNetworkWithoutTeam(indexTeam);

        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, vertexS(), vertexT());

        int regress = 0;
        for(int i = 0; i < teams.size(); i++){
            if(i == indexTeam) {
                regress = 1;
                continue;
            }
            if(fordFulkerson.inCut(i - regress)){
                return true;
            }
        }

        return false;

    }
    public Iterable<String> certificateOfElimination(String team)  // subset R of teams that eliminates given team; null if not eliminated
    {
        Bag<String> bagEliminators = new Bag<String>();
        int indexTeam = teams.indexOf((team));

        if(indexTeam == -1){
            throw new IllegalArgumentException("Team does not exist");
        }

        for(int i = 0; i < teams.size(); i++){
            if(w[indexTeam] + r[indexTeam] <= w[i]){
                bagEliminators.add(teams.get(i));
                return bagEliminators;
            }
        }

        FlowNetwork flowNetwork = createFlowNetworkWithoutTeam(indexTeam);

        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, vertexS(), vertexT());

        int regress = 0;
        for(int i = 0; i < teams.size(); i++){
            if(i == indexTeam) {
                regress = 1;
                continue;
            }
            if(fordFulkerson.inCut(i - regress)) {
                bagEliminators.add(teams.get(i - regress));
            }
        }

        if(bagEliminators.size() == 0){
            return null;
        }
        else {
            return bagEliminators;
        }
    }

    private FlowNetwork createFlowNetworkWithoutTeam(int indexTeam){

        FlowNetwork flowNetwork = new FlowNetwork(numberOfVertexies());

        int counterVertexGamesBetweenTeams = vertexT() + 1;

        for(int i = 0; i < teams.size(); i++){ //go by top diagonal matrix
            for(int j = i + 1; j < teams.size(); j++){
                if(i == indexTeam || j ==indexTeam){
                    continue;
                }

                FlowEdge flowEdgeFromS = new FlowEdge(vertexS(), counterVertexGamesBetweenTeams, g[i][j]);
                flowNetwork.addEdge(flowEdgeFromS);

                FlowEdge flowEdgeToI = new FlowEdge(counterVertexGamesBetweenTeams, i, Integer.MAX_VALUE);
                flowNetwork.addEdge(flowEdgeToI);

                FlowEdge flowEdgeToJ = new FlowEdge(counterVertexGamesBetweenTeams, j, Integer.MAX_VALUE);
                flowNetwork.addEdge(flowEdgeToJ);

                counterVertexGamesBetweenTeams++;
            }
        }
        int regress = 0;
        for(int i = 0; i < teams.size(); i++){
            if(i == indexTeam) {
                regress = 1;
                continue;
            }

            if(w[indexTeam] + r[indexTeam] - w[i] >= 0) {
                FlowEdge flowEdgeFromIToT = new FlowEdge(i - regress, vertexT(), w[indexTeam] + r[indexTeam] - w[i]);
                flowNetwork.addEdge(flowEdgeFromIToT);
            }
        }

        return flowNetwork;
    }

    private int numberOfVertexies(){
        int num = 0;
        for(int i = 0; i < teams.size() - 1; i++){
            num += i;
        }
        num += (teams.size() - 1) + 2;
        return num;
    }

    private int vertexS() { return teams.size() - 1; }

    private int vertexT(){  return teams.size();  }

    public static void main(String[] args){

    }

}
