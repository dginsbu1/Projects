import sys, string
from TextFilter import TextFilter
from LowerCaser import LowerCaser
from Splitter import Splitter
from NonEnglishFilter import NonEnglishFilter
from UniqueWordCounter import UniqueWordCounter

class DocumentProcessor:

    def __init__(self, builder):
        self.lineFilter = builder.getLineFilter()
        self.caseConverter = builder.getCaseConverter()
        self.nonABCFilter = builder.getNonABCFilter()
        self.wordFinder = builder.getWordFinder()
        self.wordCounter = builder.getWordCounter()
        self.grep = not (self.lineFilter is None)
        self.wc = not (self.caseConverter is None)

        # The “DocumentProcessor.process” method will return a
        # map that maps a unique word to the number of times it appeared in the document. MyApp will then print out the
        # results to the console, with one word-number combination per line

    def process(self, text):
        self.text = text
        if self.grep and self.wc:
            return self.processAll()
        elif self.grep:
            return self.processGrep()
        elif self.wc:
            return self.processWc()
        else:
            print("Neither grep no WC was called")

    def processAll(self):
        self.processGrep()
        return self.processWc()

    def processGrep(self):
        self.text = self.lineFilter.process(self.text)
        return self.text

    def processWc(self):
        mySet = self.wordCounter.process(self.nonABCFilter.process(self.wordFinder.process(self.caseConverter.process(self.text))))
        return mySet

    #def toMap(self, set):
     #   set.forEach(t -> map.put(t.getKey(), t.getValue()))  # contains same key and value pair
      #  return map
class DocumentProcessorBuilder:
    def __init__(self):
        self.lineFilter = None
        self.caseConverter = None
        self.nonABCFilter = None
        self.wordFinder = None
        self.wordCounter = None
        self.text = ""

    def build(self):
        return DocumentProcessor(self)

    def getLineFilter(self):
        return self.lineFilter

    def getCaseConverter(self):
        return self.caseConverter

    def getNonABCFilter(self):
        return self.nonABCFilter

    def getWordFinder(self):
        return self.wordFinder

    def getWordCounter(self):
        return self.wordCounter

    def setLineFilter(self, lineFilter):
        self.lineFilter = lineFilter
        return self

    def setCaseConverter(self, caseConverter):
        self.caseConverter = caseConverter
        return self

    def setNonABCFilter(self, nonABCFilter):
        self.nonABCFilter = nonABCFilter
        return self

    def setWordFinder(self, wordFinder):
        self.wordFinder = wordFinder
        return self

    def setWordCounter(self, wordCounter):
        self.wordCounter = wordCounter
        return self
