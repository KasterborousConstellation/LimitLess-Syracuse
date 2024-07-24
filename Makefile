#COMPILER
COMPILER=gcc
FLAGS = -Wall -Wextra -std=c99 -pedantic

#PARAMS
N_THREADS = 10
RANGE_BEGIN = 1000
RANGE_END = 2000
#JAVA
SOURCE_PATH = SyracuseInterface/src
SOURCES := $(shell find $(SOURCE_PATH) -name "*.java")
COMPILER_PATH = SyracuseInterface/out
JAR_OUTPUT = SyracuseInterface/build/SyracuseInterface.jar
MANIFEST_PATH = $(SOURCE_PATH)/MANIFEST.MF
#SOCKET
ADDRESS = "0.0.0.0"
PORT = 8080
run_server: C
run_client: java
java:
	rm -rf $(COMPILER_PATH)
	javac -d $(COMPILER_PATH) $(SOURCES)
	jar cvmf $(MANIFEST_PATH) $(JAR_OUTPUT) -C $(COMPILER_PATH) .
	java -jar $(JAR_OUTPUT) $(ADDRESS) $(PORT) $(N_THREADS)
stdintlib: stdintlib.c stdintlib.h
	$(COMPILER) -c stdintlib.c $(FLAGS)
syracuselib: syracuselib.c syracuselib.h
	$(COMPILER) -c syracuselib.c $(FLAGS)
stdsocket: stdsocket.c stdsocket.h
	$(COMPILER) -c stdsocket.c $(FLAGS)
C: main.c syracuselib stdintlib stdsocket
	$(COMPILER) -o main syracuselib.o stdintlib.o stdsocket.o main.c $(FLAGS)
	./main $(N_THREADS) $(RANGE_BEGIN) $(RANGE_END)
archivage:
	tar -zcvf tab.tar *c *h Makefile
clean: 
	rm -f *.o main