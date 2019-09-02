import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class GenomeNuSMVWriter {
    private int numEdges;
    private int numEdgesMinus;
    private ArrayList<Edge> edgeList;
    private ArrayList<String> nodeList;

    private int numMotifs;
    private int numMotifsMinus;
    private ArrayList<Edge> motifEdgeList;
    private ArrayList<String> motifNodeList;

    private String allNames;
    private ArrayList<Edge> solEdgeList;

    //private String NuSMVFilePathWrite = "C:\\Users\\dgmon\\GIT\\GenomeMotif\\src\\main\\resources\\NuSMVGeneMotif.txt";
    private String NuSMVFilePathWrite = Controller.NuSMVFileWrite;
    private String NuSMVFilePathConfirm= Controller.NuSMVFileWriteConfirm;
    private String moduleOne;
    private String moduleOneTemp;
    private String LTLSPEC;
    private String LTLLogic;
    private String LTLSPECTemp;
    private boolean modulesSet;
    private int attempt;
    private Edge badEdge;

    //private HashMap<String, Edge> motifEdgeMap = new HashMap<>();
    private String moduleTwo;
    private String moduleThree;
    private String moduleFour;
    private String moduleFive;
    private String moduleSix;
    private String staticModules;


    public static void main(String args[]) throws IOException {
        GraphFileScanner graphScanner = new GraphFileScanner();
        graphScanner.scanFile("C:\\Users\\dgmon\\1standard\\Downloads\\genome\\nodes.txt");
        GraphFileScanner motifScanner = new GraphFileScanner();
        motifScanner.scanFile("C:\\Users\\dgmon\\1standard\\Downloads\\genome\\nodesMotif.txt");
        GenomeNuSMVWriter writer = new GenomeNuSMVWriter(graphScanner.getEdges().size(), graphScanner.getEdges(), graphScanner.getNodes());
        writer.setMotifNodeList(motifScanner.getNodes());
        writer.setMotifEdgeList(motifScanner.getEdges());
        writer.writeNuSMV();
        writer.WriteNuSMVBadEdges(0);
    }
    //creates a writer object for writing NuSMV code
    //used for gene motif searching
    public GenomeNuSMVWriter(int numEdges, ArrayList<Edge> edgeList, ArrayList<String> nodeList) {
        this.numEdges = numEdges;
        this.numEdgesMinus = numEdges - 1;
        this.edgeList = edgeList;
        this.nodeList = nodeList;
        setNames(nodeList);
        modulesSet = false;
    }
    //sets and writes all the Modules to find the Motif
    public String writeNuSMV() throws IOException {
        if(modulesSet == false) {
            setAllModules();}
        modulesSet = true;
        return writeAllModules(NuSMVFilePathWrite);
    }
    //gives the writer the solEdges for the confirmation stage
    public void setWriter(ArrayList<Edge> solEdgeList){
        this.solEdgeList = solEdgeList;
    }
    // 0 = lok for bad required edges, 1 = look for optional
    //2+ = look for optional and add on previous answer to LTL
    public String WriteNuSMVBadEdges(int attempt) throws IOException {
        this.attempt = attempt;
        setModulesBadEdges();
        return writeAllModules(NuSMVFilePathConfirm);
    }
    // 0 = lok for bad required edges, 1 = look for optional
    //2+ = look for optional and add on previous answer to LTL
    public String WriteNuSMVBadEdges(int attempt, Edge badEdge) throws IOException {
        this.attempt = attempt;
        this.badEdge = badEdge;
        setModulesBadEdges();
        return writeAllModules(NuSMVFilePathConfirm);
    }
    //reSets the modules for the second run of the NuSMV
    private void setModulesBadEdges() {
        setModuleOne("second");
        setLTLSPECBadEdges();
    }
    private void setLTLSPECBadEdges() {
        if(attempt == 0){
            LTLSPECTemp = LTLSPEC;
            LTLSPEC = "(checkOneEdge.valid = FALSE)";
        }
        if(attempt == 1){
            LTLSPEC = "(checkOneEdge.valid = FALSE)";
        }
        if(attempt >= 2){
            String cert = badEdge.isPos()? "t":"f";
            LTLSPEC += " &! (c.o[0] = "+badEdge.getFrom()+" & c.o[1] = "+badEdge.getTo()+" & c.o[2] = "+cert+")";
        }
    }
    private void setAllModules() {
        //String represents which iteration it is (first or second)
        setModuleOne("first");
        setLTLSPEC();
        setModuleTwo();
        setModuleThree();
        setModuleFour();
        setModuleFive();
        setModuleSix();
        setStaticModules();
    }
    public String writeAllModules(String filePath) throws IOException {
        FileWriter fw = new FileWriter(filePath);
        fw.write(moduleOne);
        fw.write("LTLSPEC G!("+LTLSPEC+");");
        fw.write(moduleTwo);
        fw.write(moduleThree);
        fw.write(moduleFour);
        fw.write(moduleFive);
        fw.write(moduleSix);
        fw.write(staticModules);
        fw.close();
        return NuSMVFilePathWrite;
    }
    //sets the Main Module
    private void setModuleOne(String iteration) {
        String mainModule, define, var, assign;
        define = "MODULE main\n";
        StringBuilder varString = new StringBuilder();
        varString.append("\tVAR \n" +
                "\t\tnode : Node(t," + numEdgesMinus + "); --holds all nodes\n\n");
        for (int i = 0; i < numEdges; i++) {
            Edge edge = edgeList.get(i);
            String from = edge.getFrom();
            String to = edge.getTo();
            String positive = edge.isPosString();
            String certain = edge.isCertainString();
            String edgeName = "edge" + i;
            //create and add the edges.
            varString.append("\t\t" + edgeName + " : Edge(" + from + "," + to + "," + positive + "," + certain + ");\n");
            varString.append("\t\taddEdge" + i + " : addEdge(node," + edgeName + "," + i + ");\n");
        }
        varString.append("\n\t\tnodeMotif : Node(f," + numMotifsMinus + ");--add size\n\n");
        varString.append("\n\t\tc : c(checkOneEdge.randomEdge.o);");
        if(iteration.equals("first") ) {//to find potential solution
            varString.append("\n\t\tcheckOneEdge : checkOneEdge(node, nodeMotif);\n");
            for (int i = 0; i < numMotifs; i++) {
                String solEdgeName = "solEdge" + i;
                if(motifEdgeList.get(i).isCertain()) {//certain edge
                    varString.append("\t\t" + solEdgeName + " : getRandomEdge(node," + numEdgesMinus + "); \n");
                } else{//optional edge
                    varString.append("\t\t" + solEdgeName + " : maybeGetRandomEdge(node," + numEdgesMinus + "); \n");
                }
                varString.append("\t\taddArray" + i + ": addArray(nodeMotif, " + solEdgeName + ".o," + i + ");\n");
            }
        }
        if(iteration.equals("second")){//to verify potential solution
            //first time validating that there are no extra certain edges
            if(attempt == 0){
                    varString.append("\n\t\tcheckOneEdge : checkOneValidEdge(node, nodeMotif);\n");}
            if(attempt > 0){//validate all edges
                varString.append("\n\t\tcheckOneEdge : checkOneEdge(node, nodeMotif);\n");}
            //save old ModuleOne
            //moduleOneTemp = moduleOne;
            for (int i = 0; i < numMotifs; i++) {
                String solEdgeName = "solEdge" + i;
                Edge solEdge = solEdgeList.get(i);
                String edgeString = solEdge.getFrom()+","+solEdge.getTo()+","+
                        solEdge.isPosString()+","+solEdge.isCertainString();
                varString.append("\t\t" + solEdgeName + " : Edge("+edgeString+");\n");
                varString.append("\t\taddSolEdge" + i + ": addEdge(nodeMotif, " + solEdgeName + "," + i + ");\n");
            }
        }
        var = varString.toString();
        mainModule = define + var;
        this.moduleOne = mainModule;
        if(iteration.equals("first")){this.moduleOneTemp = this.moduleOne;}
    }
    //Sets the LTLSPEC at the end of the main module
    public void setLTLSPEC() {
        //create From and To map
        StringBuilder LTLBuilder = new StringBuilder();
        HashMap<String, ArrayList<Edge>> motifFromMap = new HashMap<>();
        HashMap<String, ArrayList<Edge>> motifToMap = new HashMap<>();
        //used for mapping edges to NuSMV names
        HashMap<Edge, String> edgeNameMap = new HashMap<>();
        for(int i = 0; i < motifEdgeList.size(); i++){
            Edge e = motifEdgeList.get(i);
            //add edge to both FromMap and ToMap
            motifFromMap.computeIfAbsent(e.getFrom(), k -> new ArrayList<>()).add(e);
            motifToMap.computeIfAbsent(e.getTo(), k -> new ArrayList<>()).add(e);
            edgeNameMap.put(e, "solEdge"+i);
        }
        Set<String> fromKeys = motifFromMap.keySet();
        Set<String> toKeys = motifToMap.keySet();
        Set<String> allKeys = new HashSet<>();
        allKeys.addAll(fromKeys);
        allKeys.addAll(toKeys);
        for (String str : allKeys) {
            ArrayList<Edge> fromEdges = motifFromMap.get(str);
            ArrayList<Edge> toEdges = motifToMap.get(str);
            int fromSize = fromEdges == null ? 0 : fromEdges.size();
            int toSize = toEdges == null ? 0 : toEdges.size();
            int totSize = fromSize + toSize;
            Edge previousEdge, currentEdge;
            //if only 1 edge then don't need any comparisons
            if (totSize < 2) {
                continue;
            }
            if (fromSize > 0) {
                previousEdge = fromEdges.get(0);
                if (fromSize > 1) {
                    for (int i = 1; i < fromSize; i++) {
                        previousEdge = appendLTL(0, 0, previousEdge, fromEdges.get(i), LTLBuilder, edgeNameMap); } }
                if (toSize == 0) {continue;}
                if (toSize >= 1) {
                    previousEdge = appendLTL(0, 1, previousEdge, toEdges.get(0), LTLBuilder, edgeNameMap);}
                if (toSize > 1) {
                    for (int i = 1; i < toSize; i++) {
                        previousEdge = appendLTL(1, 1, previousEdge, toEdges.get(i), LTLBuilder, edgeNameMap); } }
            }
            //toSize must be >= 2
            if (fromSize == 0) {
                previousEdge = toEdges.get(0);
                for (int i = 1; i < toSize; i++) {
                    previousEdge = appendLTL(1, 1, previousEdge, toEdges.get(i), LTLBuilder, edgeNameMap); }
            }
        }
        //add LTLSPEC for activate and certainty
        LTLBuilder.append(buildActCer(edgeNameMap));
        LTLBuilder.append("TRUE");
        LTLSPEC = LTLBuilder.toString();


    }
    //sets up the end of the LTL that verifies the edges
    //are positive or negative ex. (solEdge.o[2] = 2)
    private String buildActCer(HashMap<Edge,String> edgeNameMap) {
        Set<Edge> edgeKeys = edgeNameMap.keySet();
        StringBuilder builder = new StringBuilder();
        for(Edge e : edgeKeys){
            String name = edgeNameMap.get(e);
            String act = e.isPos()? "t" : "f";
            boolean optional = !e.isCertain();
            builder.append("("+name+".o[2] = "+act);
            //if it is optional then it could be null
            builder.append(optional ? " | "+name+".o[0] = mNull":"");
            builder.append(") & ");
        }
        return builder.toString();

    }
    //augments the LTL to exclude the current answer so it can find more solutions
    private Edge appendLTL(int l, int r, Edge previousEdge, Edge currentEdge, StringBuilder LTLBuilder, HashMap<Edge, String> edgeNameMap) {
        String leftEdge = edgeNameMap.get(previousEdge);
        String rightEdge = edgeNameMap.get(currentEdge);
        LTLBuilder.append("("+leftEdge+".o["+l+"] = "+rightEdge+".o["+r+"]");
        //must account for optional motifs that show up as mNull
        if(!previousEdge.isCertain()){
            LTLBuilder.append(" | "+leftEdge+".o[0] = mNull");
        }
        if(!currentEdge.isCertain()){
            LTLBuilder.append(" | "+rightEdge+".o[0] = mNull");
        }
        LTLBuilder.append(") & ");

        return currentEdge;
    }
    //sets the "Node" module
    private void setModuleTwo() {
        String moduleTwo = "\n--------------------------------------\n" +
                "--Module 2\n" +
                "MODULE Node(nme, numEdgs)\n" +
                "\tVAR \n" +
                "\t\tname : {t,f};\n" +
                //"\t\tname : {" + allNames + "};\n" +
                "\t\tnumEdges : 0..numEdgs;\n" +
                "\t\tedges : array 0..numEdgs of array 0..4 of {" + allNames + "};\n" +
                "\tASSIGN \n" +
                "\t\tnumEdges := numEdgs;\n" +
                "\t\tname := nme;\n" +
                "------------------------------------------------\n" +
                "MODULE c(p)\n" +
                "\tVAR\n" +
                "\t\to : array 0..4 of {AMR, BOB, CAT, DOV, ELI, GOG, Xa, Yaa, Zaaa, t, f, eNull, mNull};\n" +
                "\tASSIGN\n" +
                "\t\to[0] := p[0];\n" +
                "\t\to[1] := p[1];\n" +
                "\t\to[2] := p[2];\n" +
                "\t\to[3] := p[3];\n" +
                "\t\to[4] := p[4];";
        this.moduleTwo = moduleTwo;
    }
    //sets the "Edge" module
    private void setModuleThree() {
        String moduleThree = "--------------------------------------\n" +
                "--MODULE 3" +
                "--Edge holds from, to, active, certain, used (in motif)\n" +
                "--Module 3\n" +
                "MODULE Edge(frm, too, acti, cert)\n" +
                "\tVAR\n" +
                "\t\tfrom : {" + allNames + "};\n" +
                "\t\tto   : {" + allNames + "};\n" +
                "\t\tact  : {t,f};\n" +
                "\t\tcer  : {t,f}; \t\n" +
                "\t\tused : {t,f};\n" +
                "\tASSIGN  \n" +
                "\t\tfrom:= frm;\n" +
                "\t\tto  := too;\n" +
                "\t\tact := case \n" +
                "\t\t\t\tacti = FALSE: f;\n" +
                "\t\t\t\tTRUE: t;\n" +
                "\t\t\t\tesac;\n" +
                "\t\tcer := case \n" +
                "\t\t\t\tcert = FALSE: f;\n" +
                "\t\t\t\tTRUE: t;\n" +
                "\t\t\t\tesac;\n" +
                "\t\tused := f;\n";
        this.moduleThree = moduleThree;
    }
    //sets the "getRandomEdge" module
    private void setModuleFour() {
        String moduleFour = "--------------------------------------\n" +
                "--Module 4\n" +
                "MODULE getRandomEdge(node, numEdges)\n" +
                "\tVAR\n" +
                "\t\tposition : 0..numEdges;\n" +
                "\t\t--o is for output\n" +
                "\t\to : array 0..4 of {" + allNames + "};\n" +
                "\tASSIGN\n" +
                "\t\to[0] := node.edges[position][0];\n" +
                "\t\to[1] := node.edges[position][1];\n" +
                "\t\to[2] := node.edges[position][2];\n" +
                "\t\to[3] := node.edges[position][3];\n" +
                "\t\to[4] := node.edges[position][4];\n"+
                "--------------------------------------\n" +
                "MODULE maybeGetRandomEdge(node,numEdges)--**\n" +
                "\tVAR\n" +
                "\t\tyes : {0, 1};\n" +
                "\t\tposition : 0..numEdges;\n" +
                "\t\t--o is for output\n" +
                "\t\to : array 0..4 of {"+allNames+"};--**\n" +
                "\tASSIGN\n" +
                "\t\to[0] := case \n" +
                "\t\t\t\t\tyes = 1 : node.edges[position][0];\n" +
                "\t\t\t\t\tTRUE : mNull; --motifNull\n" +
                "\t\t\t\t\tesac;\n" +
                "\t\to[1] := case \n" +
                "\t\t\t\t\tyes = 1  : node.edges[position][1];\n" +
                "\t\t\t\t\tTRUE : mNull; \n" +
                "\t\t\t\t\tesac;\n" +
                "\t\to[2] := case \n" +
                "\t\t\t\t\tyes = 1  : node.edges[position][2];\n" +
                "\t\t\t\t\tTRUE : mNull; \n" +
                "\t\t\t\t\tesac;\n" +
                "\t\to[3] := case \n" +
                "\t\t\t\t\tyes = 1  : node.edges[position][3];\n" +
                "\t\t\t\t\tTRUE : mNull; \n" +
                "\t\t\t\t\tesac;\n" +
                "\t\to[4] := case \n" +
                "\t\t\t\t\tyes=1  : node.edges[position][4];\n" +
                "\t\t\t\t\tTRUE : mNull; \n" +
                "\t\t\t\t\tesac;\n";
        this.moduleFour = moduleFour;
    }

    //sets the "checkOneEdge" module
    private void setModuleFive() {
        this.moduleFive = "--------------------------------------------\n"+
                "MODULE checkOneEdge(node, nodeMotif)\n" +
                "\tVAR \n" +
                "\t\trandomEdge : getRandomEdge(node,"+numEdgesMinus+"); \n" +
                "\t\tvalid : boolean;\n" +
                "\t\tvalidEdge : validEdge(randomEdge, nodeMotif);\n" +
                "\tASSIGN \n" +
                "\t\tvalid := case\n" +
                "\t\t\t\t\tvalidEdge.valid = FALSE : FALSE;\n" +
                "\t\t\t\t\tTRUE : TRUE; \n" +
                "\t\t\t\t\tesac;\n"+
                "--------------------------------------------\n" +
                "MODULE checkOneValidEdge(node, nodeMotif)\n" +
                "\tVAR \n" +
                "\t\trandomEdge : getRandomEdge(node,"+numEdgesMinus+"); \n" +
                "\t\tvalid : boolean;\n" +
                "\t\tvalidEdge : validCertainEdge(randomEdge, nodeMotif);\n" +
                "\tASSIGN \n" +
                "\t\tvalid := case\n" +
                "\t\t\t\t\tvalidEdge.valid = FALSE : FALSE;\n" +
                "\t\t\t\t\tTRUE : TRUE; \n" +
                "\t\t\t\t\tesac;";
    }
    //sets the "validEdge" module
    private void setModuleSix() {
        StringBuilder moduleSix = new StringBuilder();
        moduleSix.append("---------------------------------------------\n" +
                "--Module 6\n" +
                "MODULE validEdge(randomEdge, nodeMotif)\n" +
                "\tVAR \n" +
                "\t\tvalid : boolean;\n" +
                "\t\tmotifEdge1 : getRandomEdge(nodeMotif, " + numMotifsMinus + ");\n" +
                "\t\tmotifEdge2 : getRandomEdge(nodeMotif, " + numMotifsMinus + ");\n" +
                "\tASSIGN \n" +
                "\t\tvalid := case\n" +
                "\t\t\t--check the 'to' and 'from' of the randomEdge\n" +
                "\t\t\t((randomEdge.o[0] = motifEdge1.o[0]  |\n" +
                "\t\t\t  randomEdge.o[0] = motifEdge1.o[1]) &\n" +
                "\t\t\t (randomEdge.o[1] = motifEdge2.o[0]  |\n" +
                "\t\t\t  randomEdge.o[1] = motifEdge2.o[1]))&!(\n");
        for (int i = 0; i < numMotifs; i++) {
            moduleSix.append(
                    "\t\t\t(randomEdge.o[0] = nodeMotif.edges[" + i + "][0] &\n" +
                    "\t\t\t randomEdge.o[1] = nodeMotif.edges[" + i + "][1])|\n");
        }
        moduleSix.append("\t\t\t  FALSE) : FALSE;\n" +
                "\t\t\t TRUE : TRUE;\n" +
                "\t\t\t esac;\n");
        this.moduleSix = moduleSix.toString();
        setModuleSixPartTwo();
    }

    private void setModuleSixPartTwo() {
        StringBuilder moduleSix = new StringBuilder();
        moduleSix.append("---------------------------------------------\n" +
                "--Module 6.5\n" +
                "MODULE validCertainEdge(randomEdge, nodeMotif)\n" +
                "\tVAR \n" +
                "\t\tvalid : boolean;\n" +
                "\t\tmotifEdge1 : getRandomEdge(nodeMotif, " + numMotifsMinus + ");\n" +
                "\t\tmotifEdge2 : getRandomEdge(nodeMotif, " + numMotifsMinus + ");\n" +
                "\tASSIGN \n" +
                "\t\tvalid := case\n" +
                "\t\t\t--check the 'to' and 'from' of the randomEdge\n" +
                "\t\t\t!(randomEdge.o[3] = f) & --if uncertain then fine\n"+
                "\t\t\t((randomEdge.o[0] = motifEdge1.o[0]  |\n" +
                "\t\t\t  randomEdge.o[0] = motifEdge1.o[1]) &\n" +
                "\t\t\t (randomEdge.o[1] = motifEdge2.o[0]  |\n" +
                "\t\t\t  randomEdge.o[1] = motifEdge2.o[1]))&!(\n");
        for (int i = 0; i < numMotifs; i++) {
            moduleSix.append(
                    "\t\t\t(randomEdge.o[0] = nodeMotif.edges[" + i + "][0] &\n" +
                            "\t\t\t randomEdge.o[1] = nodeMotif.edges[" + i + "][1])|\n");
        }
        moduleSix.append("\t\t\t  FALSE) : FALSE;\n" +
                "\t\t\t TRUE : TRUE;\n" +
                "\t\t\t esac;\n");
        this.moduleSix += moduleSix.toString();
    }
    //set the "addEdge", and "addArray" module
    private void setStaticModules() {
        staticModules = "--------------------------------------------------------\n" +
                "--Modules 7,8\n" +
                "--STATIC\n" +
                "MODULE addEdge(n, e, position)\n" +
                "\tASSIGN\n" +
                "\t\tn.edges[position][0] := e.from;\n" +
                "\t\tn.edges[position][1] := e.to;\n" +
                "\t\tn.edges[position][2] := e.act;\n" +
                "\t\tn.edges[position][3] := e.cer;\n" +
                "\t\tn.edges[position][4] := e.used;\n" +
                "-------------------------------------\n" +
                "--STATIC\n" +
                "MODULE addArray(n, e, position)\n" +
                "\tASSIGN\n" +
                "\t\tn.edges[position][0] := e[0];\n" +
                "\t\tn.edges[position][1] := e[1];\n" +
                "\t\tn.edges[position][2] := e[2];\n" +
                "\t\tn.edges[position][3] := e[3];\n" +
                "\t\tn.edges[position][4] := e[4];\n" +
                "-------------------------------------\n";
    }

    //sets the String which consists of all the node names
    private void setNames(ArrayList<String> nameList) {
        StringBuilder names = new StringBuilder();
        for (int i = 0; i < nameList.size(); i++) {
            names.append(nameList.get(i) + ", ");
        }
        names.append("t, f, eNull, mNull");
        this.allNames = names.toString();
    }
    public void updateLTLSPEC(ArrayList<Edge> solEdges) {
        moduleOne = moduleOneTemp;
        StringBuilder ltlBuilder = new StringBuilder();
        ltlBuilder.append(LTLSPECTemp);
        ltlBuilder.append(" &! (");
        for(int i = 0; i < solEdges.size(); i++){
            Edge edge = solEdges.get(i);
            String edgeName = "solEdge"+i;
            ltlBuilder.append("("+edgeName+".o[0] = "+edge.getFrom()+" & "+edgeName+".o[1] = "+edge.getTo()+") & ");
        }
        //finish the ltlBuilder
        ltlBuilder.append(" TRUE)");
        LTLSPEC = ltlBuilder.toString();
    }
    //setters
    private void setNuSMVFilePathWrite(String filePathWrite) {
        NuSMVFilePathWrite = filePathWrite;
    }
    public void setNumMotifs(int numMotifs) {
        this.numMotifs = numMotifs;
        this.numMotifsMinus = numMotifs - 1;
    }
    public void setMotifEdgeList(ArrayList<Edge> motifEdgeList) {
        this.motifEdgeList = motifEdgeList;
        setNumMotifs(motifEdgeList.size());
    }
    public void setMotifNodeList(ArrayList<String> motifNodeList) {
        this.motifNodeList = motifNodeList;
    }
    //getters
    public ArrayList<String> getMotifNodeList() {
        return motifNodeList;
    }


}

