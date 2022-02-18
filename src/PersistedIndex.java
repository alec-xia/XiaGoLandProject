import java.io.Serializable;
import java.util.ArrayList;
import java.io.File;
import java.util.Arrays;

public class PersistedIndex implements Serializable {
    private ArrayList<File> library = new ArrayList<>();

    public PersistedIndex(File[] files) {
        library.addAll(Arrays.asList(files).subList(0, files.length));
    }

    public ArrayList<File> getLibrary() {
        return library;
    }
}
