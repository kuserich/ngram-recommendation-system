import fs.FSClient;
import kave.KaVEClient;

public class RunMeInClass {
	public static void main(String[] args) {
		new FSClient().run();
		new KaVEClient().run();
	}
}