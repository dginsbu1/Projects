package OOprogram;

// filter out lines that don’t meet grep search criteria
public interface LineFilter {
   public String process(String text);
}
