import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.utils.io.IReadingArchive;
import cc.kave.commons.utils.io.ReadingArchive;
import examples.IoHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SentenceExtractor {

    public List<List<APIToken>> extract(String contextsDirectory) {
        try {
            Set<String> inputContexts = getInputZips(contextsDirectory);
            List<List<APIToken>> apiSentences = new ArrayList<>();

            for(String inputContext : inputContexts) {
                apiSentences.addAll(processZip(contextsDirectory + "/" + inputContext));
            }
            
            return apiSentences;
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<List<APIToken>> processZip(String inputFilePath) {
        System.out.println();
        System.out.println("+-------------------------------------------+");
        System.out.println("PROCESSING "+inputFilePath);

        List<List<APIToken>> apiSentences = new ArrayList<>();

        try(IReadingArchive ra = new ReadingArchive(new File(inputFilePath))) {
            while(ra.hasNext()) {
                // within the slnZip, each stored context is contained as a single file that
                // contains the Json representation of a Context.
                Context context = ra.getNext(Context.class);

                // the events can then be processed individually
                apiSentences.addAll(processContext(context));
            }
        }
        return apiSentences;
    }

    private List<List<APIToken>> processContext(Context context) {
        List<APISentenceTree> apiSentenceTrees = process(context.getSST());
        
        List<List<APIToken>> apiSentences = new ArrayList<>();
        for(APISentenceTree asp : apiSentenceTrees) {
            List<List<APIToken>> kk = asp.flatten();
            apiSentences.addAll(kk);
            System.out.println(kk);
        }
        return apiSentences;
    }

    private List<APISentenceTree> process(ISST sst) {
        // SSTs represent a simplified meta model for source code. 
        // You can use the various accessors to browse the contained information
        
        List<APISentenceTree> apiSentences = new ArrayList<>();
        
        for(IMethodDeclaration md : sst.getMethods()) {
            APISentenceTree sentence = new APISentenceTree();
            md.accept(new APIVisitor(), sentence);
            if(!sentence.isEmpty()) {
                apiSentences.add(sentence);
            }
        }
        
        return apiSentences;
    }

    /**
     * Returns a Set of zip files in a given file path.
     * 
     * Notice that this returns a Set with a single entry if the given path is a file (i.e. not a directory).
     * If the given path is a directory, this function will find and return all zips in all subdirectories as well
     * as the given directory. The directories are traversed recursively using {@link IoHelper#findAllZips(String)}.
     * 
     * @see IoHelper#findAllZips(String)
     * 
     * @param path
     *          input path, can be a directory
     * @return
     *          Set of paths to all zip files at given path
     *          
     * @throws FileNotFoundException
     *          thrown if the given path does not exist or cannot be found
     */
    private Set<String> getInputZips(String path) throws FileNotFoundException {
        File file = new File(path);
        
        if(!file.exists()) {
            throw new FileNotFoundException("Given path does not exist: "+path);
        }
        
        Set<String> inputDirectories;
        
        // recursively traverse directory path if the given path is a directory 
        // or simply  add the given path to inputDirectories if it's not
        if(file.isDirectory()) {
            inputDirectories = IoHelper.findAllZips(path);
        } else {
            inputDirectories = new HashSet<>();
            inputDirectories.add(path);
        }
        
        return inputDirectories;
    }
}
