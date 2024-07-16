COMPILER=gcc
FLAGS = -Wall -Wextra -Werror -std=c99 -pedantic
all: main
stdintlib: stdintlib.c stdintlib.h
	$(COMPILER) -c stdintlib.c $(FLAGS)
syracuselib: syracuselib.c syracuselib.h
	$(COMPILER) -c syracuselib.c $(FLAGS)
main: main.c syracuselib stdintlib
	$(COMPILER) -o main syracuselib.o stdintlib.o main.c $(FLAGS)
archivage:
	tar -zcvf tab.tar *c *h Makefile