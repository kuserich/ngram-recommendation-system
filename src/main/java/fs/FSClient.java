package fs;

import fs.model.File;
import fs.model.Folder;
import fs.model.IFileSystemItem;
import fs.model.IFileSystemVisitor;

public class FSClient {

	public void run() {
		File a = new File("filea");
		File b = new File("filea");
		File c = new File("filea");

		Folder f1 = new Folder("f1", a, b);
		Folder f2 = new Folder("f1", f1, c);

		doSomething(a, new KindVisitor());
		doSomething(b, new KindVisitor());
		doSomething(c, new KindVisitor());
		doSomething(f1, new KindVisitor());
		doSomething(f2, new KindVisitor());
	}

	public static void doSomething(IFileSystemItem i, IFileSystemVisitor v) {
		// so the client does not need to know the concrete type of the file system
		// item, but just the interface.

		// typically, the clients knows the type of the visitor, but this does not
		// really make a difference, because the client will call the .accept() method
		// anyways

		System.out.println(i.accept(v));
	}

	public static class KindVisitor implements IFileSystemVisitor {

		public String visit(File folder) {
			return "this is a file";
		}

		public String visit(Folder folder) {
			return "this is a folder";
		}

	}
}