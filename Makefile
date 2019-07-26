# Binary Tree Assignmnet program makefile
# Chris Cushway
# 12 March 2018

JAVAC=/usr/bin/javac
.SUFFIXES: .java .class

SRCDIR=src
BINDIR=bin

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<

CLASSES= Card.class Person.class Lesson.class Game.class
CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)

default: $(CLASS_FILES)

run:
	java -cp bin Game

clean:
	rm $(BINDIR)/*.class
