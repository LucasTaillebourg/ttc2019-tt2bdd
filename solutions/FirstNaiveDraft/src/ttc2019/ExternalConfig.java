package ttc2019;

public class ExternalConfig {
    private static final String LINUX_MODEL_PATH = "../../models/GeneratedI4O2Seed42.ttmodel";
    private static final String WINDOWS_MODEL_PATH = "./models/GeneratedI4O2Seed42.ttmodel";
    private static final String OS = System.getProperty("os.name").toLowerCase();

    public static final String MODEL_PATH = isWindows() ? WINDOWS_MODEL_PATH : LINUX_MODEL_PATH;


    private static boolean isUnix() {
        return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix") );
    }
    private static boolean isWindows() {
        return (OS.contains("win"));
    }
}
