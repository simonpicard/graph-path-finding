JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Main.java \
	Parser.java \
	Path.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
