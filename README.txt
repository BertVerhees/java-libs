This was a copy of the https://github.com/openEHR/java-libs repository.

This copy is created to present a adl-parser-antlr which is a replacement of adl-parser in the original.
This version runs through all tests of the original version parser.
So functional it is about the same.
(ArchetypeOntology got one more paramater in constructor: languages_available)

The parser does not break on Exceptions. This is because of a project requirement.
It stores errormessages formatted in ValidationError-format in a error-list.
For convenience there is a generated Exception trace which can be thrown by the method: parser.generatedParserException();


The reason for replacement is that this uses a antlr4 grammar for ADL1.4
Antlr4 has many advantages above javacc which was the grammar-kind in the original version.
- It has many target languages, javacc only one.
- It generates more simple and better maintainable code
- the last non-beta version of javacc was from 1996.
https://javacc.org/javacc-release-notes

There are many blog's on the internet which explain the advantages and disadvantages of one or the other.
Just google for antlr4 vs javacc.
For example, javacc generated code parses faster. But in my opinion, the antlr4 performance is good enough.

There is also on github a repository containing a ADL2.0 grammar
You can find it here:
https://github.com/nedap/archie

Have good luck.


Java openEHR Implementation project
-----------------------------------

VERSION
        Current version 1.1.0-SNAPSHOT