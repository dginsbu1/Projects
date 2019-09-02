import sys, string
from TextFilter import TextFilter
from LowerCaser import LowerCaser
from Splitter import Splitter
from NonEnglishFilter import NonEnglishFilter
from UniqueWordCounter import UniqueWordCounter
from DocumentProcessor import DocumentProcessor
from DocumentProcessor import DocumentProcessorBuilder




class DocumentProcessorController:
    def __init__(self):
        self._searchString = ""
        self._path = ""
        self._grep = False
        self._wc = False
        self._text = ""
        self._builder = DocumentProcessorBuilder()
        self._processor = None

    def beginProcess(self, args):
        self._processor = self.buildProcessor(args)
        with open(self._path) as file:
            self._text = file.read()
        result = self._processor.process(self._text)
        if not (self._wc):
            resultString = str(result)
            print(resultString)
        else:
            for pair in result:
                print(pair)

    def buildProcessor(self, args):
        self.reset()
        self._builder = DocumentProcessorBuilder()
        if args[1] == "grep":  # basic grep ex. "grep this silly.txt"
            self._searchString = args[2]
            self._path = args[3]
            self._grep = True
            self._builder.setLineFilter(TextFilter(self._searchString))
            if not (len(args) == 4):  # complicated grep ex.  "grep this silly.txt | wc"
                self._wc = True
                self.setWC(self._builder)
        elif args[1] == "wc":  # just wc. ex. "wc silly.text"
            self._path = args[2]
            self._wc = True
            self.setWC(self._builder)
        else:
            print("seems like bad input to me")
        processor = self._builder.build()
        return processor

    # resets all variables that may have been changed in past iteration.
    def reset(self):
        self._searchString = ""
        self._path = ""
        self._text = ""
        self._grep = False
        self._wc = False
        self._builder = DocumentProcessorBuilder()
        self._processor = None

    def setWC(self, builder):
        builder.setCaseConverter(LowerCaser())
        builder.setWordFinder(Splitter())
        builder.setNonABCFilter(NonEnglishFilter())
        builder.setWordCounter(UniqueWordCounter())
