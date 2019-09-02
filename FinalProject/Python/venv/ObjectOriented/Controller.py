from TextFilter import TextFilter
from LowerCaser import LowerCaser
from Splitter import Splitter
from NonEnglishFilter import NonEnglishFilter
from UniqueWordCounter import UniqueWordCounter
from DocumentProcessorController import DocumentProcessorController
from DocumentProcessor import DocumentProcessor
import re
from sys import argv

# work on the builder


class Controller:

    def __init__(self):
        pass

def main():
    args = argv
    controller = DocumentProcessorController()
    controller.beginProcess(args)
    if True == False:
        string = "This is a test \n this Is' a' test!\na teSt is, this. \nnot On@ @ the test@\nwhat abot, ??????    this\ndont be lazy, \nquality,"
        x = TextFilter("this")
        ans = x.process(string)  # filter
        print(ans, end="\n\n")

        caser = LowerCaser()
        ans = caser.process(ans)
        print(ans, end="\n\n")

        splitter = Splitter()
        ans = splitter.process(ans)
        print(ans, end="\n\n")

        letter = NonEnglishFilter()
        ans = letter.process(ans)
        print(ans, end="\n\n")

        counter = UniqueWordCounter()
        ans = counter.process(ans)
        for pair in ans:
            print(pair)
if __name__ == "__main__":
    main()

