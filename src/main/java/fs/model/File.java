package fs.model;

public class File implements IFileSystemItem {

	public String name;

	public  File(String name) {
		this.name = name;
		
	}
	
	public String accept(IFileSystemVisitor visitor) {
		return visitor.visit(this);
	}
}
