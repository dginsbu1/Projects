import java.io.*;
//import java.lang.reflect.Array;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

//uses Array<String> for node names
//use Array<Edge> for edges
// Edge: getFrom(), getTo, isCertain, isPositive
public class GraphFileScanner {
    private ArrayList<String> nodes = new ArrayList<>();
    private ArrayList<Edge> edges = new ArrayList<>();
    private String NuSMVResultsPath = Controller.NuSMVResults;
    private int solEdgeNum;


    public static void main(String args[]) throws IOException {
        GraphFileScanner genomeSearcher = new GraphFileScanner();
        genomeSearcher.scanFile("C:\\Users\\dgmon\\1standard\\Downloads\\genome\\model.txt");
    }
    public GraphFileScanner(){}
    //scans the given file and extracts the nodes and edges
    public String[] scanFile(String pathName) throws IOException {
        Path path = Paths.get(pathName);
        String fileString = new String(Files.readAllBytes(Paths.get(pathName)));
        String[] fileStringArray = fileString.split("\\s+");
        for(String word: fileStringArray)
            if(word.contains("["))
                nodes.add(word.substring(0,word.indexOf("[")));
        String[] lineArray;
        InputStream is = new FileInputStream(pathName);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        String line = buf.readLine();
        while (line != null) {//either three or four words
            int lineSize = line.length();
            if(line.contains("positive") || line.contains("negative")){
                lineArray = line.split("\\s+");
                String from = lineArray[0];
                String to = lineArray[1];
                boolean active = line.contains("positive");
                boolean certain = !line.contains("optional");
                if(lineArray.length == 3) {//not optional
                    edges.add(new Edge(from, to, active, certain));
                }else if(lineArray.length == 4){
                    edges.add(new Edge(from, to, active, certain));
                }
            }
            line = buf.readLine();
        }
        Collections.sort(nodes);
        Collections.sort(edges);
        //assuming this is an motifScanner. can override
        this.solEdgeNum = edges.size();
        System.out.println("GraphFileScanner Results:\n");
        printStrings(nodes);
        printEdges(edges);
        System.out.println("THE END \n\n");
        return null;
    }
    //scans the results.txt file for a potential solution
    public ArrayList<Edge> scanNuSMVResults(int solEdgeNum) throws IOException {
        this.solEdgeNum = solEdgeNum;
        return scanNuSMVResults();
    }
    //scans the results.txt file for a potential solution assuming solEdgeNum is set
    public ArrayList<Edge> scanNuSMVResults() throws IOException {
        int remainingSol = solEdgeNum;
        ArrayList<Edge> solEdgeList = new ArrayList<>(solEdgeNum);
        InputStream is = new FileInputStream(NuSMVResultsPath);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        String line = buf.readLine();
        while (remainingSol > 0) {
            if(line.contains("no counterexample found")){
                break;
            }
            Edge solEdge = new Edge();
            if (line.contains("solEdge") & line.contains(".o[") &! line.contains("-- specification")) {
                solEdge = createEdgeFromFile(buf, line);
                solEdgeList.add(solEdge);
                remainingSol--;
            }
            line = buf.readLine();
        }
        return solEdgeList;
    }
    //scans the second NuSMV and searches for a counter example.
    //if one exist then the there is no valid solution
    public Edge getBadEdge() throws IOException {
        InputStream is = new FileInputStream(NuSMVResultsPath);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        String line = buf.readLine();
        //used to hold the edges that invalidate the motif in the graph
        //ArrayList<Edge> badEdges = new ArrayList<>();
        while (line != null) {
            //if it has a counterexample then we must
            //differentiate between certain vs optional edges
            if(line.contains("-> State: 1.1 <-")){
                line = buf.readLine();
                while(line != null){
                    if(line.contains("checkOneEdge.randomEdge.position")){
                        //add the Edge to the graph
                        return createEdgeFromFile(buf, buf.readLine());
                    }
                    line = buf.readLine();
                }
            }
            if(line.contains("no counterexample")){
                return null;
            }
            line = buf.readLine();
        }
        //should never reach this point
        throw new IOException("You done messed up A-Aron!");
    }
    //creates an edge from a file by scanning
    //used when finding badEdges in the graph
    private Edge createEdgeFromFile(BufferedReader buf, String line) throws IOException {
        //line = buf.readLine();//set from
        String from = getVar(line);
        line = buf.readLine();//set to
        String to = getVar(line);
        line = buf.readLine();//set positive/negative
        boolean pos = getVar(line).equals("t")? true : false;
        line = buf.readLine();//set certain/uncertain
        boolean certain = getVar(line).equals("t")? true : false;
        line = buf.readLine();//skip past the used criteria
        return new Edge(from, to, pos, certain);

    }
    //getters
    private String getVar(String line) {
        return line.substring(line.indexOf("= ")+1).trim();
    }
    public ArrayList<Edge> getEdges() {
        return edges;
    }
    public ArrayList<String> getNodes() {
        return nodes;
    }

    private void printEdges(ArrayList<Edge> edges) {
        for(Edge e: edges){
            System.out.println(e);
        }
    }
    private void printStrings(ArrayList<String> nodes) {
        for(String s : nodes){
            System.out.println(s);
        }
    }
    //@depreciated
    public boolean counterExists(ArrayList<Edge> solEdges) throws IOException {
        InputStream is = new FileInputStream(NuSMVResultsPath);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        String line = buf.readLine();
        while (line != null) {
            //if it has a counterexample then we must
            //differentiate between certain vs optional edges
            if(line.contains("-> State: 1.1 <-")){
                return true;
            }
            if(line.contains("no counterexample")){
                return false;
            }
            line = buf.readLine();
        }
        //should never reach this point
        throw new IOException("You done messed up A-Aron!");
    }

}


