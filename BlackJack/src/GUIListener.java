import java.io.*;

public interface GUIListener {
	void guiVerifyLogin(String username, String password);
	void guiExit() throws IOException;
}
