public class Edge implements Comparable<Edge>{
    private String from, to;
    private boolean pos, certain;
    private boolean origin; //used for determining which side to use for LTLSPEC in writerClass;

    public Edge(){}
    public Edge(String from, String to, boolean pos, boolean certain) {
        this.from = from;
        this.to = to;
        this.pos = pos;
        this.certain = certain;
    }
    //@depreciated
    public Edge(String from, String to, boolean pos, boolean certain, boolean origin) {
        this.from = from;
        this.to = to;
        this.pos = pos;
        this.certain = certain;
    }
    //@depreciated
    public Edge copyEdge(boolean origin){
        Edge edge = new Edge(this.from, this.to, this.pos, this.certain, origin);
        return edge;
    }
    //@depreciated
    public void setFront(boolean origin) {
        this.origin = origin;
    }
    //setters
    public void setFrom(String from){
        this.from = from;
    }
    public void setTo(String to){
        this.to = to;
    }
    public void setCertain(boolean certain) {
         this.certain = certain;
    }
    public void setPos(boolean pos) {
        this.pos = pos;
    }

    //getters
    public String getTo() {
        return to;
    }
    public boolean isFront(){
        return origin;
    }
    public String getFrom() {
        return from;
    }
    public boolean isPos() {
        return pos;
    }
    public String isPosString(){
        return (isPos()? "TRUE": "FALSE");
    }
    public boolean isCertain() {
        return certain;
    }
    public String isCertainString(){
        return (isCertain()? "TRUE" : "FALSE");
    }

    @Override
    public String toString(){
        String string = from+" "+to+" pos: "+pos+" certain: "+certain;
        return string;
    }

    @Override
    public int compareTo(Edge e) {
        int from = this.getFrom().compareTo(e.getFrom());
        if(from != 0) {
            return from;
        } else{
            int to = this.getTo().compareTo(e.getTo());
            if(to != 0){
                return to;
            } else{
                if(this.isCertain())return -1;
                else return 1;
            }
        }
    }
}