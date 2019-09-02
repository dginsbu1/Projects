
import sys, string
import re

class Splitter:

    def __init__(self):
        pass

    def process(self, text):
        lines = re.split("\\s+", text)
        return lines


