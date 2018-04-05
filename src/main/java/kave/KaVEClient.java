package kave;

import java.io.File;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.utils.io.ReadingArchive;

public class KaVEClient {
	
	public static String contextsDir = "Contexts-170503";

	// first, adapt pom.xml!
	
	public void run() {
		File f = new File(contextsDir+"/01org/acat/src/ACAT.sln-contexts.zip");
		
		try (ReadingArchive ra = new ReadingArchive(f)) {
			while (ra.hasNext()) {
				Context ctx = ra.getNext(Context.class);
				ctx.getSST().accept(new InvocationVisitor(), null);
			}
		}
	}
}
