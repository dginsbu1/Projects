import java.io.IOException;

public class NuSMVRunner {
    //runs a the file
    public void runNuSMV() throws IOException, InterruptedException {
        Runtime rt = Runtime.getRuntime();
        String command = "cmd.exe /c"+"NuSMV -bmc -bmc_length 0 "+Controller.NuSMVFileWrite+" > "+Controller.NuSMVResults;
        Process child = Runtime.getRuntime().exec(command);
    }
    public void runNuSMV(String path) throws IOException, InterruptedException {
        Runtime rt = Runtime.getRuntime();
        String command = "cmd.exe /c"+"NuSMV -bmc -bmc_length 0 "+path+" > "+Controller.NuSMVResults;
        Process child = Runtime.getRuntime().exec(command);
    }
}
