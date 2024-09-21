#COMPILER
COMPILER=gcc
FLAGS = -Wall -Wextra -std=c99 -pedantic 
#-fsanitize=address

#PARAMS
RANGE_BEGIN = 1
RANGE_END = 10000
COMM_BUFF_SIZE = 65534

#SOCKET
ADDRESS = "0.0.0.0"
PORT = 8080
#SECURITY
SECURITY_KEY = 123456789

run_client: C
stdintlib: stdintlib.c stdintlib.h
	$(COMPILER) -c stdintlib.c $(FLAGS)
syracuselib: syracuselib.c syracuselib.h
	$(COMPILER) -c syracuselib.c $(FLAGS)
stdsocket: stdsocket.c stdsocket.h
	$(COMPILER) -c stdsocket.c $(FLAGS)
corelib: coreLib.c coreLib.h
	$(COMPILER) -c coreLib.c $(FLAGS)
C: main.c syracuselib stdintlib stdsocket corelib
	$(COMPILER) -o main syracuselib.o stdintlib.o stdsocket.o coreLib.o main.c $(FLAGS)
	./main $(RANGE_BEGIN) $(RANGE_END) $(PORT) $(SECURITY_KEY) $(COMM_BUFF_SIZE)
archivage:
	tar -zcvf tab.tar *c *h Makefile
clean: 
	rm -f *.o main