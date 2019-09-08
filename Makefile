# Bridge-Game makefile
#  2019


.SUFFIXES: .java .class

LIB = ./lib
SRCDIR = src
BINDIR = bin
TESTDIR = test
DOCDIR = doc

CLI = $(LIB)/cli/commons-cli-1.3.1.jar
ASM = $(LIB)/asm/asm-7.1.jar:$(LIB)/asm/asm-commons-7.1.jar:$(LIB)/asm/asm-tree-7.1.jar
JUNIT = $(LIB)/Junit/junit-4.12.jar:$(LIB)/Junit/hamcrest-core-1.3.jar
JACOCO = $(LIB)/jacoco/org.jacoco.core-0.8.4.201905082037.jar:$(LIB)/jacoco/org.jacoco.report-0.8.4.201905082037.jar:
TOOLS = $(LIB)/tools/

JAVAC = javac
JFLAGS = -g -d $(BINDIR) -cp $(BINDIR):$(JUNIT)

vpath %.java $(SRCDIR):$(TESTDIR)
vpath %.class $(BINDIR)

# define general build rule for java sources
.SUFFIXES:  .java  .class

.java.class:
	$(JAVAC)  $(JFLAGS)  $<

#default rule - will be invoked by make

all: 				Card.class \
				Person.class \
				Lesson.class \
				GUI.class \
				Game.class \
				BackgroundPanel.class\
				LoadScreen.class\
				MenuScreen.class\
				GameScreen.class\
				GameController.class\
				Bridge.class
				 
					

# rule to run the class

run:
	java -cp bin Game 

MVC:
	java -cp bin Bridge

doc:
	javadoc -d $(DOCDIR) $(SRCDIR)/*.java
 
.PHONY: all doc clean


# Rules for unit testing
test_classes: all PersonTest.class LessonTest.class CardTest.class

test: test_classes
	java -ea -cp $(BINDIR):$(JUNIT) org.junit.runner.JUnitCore LessonTest
	java -ea -cp $(BINDIR):$(JUNIT) org.junit.runner.JUnitCore PersonTest
	java -ea -cp $(BINDIR):$(JUNIT) org.junit.runner.JUnitCore CardTest
	
# Rules for generating tests coverage
jacoco.exec: test_classes
	java -ea -javaagent:$(LIB)/jacoco/jacocoagent.jar -cp $(BINDIR):$(JUNIT) org.junit.runner.JUnitCore PersonTest
	java -ea -javaagent:$(LIB)/jacoco/jacocoagent.jar -cp $(BINDIR):$(JUNIT) org.junit.runner.JUnitCore CardTest
	java -ea -javaagent:$(LIB)/jacoco/jacocoagent.jar -cp $(BINDIR):$(JUNIT) org.junit.runner.JUnitCore LessonTest

report: jacoco.exec
	java -cp $(BINDIR):$(CLI):$(JACOCO):$(ASM):$(TOOLS) Report --reporttype html .

clean:
	@rm -f  $(BINDIR)/*.class
	@rm -f jacoco.exec *.xml *.csv
	@rm -Rf coveragereport
	@rm -Rf doc
