import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.utils.io.IReadingArchive;
import cc.kave.commons.utils.io.ReadingArchive;
import examples.IoHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SentenceExtractor {

    public void extract(String contextsDirectory) {
        try {
            Set<String> inputContexts = getInputZips(contextsDirectory);
            for(String inputContext : inputContexts) {
                processZip(contextsDirectory + "/" + inputContext);
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void processZip(String inputFilePath) {
        System.out.println();
        System.out.println("+-------------------------------------------+");
        System.out.println("PROCESSING "+inputFilePath);
        try(IReadingArchive ra = new ReadingArchive(new File(inputFilePath))) {
            while(ra.hasNext()) {
                // within the slnZip, each stored context is contained as a single file that
                // contains the Json representation of a Context.
                Context context = ra.getNext(Context.class);

                // the events can then be processed individually
                processContext(context);
            }
        }
    }

    private void processContext(Context context) {
        // a context is an abstract view on a single type declaration that contains of
        // two things:

        // 1) a simplified syntax tree of the type declaration
        process(context.getSST());

        // 2) a "type shape" that provides information about the hierarchy of the
        // declared type
//        process(context.getTypeShape());
    }

    private void process(ISST sst) {
        // SSTs represent a simplified meta model for source code. 
        // You can use the various accessors to browse the contained information
        
        // which type was edited?
        ITypeName declType = sst.getEnclosingType();

        
        Set<APITokenSet> sentences = new HashSet<>();
        Set<IMethodDeclaration> methodDeclarations = sst.getMethods();
        // which methods are defined?
        for(IMethodDeclaration md : methodDeclarations) {
            md.accept(new APIVisitor(), sentences);
        }
        
        // all references to types or type elements are fully qualified and preserve
        // many information about the resolved type
        declType.getNamespace();
        declType.isInterfaceType();
        declType.getAssembly();

        // TODO: a useful boi
        // you can distinguish reused types from types defined in a local project
        boolean isLocal = declType.getAssembly().isLocalProject();

        // the same is possible for all other <see>IName</see> subclasses, e.g.,
        // <see>IMethodName</see>
        IMethodName m = Names.getUnknownMethod();
        m.getDeclaringType();
        m.getReturnType();
        // or inspect the signature
        for (IParameterName p : m.getParameters()) {
            String pid = p.getName();
            ITypeName ptype = p.getValueType();
        }

    }

    private Map<String, Set<Set<APIToken>>> listOfMapsToMapOfLists(Set<Map<String, Set<APIToken>>> listOfMaps) {
        Map<String, Set<Set<APIToken>>> mapOfLists = new HashMap<>();

        listOfMaps.forEach(map -> {
            map.keySet().forEach(key -> {
                if(!mapOfLists.containsKey(key)) {
                    mapOfLists.put(key, new HashSet<>());
                }
                mapOfLists.get(key).add(map.get(key));
            });
        });
        
        return mapOfLists;
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
