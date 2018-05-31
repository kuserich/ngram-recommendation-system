package extractor;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.utils.io.IReadingArchive;
import cc.kave.commons.utils.io.ReadingArchive;
import util.IoHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class SentenceExtractor {

    private static final int MAX_FLATTEN_DEPTH = 10;
    
    /**
     * Returns all API sentences from all contexts in the given directory.
     * 
     * @param contextsDirectory
     *          path to the directory containing context files that should be processed
     *          
     * @return
     *          all API sentences in all contexts in the given directory
     */
    public void extract(String contextsDirectory, String outputDirectory) {
        try {
            IoHelper.createDirectoryIfNotExists(outputDirectory);
            Set<String> inputContexts = getInputZips(contextsDirectory);

            int cnt = 1;
            int totalFiles = inputContexts.size();
            for(String inputContext : inputContexts) {
                processZip(contextsDirectory + "/" + inputContext, outputDirectory);
                System.out.println("\n\n\nFINISHED PROCESSING FILE "+String.valueOf(cnt++)+"/"+String.valueOf(totalFiles));
                System.out.println("========================================================================================");
            }
            
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void processZip(String inputFilePath, String outputDirectory) {
        System.out.println();
        System.out.println("PROCESSING "+inputFilePath);

        int cnt = 1;
        try(IReadingArchive ra = new ReadingArchive(new File(inputFilePath))) {
            while(ra.hasNext()) {
                System.out.print("\rProcessing entry: "+String.valueOf(cnt++)+"/"+ra.getNumberOfEntries());

                // within the slnZip, each stored context is contained as a single file that
                // contains the Json representation of a Context.
                Context context = ra.getNext(Context.class);

                // the events can then be processed individually
                processContext(context, outputDirectory);
            }
        }
    }

    /**
     * Return all API sentences from the given context.
     * 
     * This method calls {@link #process(ISST)} with the SST from the context
     * and returns a list of all API sentences that can be created from the 
     * results of {@link #process(ISST)}}.
     * 
     * @see #process(ISST) 
     *          method that returns all {@link APISentenceTree} objects 
     *          from the context
     * @see APISentenceTree#flatten() 
     *          method that returns all API sentences from the 
     *          {@link APISentenceTree} objects
     * 
     * @param context
     *          context
     *          
     * @return
     *          list of all API sentences from the given context
     */
    private void processContext(Context context, String outputDirectory) {
        List<APISentenceTree> apiSentenceTrees = process(context.getSST());
        
        for(APISentenceTree asp : apiSentenceTrees) {
            if(asp.numberOfSentences() < 100000L) {
                List<List<APIToken>> apiSentences = asp.flatten(MAX_FLATTEN_DEPTH);
                Map<String, List<List<APIToken>>> bucketizedSentences = bucketizeApiSentences(apiSentences);
                try {
                    if(apiSentences.size() > 0) {
                        for(String key : bucketizedSentences.keySet()) {
                            if(bucketizedSentences.get(key).size() > 0) {
                                String filename = outputDirectory+key+".txt";
                                IoHelper.appendAPISentencesToFile(filename, bucketizedSentences.get(key), 2);
                            }
                        }
                    }
                } catch(IOException e) {
                    e.printStackTrace();
                }
                
            } else {
                System.out.println("\n[WARN]\t!!! SKIPPING TOO LARGE BODY !!! (~"+asp.numberOfSentences()+" sentences)\n");
            }
        }
    }

    /**
     * 
     * @param apiSentences
     * @return
     */
    private Map<String, List<List<APIToken>>> bucketizeApiSentences(List<List<APIToken>> apiSentences) {
        Map<String, List<List<APIToken>>> buckets = new HashMap<>();
        for(List<APIToken> sentence : apiSentences) {
            Map<String, List<APIToken>> currentMap = new HashMap<>();
            for(APIToken token : sentence) {
                if(!currentMap.containsKey(token.getNamespace())) {
                    currentMap.put(token.getNamespace(), new ArrayList<>());
                }
                currentMap.get(token.getNamespace()).add(token);
            }
            for(String key : currentMap.keySet()) {
                if(!buckets.containsKey(key)) {
                    buckets.put(key, new ArrayList<>());
                }
                buckets.get(key).add(currentMap.get(key));
            }
        }
        return buckets;
    }

    /**
     * Returns all {@link APISentenceTree} objects from the given syntax tree.
     * There will be at most one {@link APISentenceTree} object per method declaration.
     * 
     * Notice that these are not the API sentences but only their trees.
     * To retrieve API sentences, the trees must be flattened using
     * {@link APISentenceTree#flatten()}.
     * 
     * @param sst
     *          syntax tree
     *          
     * @return
     *          List of {@link APISentenceTree} objects
     */
    private List<APISentenceTree> process(ISST sst) {
        List<APISentenceTree> apiSentenceTrees = new ArrayList<>();
        
        for(IMethodDeclaration md : sst.getMethods()) {
            APISentenceTree sentence = new APISentenceTree();
            md.accept(new APIVisitor(), sentence);
            if(!sentence.isEmpty()) {
                apiSentenceTrees.add(sentence);
            }
        }
        
        return apiSentenceTrees;
    }

    /**
     * Returns a set of zip files in a given file path.
     * 
     * Notice that this returns a set with a single entry if the given path is a file (i.e. not a directory).
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
