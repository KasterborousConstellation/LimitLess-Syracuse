#COMPILER
COMPILER=gcc
FLAGS = -Wall -Wextra -std=c99 -pedantic

#PARAMS
N_THREADS = 2
RANGE_BEGIN = 1000
RANGE_END = 2000


all: main
stdintlib: stdintlib.c stdintlib.h
	$(COMPILER) -c stdintlib.c $(FLAGS)
syracuselib: syracuselib.c syracuselib.h
	$(COMPILER) -c syracuselib.c $(FLAGS)
stdsocket: stdsocket.c stdsocket.h
	$(COMPILER) -c stdsocket.c $(FLAGS)
main: main.c syracuselib stdintlib stdsocket
	$(COMPILER) -o main syracuselib.o stdintlib.o stdsocket.o main.c $(FLAGS)
	./main $(N_THREADS) $(RANGE_BEGIN) $(RANGE_END)
archivage:
	tar -zcvf tab.tar *c *h Makefile
clean: 
	rm -f *.o main