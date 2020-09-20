SDIR= ./SRC
BIN=./bin

CC=javac
CFLAGS= -d $(BIN)

SocialMedia: Main
	jar cfe SocialMedia.jar $(BIN)/*.class 

Main:  
	$(CC) $(SDIR)/*.java $(CFLAGS)


clean :	 
	rm -rf $(BIN) SocialMedia.jar
	mkdir $(BIN)
