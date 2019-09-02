import sys, string
import re


class TextFilter:

    def __init__(self, searchString):
        self._searchString = searchString

    def process(self, text):
        lines = re.split("\n", text)
        array = []
        for line in lines:
            if self._searchString not in line:
                continue
            array.append(line)
        return "\n".join(array)

