package fs.model;

public interface IFileSystemVisitor {

	String visit(File folder);

	String visit(Folder folder);
}