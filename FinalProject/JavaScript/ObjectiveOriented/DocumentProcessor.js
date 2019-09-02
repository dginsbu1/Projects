const LowerCaser = require("./LowerCaser");
const NonEnglishFilter= require("./NonEnglishFilter");
const Splitter= require("./Splitter");
const UniqueWordCounter=require("./UniqueWordCounter");
const LineFilter = require("./LineFilter");

class DocumentProcessor{
    constructor(builder) {
        this.lineFilter = builder.getLineFilter();
        //console.log(builder.lowe)
        this.caseConverter = builder.getCaseConverter();
        this.nonABCFilter = builder.getNonABCFilter();
        this.wordFinder = builder.getWordFinder();
        this.wordCounter = builder.getWordCounter();
        this.grep = this.lineFilter != undefined;
        this.wc = this.caseConverter != undefined;
    }
    process(text){
        this.text = text;
        if(this.grep && this.wc){
            return this.processAll();
        }
        else if(this.grep){
            return this.processGrep();
        }
        else if(this.wc){
            return this.processWc();
        }

    }

    processAll() {
        this.processGrep();
        return this.processWc();
    }
    processGrep() {
        this.text = this.lineFilter.process(this.text);
        return this.text;
    }
    processWc() {
        this.wordCounter.process(this.nonABCFilter.process(this.wordFinder.process(this.caseConverter.process(this.text))));
        return undefined;
    }
}
class DocumentProcessorBuilder {
    build() {
        return new DocumentProcessor(this);
    }

    setLineFilter(lineFilter) {
        this.lineFilter = lineFilter;
        return this;
    }

    setCaseConverter(caseConverter) {
        this.caseConverter = caseConverter;
        return this;
    }

    setNonABCFilter(nonABCFilter) {
        this.nonABCFilter = nonABCFilter;
        return this;
    }

    setWordFinder(wordFinder) {
        this.wordFinder = wordFinder;
        return this;
    }

    setWordCounter(wordCounter) {
        this.wordCounter = wordCounter;
        return this;
    }

    getLineFilter() {
        return this.lineFilter;
    }

    getCaseConverter() {
        return this.caseConverter;
    }

    getNonABCFilter() {
        return this.nonABCFilter;
    }

    getWordFinder() {
        return this.wordFinder;
    }

    getWordCounter() {
        return this.wordCounter;
    }
}
    

module.exports.DocumentProcessor = DocumentProcessor;
module.exports.DocumentProcessorBuilder = DocumentProcessorBuilder;
