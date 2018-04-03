package kave;

import java.io.File;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.utils.io.ReadingArchive;

public class KaVEClient {
	
	private String contextsDir;
	
	public KaVEClient(String contextsDir) {
		this.contextsDir = contextsDir;	
	}
	
	// first, adapt pom.xml!
	
	public void run() {
		File f = new File(contextsDir);
		
		try (ReadingArchive ra = new ReadingArchive(f)) {
			while (ra.hasNext()) {
				Context ctx = ra.getNext(Context.class);
				ISST isst = ctx.getSST();
				ctx.getSST().accept(new InvocationVisitor(), null);
			}
		}
	}
}
