public class RunExample {

    /*
     * download the interaction data and unzip it into the root of this project (at
     * the level of the pom.xml). Unpack it, you should now have a folder that
     * includes a bunch of folders that have dates as names and that contain .zip
     * files.
     */
    public static String eventsDir = "Events-170301-2";

    /*
     * download the context data and follow the same instructions as before.
     */
    public static String contextsDir = "Contexts-170503/";
    
    public static void main(String[] args) {
//        new FSClient().run();
//        GettingStarted gs = new GettingStarted(eventsDir);
//        gs.run();
//        GettingStartedContexts gsc = new GettingStartedContexts(contextsDir);
//        gsc.run();
        
        SentenceExtractor se = new SentenceExtractor();
        se.extract(contextsDir);
    }
    
}
