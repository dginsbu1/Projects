
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class Controller {
    private static String localDir = System.getProperty("user.dir");
    private static String resourcePath = localDir + "\\src\\main\\resources";

    private static String geneGraphFileRead = resourcePath+"\\NuSMVGeneGraph.txt";
    private static String geneMotifFileRead = resourcePath+"\\NuSMVGeneMotif.txt";

    public static String NuSMVFileWrite = resourcePath+"\\NuSMVFile.smv";
    public static String NuSMVFileWriteConfirm  = resourcePath+"\\NuSMVFileConfirm.smv";
    public static String NuSMVResults      = resourcePath+"\\genomeResults.txt";
    GraphFileScanner graphFileScanner;
    GraphFileScanner graphScanner;
    GraphFileScanner motifScanner;
    GenomeNuSMVWriter writer;
    NuSMVRunner runner;


    public static void main(String args[]) throws IOException, InterruptedException {
         Controller controller = new Controller();
         if(args.length == 2){//sets the files to process
             controller.setGeneGraphFileRead(args[0]);
             controller.setGeneMotifFileRead(args[1]);
         }
         controller.run();
    }

    //writes, runs, and scans the NuSMV code to search for
    //geneMotifs in a genome
    public void run() throws IOException, InterruptedException {
        //create objects
        graphScanner = new GraphFileScanner();
        motifScanner = new GraphFileScanner();
        //scan initial files
        graphScanner.scanFile(geneGraphFileRead);
        motifScanner.scanFile(geneMotifFileRead);
        writer = instantiateWriter(graphScanner, motifScanner);
        //write NuSMV files
        while(true) {
            writer.writeNuSMV();
            //run NuMSV files
            //sleep(350);
            runner = new NuSMVRunner();
            while(true){
                runner.runNuSMV(NuSMVFileWrite);
                sleep(650);
                break;
            }

            //scan results
            ArrayList<Edge> solEdges = motifScanner.scanNuSMVResults();
            if (solEdges.size() == 0) {
                System.out.println("NO MORE SOLUTIONS");
                break;
            }
            //rewrites the NuSMV, to look for potential extraneous edges
            //sleep(500);
            writer.setWriter(solEdges);
            writer.WriteNuSMVBadEdges(0);
            //rerun files
            while(true) {
                runner.runNuSMV(NuSMVFileWriteConfirm);
                sleep(650);
                break;

            }
            sleep(350);
            //rescan files
            //it is a valid Solution
            Edge badEdge = motifScanner.getBadEdge();
            if (badEdge == null) {//no badEdge
                System.out.println("This is one valid Solution");
                printSolution(solEdges);
                //check for optional bad edges
                ArrayList<Edge> badEdges = getAllBadEdges();
                if(badEdges.size() == 0)//no bad edges
                    System.out.println("There are no bad optional edges for this graph\n");
                else {
                    System.out.println("assuming the following optional edges are turned off:");
                    printSolution(badEdges);
                    System.out.println();
                }
            }//not valid solution
            else {
                System.out.println("Correct Motif BUT extra edge(s):");
                printSolution(solEdges);
                System.out.println("The bad edge is:\n"+
                        badEdge.toString()+"\n");
            }
            writer.updateLTLSPEC(solEdges);
        }
        //clearFiles();
    }
    //sets up the variables of the NuSMV writer
    private GenomeNuSMVWriter instantiateWriter(GraphFileScanner graphScanner, GraphFileScanner motifScanner) {
        ArrayList<Edge> edges = graphScanner.getEdges();
        GenomeNuSMVWriter writer = new GenomeNuSMVWriter(edges.size(), edges, graphScanner.getNodes());
        writer.setMotifNodeList(motifScanner.getNodes());
        writer.setMotifEdgeList(motifScanner.getEdges());
        return writer;
    }
    //rewrites the NuSMV to get all the optional bad edges
    public ArrayList<Edge> getAllBadEdges() throws IOException, InterruptedException {
        ArrayList<Edge> badEdges = new ArrayList<>();
        int attempt = 1;
        while(true) {
            //update LTLSPEC
            //don't add any badEdges bec there are none
            if(attempt == 1){writer.WriteNuSMVBadEdges(attempt , null);}
            else{writer.WriteNuSMVBadEdges(attempt , badEdges.get(attempt-2));}
            sleep(500);
            runner.runNuSMV(NuSMVFileWriteConfirm);
            sleep(400);
            //rescan with motifScanner.getBadEdge
            Edge badEdge = motifScanner.getBadEdge();
            //check if it is valid
            //if yes add to list
            if (badEdge == null) return badEdges;
            else badEdges.add(badEdge);//if not then stop and return list
            attempt++;
        }
    }
    //private void clearFiles() {}
    //prints an array of edges in a clean form
    private void printSolution(ArrayList<Edge> solEdges) {
        //print out the sol Edges
        for (Edge e : solEdges) {
            System.out.println(e.toString());
        }
    }
    //setters
    public void setGeneGraphFileRead(String NuSMVGeneGraph){
        this.geneGraphFileRead = NuSMVGeneGraph;
    }
    public void setGeneMotifFileRead(String NuSMVGeneGraph){
        this.geneGraphFileRead = NuSMVGeneGraph;
    }
}
