import sys, string
import re
from functools import reduce


class UniqueWordCounter:

    def process(self, wordList):
        # use a dictionary
       # reduce(lambda myDict, word: {word, myDict[word]+1} if word in myDict else {word, 1}, wordList)
        myDict = {}
        for word in wordList:
            if word in myDict:# if in dict then add one
                myDict[word] = myDict.get(word) + 1
            else:
                myDict[word] = 1
        set = myDict.items()
        return set

