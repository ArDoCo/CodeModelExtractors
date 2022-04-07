# CodeModelExtractors

In here, you can find extractors for (simple) code models, that extract things like available classes and interfaces along their methods and comments.
The extraced information is saved to an ontology.


## Java

### Usage
```
usage: JavaCodeModelExtractor.jar
-e,--extend <arg>   path to the owl file that should be extended (instead
					of creating from empty)
-h,--help           print this message
-i,--in <arg>       path to the input directory
-o,--out <arg>      path to the output file that should be used for
					saving
-t,--output-format <arg>   specified output format: one of [JSON, OWL]
```
