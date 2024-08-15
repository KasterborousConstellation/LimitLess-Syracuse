#JAVA
SOURCE_PATH = SyracuseInterface/src
SOURCES := $(shell find $(SOURCE_PATH) -name "*.java")
COMPILER_PATH = SyracuseInterface/out
JAR_OUTPUT = SyracuseInterface/build/SyracuseInterface.jar
MANIFEST_PATH = $(SOURCE_PATH)/MANIFEST.MF
#SOCKET
ADDRESS = "0.0.0.0"
PORT = 8080
#FILE
DIR_PATH = /tmp/syracuse/
DATA_PER_FILE = 1000000
#PROJECT
PROJECT_NAME = KTBSCollatz

run_server: java
java:
	rm -rf $(COMPILER_PATH)
	javac -d $(COMPILER_PATH) $(SOURCES)
	jar cvmf $(MANIFEST_PATH) $(JAR_OUTPUT) -C $(COMPILER_PATH) .
	java -jar $(JAR_OUTPUT) $(ADDRESS) $(PORT) $(DIR_PATH) $(DATA_PER_FILE) $(PROJECT_NAME)
archivage:
	tar -zcvf tab.tar *c *h Makefile
clean: 
	rm -f *.o main