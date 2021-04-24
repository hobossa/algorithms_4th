import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class BaseballElimination {
    private final int numberOfTeams;
    private final String[] teamNames;
    private final HashMap<String, Integer> teams = new HashMap<>();
    private final HashMap<String, Boolean> eliminateCache = new HashMap<>();
    private final HashMap<String, LinkedList<String>> eliminateSubSet = new HashMap<>();
    private final int[] w;
    private final int[] l;
    private final int[] r;
    private final int[][] g;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        numberOfTeams = in.readInt();
        teamNames = new String[numberOfTeams];
        w = new int[numberOfTeams];
        l = new int[numberOfTeams];
        r = new int[numberOfTeams];
        g = new int[numberOfTeams][numberOfTeams];
        for (int i = 0; i < numberOfTeams; i++) {
            teamNames[i] = in.readString();
            teams.put(teamNames[i], i);
            w[i] = in.readInt();
            l[i] = in.readInt();
            r[i] = in.readInt();
            for (int j = 0; j < numberOfTeams; j++) {
                g[i][j] = in.readInt();
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return numberOfTeams;
    }

    // all teams
    public Iterable<String> teams() {
        //return teams.keySet();
        return Arrays.asList(teamNames);
    }

    // number of wins for given team
    public int wins(String team) {
        if (!teams.containsKey(team)) {
            throw new IllegalArgumentException("not a valid team");
        }
        return w[teams.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!teams.containsKey(team)) {
            throw new IllegalArgumentException("not a valid team");
        }
        return l[teams.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!teams.containsKey(team)) {
            throw new IllegalArgumentException("not a valid team");
        }
        return r[teams.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!teams.containsKey(team1)) {
            throw new IllegalArgumentException("team1 is not a valid team");
        }
        if (!teams.containsKey(team2)) {
            throw new IllegalArgumentException("team2 is not a valid team");
        }
        return g[teams.get(team1)][teams.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (eliminateCache.containsKey(team)) {
            return eliminateCache.get(team);
        }

        if (!teams.containsKey(team)) {
            throw new IllegalArgumentException("not a valid team");
        }
        int teamIndex = teams.get(team);
        boolean bEliminated = isTrivialEliminated(teamIndex) || isNontrivialEliminated(teamIndex);
        eliminateCache.put(team, bEliminated);
        return bEliminated;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!teams.containsKey(team)) {
            throw new IllegalArgumentException("not a valid team");
        }
        if (isEliminated(team)) {
            return eliminateSubSet.get(team);
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(numberOfTeams);
        strBuilder.append("\n");
        for (int i = 0; i < numberOfTeams; i++) {
            strBuilder.append(teamNames[i]);
            strBuilder.append(" ");
            strBuilder.append(w[i]);
            strBuilder.append(" ");
            strBuilder.append(l[i]);
            strBuilder.append(" ");
            strBuilder.append(r[i]);
            for (int j = 0; j < numberOfTeams; j++) {
                strBuilder.append(" ");
                strBuilder.append(g[i][j]);
            }
            strBuilder.append("\n");
        }
        return strBuilder.toString();
    }

    private boolean isTrivialEliminated(int teamIndex) {
        boolean eliminated = false;
        int maxWins = w[teamIndex] + r[teamIndex];
        for (int i = 0; i < numberOfTeams; i++) {
            if (i != teamIndex && maxWins < w[i]) {
                eliminated = true;
                LinkedList<String> temp = new LinkedList<>();
                temp.add(teamNames[i]);
                eliminateSubSet.put(teamNames[teamIndex], temp);
            }
        }
        return eliminated;
    }

    private boolean isNontrivialEliminated(int teamIndex) {
        boolean bEliminated = false;
        // number of edges (numberOfTeams-1)*(numberOfTeams-2)*3/2 + (numberOfTeams-1);
        // number of vertices
        int nV = 2 + (numberOfTeams - 1) * (numberOfTeams - 2) / 2 + (numberOfTeams - 1);
        FlowNetwork flowNetwork = new FlowNetwork(nV);
        int xW = w[teamIndex] + r[teamIndex];
        // add edges between s and game vertices
        int flowFromS = 0;
        for (int i = 0; i < numberOfTeams; i++) {
            if (i == teamIndex) {
                continue;
            }
            int vTi = getTeamVertexIndex(teamIndex, i);
            for (int j = i + 1; j < numberOfTeams; j++) {
                if (j == teamIndex) {
                    continue;
                }
                int vG = getGameVertexIndex(teamIndex, i, j);
                int vTj = getTeamVertexIndex(teamIndex, j);
                flowNetwork.addEdge(new FlowEdge(0, vG, g[i][j]));
                flowFromS += g[i][j];
                flowNetwork.addEdge(new FlowEdge(vG, vTi, Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(new FlowEdge(vG, vTj, Double.POSITIVE_INFINITY));
            }
            flowNetwork.addEdge(new FlowEdge(vTi, flowNetwork.V() - 1, xW - w[i]));

        }
        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, 0, flowNetwork.V() - 1);
        if (fordFulkerson.value() < flowFromS) {
            bEliminated = true;
            LinkedList<String> temp = new LinkedList<>();
            for (int i = 0; i < this.numberOfTeams; i++) {
                if (i != teamIndex && fordFulkerson.inCut(getTeamVertexIndex(teamIndex, i))) {
                    temp.add(teamNames[i]);
                }
            }
            eliminateSubSet.put(teamNames[teamIndex], temp);
        } else {
            bEliminated = false;
        }
        return bEliminated;
    }

    private int getGameVertexIndex(int x, int i, int j) {
//        if (x == i || x == j || i >= j) {
//            throw new IllegalArgumentException();
//        }
        if (i > x) {
            i--;
        }
        if (j > x) {
            j--;
        }
        int index = j - i;
        int temp = numberOfTeams - 2;
        while (i > 0) {
            index += temp;
            temp--;
            i--;
        }
        return index;
    }

    private int getTeamVertexIndex(int x, int i) {
        if (i > x) {
            i--;
        }
        return (numberOfTeams - 1) * (numberOfTeams - 2) / 2 + i + 1;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        //BaseballElimination division = new BaseballElimination("./teams12.txt");
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
