# XiaGoLandProject
This Java application provides a service for indexing text files.
The console interface allows for: 
(i) specifying the indexed files and directories
(ii) querying files containing a given word.

HOW TO USE:
Download GoLandProj.jar
Move GoLandProj.jar to the directory you want to run the application in.
Navigate to the same directory in terminal.
Run: java -jar GoLandProj.jar [arg1] [arg2] [arg3]
Fill in the required arguments with the commands below!
You can also download the entire GoLandProj zip and run it through intellij.

View src/DesignDoc for a detailed description of all commands.

Indexing commands:

indexBy [size		sizeDescending	name		nameDescending]
display
specifyFile [fileName]
specifyByIndex [indexOfFile]

Searching commands:

tokenize
searchAll [keyword]
searchFile [fileName] [keyword]

Example (CWD is src/Articles):

java -jar GoLandProj.jar indexBy size
java -jar GoLandProj.jar display
^^^
This will print out the indexed files in /Articles in the order of file size in bytes.

java -jar GoLandProj.jar indexBy sizeDescending
java -jar GoLandProj.jar specifyByIndex 0
^^^
This will print out the properties of the largest file.

java -jar GoLandProj.jar tokenize
java -jar GoLandProj.jar searchFile “Brenda Goodman” than
^^^
This will print out the number of times “than” appears in the file “Brenda Goodman”

java -jar GoLandProj.jar searchAll than
^^^
This will print out all the files that contain the word “than”

Thank you for your time and consideration. I greatly appreciate this opportunity, and I hope I showed you not only my programming abilities, but also my excitement to work on JetBrains applications!
