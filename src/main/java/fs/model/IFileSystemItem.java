package fs.model;

public interface IFileSystemItem {
	public String accept(IFileSystemVisitor visitor);
}