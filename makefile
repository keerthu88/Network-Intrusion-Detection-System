JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        Snids.java \
        SnidsListener.java \
        Rule.java \
        Index.java \
        RuleParser.java \
        Flag.java \
        SubRule.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
