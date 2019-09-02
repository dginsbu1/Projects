

const LowerCaser = require("./LowerCaser");
const NonEnglishFilter= require("./NonEnglishFilter");
const Splitter= require("./Splitter");
const UniqueWordCounter=require("./UniqueWordCounter");
const LineFilter = require("./LineFilter");

class DocumentProcessorController{

    constructor(){

    }

    beginProcess(args){
        this.buildProcessor(args);
        //set up text
        const fs = require('fs');
        let url = "file:"+this.path;
        const fileUrl = new URL(url);
        let text = fs.readFileSync(fileUrl).toString();
         //console.log(text);
        //run the processor
        let result = this.processor.process(text);
        if(this.grep && !this.wc){
            this.printMe(result);
        }
        else{
            this.printWC(result);
        }
    }
    buildProcessor(args){
        this.reset();
        this.builder = new DocumentProcessorBuilder();
        this.grep = false;
        this.wc = false;
        this.path = ""
        this.searchString = ""
        if (args[2] == "grep") {
            this.searchString = args[3];
            this.path = args[4];
            this.grep = true;
            this.setGrep(this.builder);
            if (args.length = 7 && args[6] == "wc") {
                this.builder.setCaseConverter(new LowerCaser());
                this.builder.setWordFinder(new Splitter());
                this.builder.setNonABCFilter(new NonEnglishFilter());
                this.builder.setWordCounter(new UniqueWordCounter());
                this.wc = true;
            }
            //else{console.log("just grep \n");}
        } else {
            this.path = args[3];
            this.wc = true;
            this.builder.setCaseConverter(new LowerCaser());
            this.builder.setWordFinder(new Splitter());
            this.builder.setNonABCFilter(new NonEnglishFilter());
            this.builder.setWordCounter(new UniqueWordCounter());
        }
        this.processor = this.builder.build();
    }

    printMe(ans) {
            console.log(ans);
    }
    printWC(ans) {
        for (var i in ans) {
            console.log(`${i}, ${ans[i]}`);
        }
    }
    setWC(builder1) {
        //console.log(typeof builder);

    }

    setGrep(builder) {
        builder.setLineFilter(new LineFilter(this.searchString));
    }

    reset() {
        this.searchString = "";
        this.path = null;
        this.text = null;
        this.grep = false;
        this.wc = false;
        this.builder = null;
        this.processor = null;
    }
}

class DocumentProcessor{
    constructor(builder) {
        this.lineFilter = builder.getLineFilter();
        this.caseConverter = builder.getCaseConverter();
        this.nonABCFilter = builder.getNonABCFilter();
        this.wordFinder = builder.getWordFinder();
        this.wordCounter = builder.getWordCounter();
        console
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
        return this.wordCounter.process(this.nonABCFilter.process(this.wordFinder.process(this.caseConverter.process(this.text))));
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

module.exports = DocumentProcessorController;
