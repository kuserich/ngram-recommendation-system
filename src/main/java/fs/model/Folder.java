package fs.model;

public class Folder implements IFileSystemItem {

	public String name;
	public IFileSystemItem[] items;

	public Folder(String name, IFileSystemItem... items) {
		this.name = name;
		this.items = items;

	}

	public String accept(IFileSystemVisitor visitor) {
		return visitor.visit(this);
	}
}