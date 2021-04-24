import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class BaseballElimination {
    private final int numberOfTeams;
    private final String[] teamNames;
    private final HashMap<String, Integer> teams = new HashMap<>();
    private final HashMap<String, Boolean> eliminateCache = new HashMap<>();
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
        return teams.keySet();
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
        return null;
    }

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(numberOfTeams);
        strBuilder.append(System.getProperty("line.separator"));
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
            strBuilder.append(System.getProperty("line.separator"));
        }
        return strBuilder.toString();
    }

    private boolean isTrivialEliminated(int teamIndex) {
        boolean eliminated = false;
        int canWin = w[teamIndex] + r[teamIndex];
        for (int i = 0; i < numberOfTeams; i++) {
            if (i != teamIndex && canWin < w[i]) {
                eliminated = true;
            }
        }
        return eliminated;
    }

    private boolean isNontrivialEliminated(int teamIndex) {
        return false;
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

        int x = 4;
        BaseballElimination d = new BaseballElimination("./teams5.txt");
        StdOut.println(d);
        int nT = d.numberOfTeams();
        // number of edges (nT-1)*(nT-2)*3/2 + (nT-1);
        // number of vertices
        int nV = 2 + (nT - 1) * (nT - 2) / 2 + (nT - 1);
        FlowNetwork flowNetwork = new FlowNetwork(nV);
        int xW = d.w[x] + d.r[x];
        // add edges between s and game vertices
        int flowFromS = 0;
        for (int i = 0; i < nT; i++) {
            if (i == x) {
                continue;
            }
            int vTi = d.getTeamVertexIndex(x, i);
            for (int j = i + 1; j < nT; j++) {
                if (j == x) {
                    continue;
                }
                int vG = d.getGameVertexIndex(x, i, j);
                int vTj = d.getTeamVertexIndex(x, j);
                flowNetwork.addEdge(new FlowEdge(0, vG, d.g[i][j]));
                flowFromS += d.g[i][j];
                flowNetwork.addEdge(new FlowEdge(vG, vTi, Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(new FlowEdge(vG, vTj, Double.POSITIVE_INFINITY));
            }
            flowNetwork.addEdge(new FlowEdge(vTi,  flowNetwork.V() - 1, xW - d.w[i]));

        }
        StdOut.println(flowNetwork);
        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, 0, flowNetwork.V()-1);
        StdOut.println(fordFulkerson.value());
        if (fordFulkerson.value() < flowFromS) {
            StdOut.print("eliminated. by the sub set (");
            for (int i = 0; i < d.numberOfTeams(); i++) {
                if (i != x && fordFulkerson.inCut(d.getTeamVertexIndex(x, i))) {
                    StdOut.print(d.teamNames[i]);
                    StdOut.print(' ');
                }
            }
            StdOut.println(")");
        } else {
            StdOut.println("not eliminated");
        }

//        BaseballElimination division = new BaseballElimination(args[0]);
//        for (String team : division.teams()) {
//            if (division.isEliminated(team)) {
//                StdOut.print(team + " is eliminated by the subset R = { ");
//                for (String t : division.certificateOfElimination(team)) {
//                    StdOut.print(t + " ");
//                }
//                StdOut.println("}");
//            }
//            else {
//                StdOut.println(team + " is not eliminated");
//            }
//        }
    }
}
