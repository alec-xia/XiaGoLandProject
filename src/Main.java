import java.io.*;
import java.nio.file.Files;
import java.util.*;

/** GoLand OA Project. 2/18/2022
 *  1. ALlows user to sort and index files.
 *  2. Allows user to tokenize files and search for keywords.
 *  @author Alec Xia
 */

public class Main {
    /* Current working repository of the user. */
    public static final File CWD = new File(System.getProperty("user.dir"));

    public static FileFilter txtOnly = pathname -> {
        boolean isFile = pathname.isFile();
        boolean isHidden = pathname.isHidden();

        try {
            String fileType = Files.probeContentType(pathname.toPath());
            if (fileType != null && !fileType.equals("text")) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("There is a malformed file.");
            System.exit(0);
        }
        return isFile && !isHidden;
    };

    public static void main(String... args) throws IOException, ClassNotFoundException {
        /* Creates a hidden .token directory */
        File tokens = new File(CWD, ".tokens");
        if (!tokens.isDirectory()) {
            tokens.mkdir();
        }

        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        /* Commands */
        switch (args[0]) {
            case "indexBy":
                argCheck(2, args);
                indexSort(args[1]);
                System.out.println("CWD indexed successfully.");
                break;
            case "display":
                argCheck(1, args);
                display();
                break;
            case "specifyFile":
                argCheck(2, args);
                specify(args[1]);
                break;
            case "specifyByIndex":
                argCheck(2, args);
                specifyByIndex(args[1]);
                break;
            case "tokenize":
                argCheck(1, args);
                tokenizeAll();
                System.out.println("Files tokenized.");
                break;
            case "search":
                argCheck(2, args);
                search(args[1]);
                break;
            case "appears":
                argCheck(3, args);
                appears(args[1], args[2]);
                break;
            default:
                System.out.println("Command does not exist.");
        }
        System.exit(0);
    }
    /* Makes sure each command takes the correct arguments */
    private static void argCheck(int expected, String[] args) {
        if (args.length != expected) {
            System.out.println("Incorrect arguments for command.");
            System.exit(0);
        }
    }

    /* Searches through files in CWD and prints names of the files containing keyword. */
    private static void search(String keyword) throws IOException{
        ArrayList<String> answers = new ArrayList<>();

        File[] files = CWD.listFiles(txtOnly);

        if (files == null) {
            System.out.println("No files in CWD.");
            System.exit(0);
        }

        for (File f : files) {
            try {
                FileInputStream tokenFile = new FileInputStream(".tokens/" + f.getName());
                ObjectInputStream deserializedFile = new ObjectInputStream(tokenFile);
                TreeMap<String, Integer> fileTokens = (TreeMap<String, Integer>) deserializedFile.readObject();
                if (fileTokens.containsKey(keyword)) {
                    answers.add(f.getName());
                }

            } catch (FileNotFoundException | ClassNotFoundException e) {
                System.out.println("Tokenize files before searching. Use command: tokenize");
                System.exit(0);
            }
        }
        System.out.println(keyword + " appears in files:");
        for (String fileName : answers) {
            System.out.println(fileName);
        }
    }
    /* Prints the number of times word appears in fileName. */
    private static void appears(String fileName, String word) throws IOException, ClassNotFoundException {
        try {
            FileInputStream tokenFile = new FileInputStream(".tokens/" + fileName);
            ObjectInputStream deserializedFile = new ObjectInputStream(tokenFile);
            TreeMap<String, Integer> fileTokens = (TreeMap<String, Integer>) deserializedFile.readObject();
            deserializedFile.close();
            Integer counter = fileTokens.get(word);
            System.out.println("Number of times " + word + " appears in " + fileName + ": " + counter);

        } catch (ClassNotFoundException e) {
            System.out.println("Tokenize files before searching. Use command: tokenize");
        }
    }
    /* Prints all files under CWD along with its index. */
    private static void display() throws IOException {
        PersistedIndex persisted = readFromPersistence();
        int index = 0;
        assert persisted != null;
        for (File f : persisted.getLibrary()) {
                    System.out.println(index + " " + persisted.getLibrary().get(index++).getName());
            }

    }
    /* Prints out file properties for file with specified name. */
    private static void specify(String fileName) {
        File specFile = null;

        for (File f : Objects.requireNonNull(CWD.listFiles())) {
            if (f.getName().equals(fileName)) {
                specFile = f;
                break;
            }
        }
        if (specFile == null) {
            System.out.println("No file with that name exists.");
            System.exit(0);
        }
        System.out.println("Name: " + specFile.getName());
        System.out.println("Size: " + specFile.length());
    }
    /* Prints out file properties for the ith file that was previously indexed. */
    private static void specifyByIndex(String index) throws IOException {
        PersistedIndex persistence = readFromPersistence();
        int i = Integer.parseInt(index);

        if (i < 0 || i > Objects.requireNonNull(persistence).getLibrary().size()) {
            System.out.println("No file exists at index " + i);
            System.exit(0);

        } else {
            File toSpec = persistence.getLibrary().get(i);
            System.out.println("Name: " + toSpec.getName());
            System.out.println("Size: " + toSpec.length());
        }
    }
    /* Takes in a keyword, and sorts the files in CWD by the named method. */
    private static void indexSort(String sortBy) throws IOException {
        switch (sortBy) {
            case "size":
                bySizeLowHigh();
                break;
            case "sizeDescending":
                bySizeHighLow();
                break;
            case "name":
                byNameAZ();
                break;
            case "nameDescending":
                byNameZA();
                break;
            default:
                System.out.println("Sorting method not recognized.");
        }
    }
    /* Sorts the text files in CWD by size. Stores the order of files in .persistence. */
    private static void bySizeLowHigh() throws IOException {

        File[] files = CWD.listFiles(txtOnly);

        if (files == null) {
            System.out.println("No text files are present in current directory.");
            System.exit(0);
        }

        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                long f1Size = f1.length();
                long f2Size = f2.length();
                return (int) (f1Size - f2Size);
            }
        });
        writeToPersistence(files);
    }
    /* Sorts the text files in CWD by size descending. Stores the order of files in .persistence. */
    private static void bySizeHighLow() throws IOException {

        File[] files = CWD.listFiles(txtOnly);

        if (files == null) {
            System.out.println("No text files are present in current directory.");
            System.exit(0);
        }

        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                long f1Size = f1.length();
                long f2Size = f2.length();
                return (int) (f2Size - f1Size);
            }
        });
        writeToPersistence(files);
    }
    /* Sorts the text files in CWD by alphabetical order. Stores the order of files in .persistence. */
    private static void byNameAZ() throws IOException {
        File[] files = CWD.listFiles(txtOnly);

        if (files == null) {
            System.out.println("No text files are present in current directory.");
            System.exit(0);
        }

        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                return f1.getName().compareTo(f2.getName());
            }
        });
        writeToPersistence(files);
    }
    /* Sorts the text files in CWD by reverse alphabetical order. Stores the order of files in .persistence. */
    private static void byNameZA() throws IOException  {
        File[] files = CWD.listFiles(txtOnly);

        if (files == null) {
            System.out.println("No text files are present in current directory.");
            System.exit(0);
        }

        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                return f2.getName().compareTo(f1.getName());
            }
        });
        writeToPersistence(files);
    }
    /* Applies tokenizeFile to all files in CWD. Then, stores serialized TreeMaps in .tokens. */
    private static void tokenizeAll() throws IOException {
        File[] files = CWD.listFiles(txtOnly);

        if (files == null) {
            System.out.println("No text files are present in current directory.");
            System.exit(0);
        }

        for (File f : files) {
            String name = f.getName();
            TreeMap<String, Integer> tokens = tokenizeFile(f);
            FileOutputStream file = new FileOutputStream(".tokens/" + name);
            ObjectOutputStream writer = new ObjectOutputStream(file);
            writer.writeObject(tokens);
            file.close();
            writer.close();
        }
    }
    /* Fail safe in case for some reason text file becomes unreadable. */
    public static String readStringSafe(File f, boolean log) {
        try {
            return Files.readString(f.toPath()).toLowerCase();
        } catch (IOException e) {
            e.printStackTrace();
            if (log) {
                System.out.println("There was a malformed file.");
            }
            System.exit(0);
            return null;
        }
    }
    /* First, splits the contents of file f into a String[] by common punctuation delimiters.
    *  Second, creates and returns a TreeMap that contains key: word, value: number of appearances. */
    public static TreeMap<String, Integer> tokenizeFile(File f) throws IOException {
        String content = readStringSafe(f, true);
        String[] tokens = content.split("[\n.,;: ]+");
        TreeMap<String, Integer> tokenMap = new TreeMap<>();

        for (String token : tokens) {
            if (tokenMap.containsKey(token)) {
                tokenMap.put(token, tokenMap.get(token) + 1);
            } else {
                tokenMap.put(token, 1);
            }
        }
        return tokenMap;
    }

    /* Helper method that returns the Persisted object in CWD/.persistence */
    private static PersistedIndex readFromPersistence() throws IOException {
        try {
            FileInputStream libFile = new FileInputStream(".persistence");
            ObjectInputStream deserializedFile = new ObjectInputStream(libFile);
            PersistedIndex persisted = (PersistedIndex) deserializedFile.readObject();
            deserializedFile.close();
            return persisted;
        } catch (FileNotFoundException | ClassNotFoundException e) {
            System.out.println("Current Working Directory has not been indexed.");
            System.exit(0);
            return null;
        }
    }
    /* Helper method that takes a file array, and serializes it into .persistence */
    private static void writeToPersistence(File[] fileList) throws IOException {
        PersistedIndex p = new PersistedIndex(fileList);
        FileOutputStream file = new FileOutputStream(".persistence");
        ObjectOutputStream writer = new ObjectOutputStream(file);
        writer.writeObject(p);
        file.close();
        writer.close();
    }
}