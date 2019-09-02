package OOprogram;

public class LowerCaser implements CaseConverter{

    @Override
    public String process(String text) {
        return text.toLowerCase();
    }
}
