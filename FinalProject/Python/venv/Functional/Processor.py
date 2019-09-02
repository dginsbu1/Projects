import collections
import re
from functools import reduce
from sys import argv

class Processor:

    def __init__(self):
        pass

    def process(self, args):
        #set up grep and wc
        grep = False
        wc = False
        if args[1] == "grep":
            searchString = args[2]
            path = args[3]
            grep = True
            if len(args) == 6: #TODO double check num
                wc = True
        else:
            wc = True
            searchString = args[1]
            path = args[2]

        #get text
        text = []
        with open(path) as file:
            text = file.readlines()

        #perform task based on grep/wc
        if grep and not(wc):#complicated grep ex.  "grep this silly.txt"
            ans = filter(lambda line: searchString in line, text)#filter
            self.printN(ans);

        elif grep and wc:#grep this C:\Users\dgmon\YU\test.txt | wc
            self.printNL(
                self.countWords(
                filter(lambda word: not (word == ""),
                map(lambda word: re.sub("[^a-zA-Z]", '', word).strip(),
                reduce(list.__add__,
                map(lambda line: line.split(),
                map(lambda line: line.lower(),
                filter(lambda line: searchString in line,text))))))))

        elif not(grep) and wc:
            self.printNL(
                self.countWords(
                filter(lambda word: not(word == ""),
                map(lambda word: re.sub("[^a-zA-Z]", '', word).strip(),
                reduce(list.__add__,
                map(lambda line: line.split(),
                map(lambda line: line.lower(), text)))))))  # remove nonletters)

    def countWords(self, wordList):
        myDict = {}
        for word in list(wordList):
            if word in myDict:# if in dict then add one
                myDict[word] = myDict.get(word) + 1
            else:
                myDict[word] = 1
        return myDict.items()

    def printNL(self, ans):
        for line in ans:
            print(line, end="\n")

    def printN(self, ans):
        for line in ans:
            print(line, end="")
        print("", end="\n");

def main():
    args = argv
    processor = Processor()
    processor.process(args)

if __name__ == "__main__":
    main()