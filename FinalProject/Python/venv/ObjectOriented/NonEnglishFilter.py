import sys, string
import re

# strip out all non-alphabetic characters.
# Eliminate any “words” that are just white space.
class NonEnglishFilter:
    def process(self, wordList):
        newList = []
        for word in wordList:
            newWord = re.sub("[^a-zA-Z]", '', word).strip()
            if not(newWord == ""):
                newList.append(newWord)
        return newList


    

