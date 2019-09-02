

const LowerCaser = require("./LowerCaser");
const NonEnglishFilter= require("./NonEnglishFilter");
const Splitter= require("./Splitter");
const UniqueWordCounter=require("./UniqueWordCounter");
const LineFilter = require("./LineFilter");
const DocumentProcessorController = require("./DocumentProcessorController");
class Controller {}

let processorController = new DocumentProcessorController();

processorController.beginProcess(process.argv);

module.exports = Controller;

//TODO TEXT
//
// const fs = require('fs');
// const fileUrl = new URL("file:C:\\Users\\dgmon\\YU\\test.txt");
// let text = fs.readFileSync(fileUrl).toString();
//
// let lineFilter = new LineFilter("this");
// ans = lineFilter.process(text);

// let lowerCaser = new LowerCaser();
// ans = lowerCaser.process(ans);
//
// let splitter = new Splitter();
// ans = splitter.process(ans);
//
// let nonEnglishFilter = new NonEnglishFilter();
// ans = nonEnglishFilter.process(ans);
//
// let uniqueWordCounter = new UniqueWordCounter();
// ans = uniqueWordCounter.process(ans);
//
//console.log(ans);
// for(i in ans){
//     console.log(`${i}, ${ans[i]}`);
// }
//

