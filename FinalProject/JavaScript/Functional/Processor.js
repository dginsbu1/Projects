function printMe(ans) {
    for(var i in ans) {
        console.log(ans[i]);
    }
}
function printWC(ans) {
    for(i in ans){
        console.log(`${i}, ${ans[i]}`);
    }
}
class Processor{
    constructor(){
    }

    process(args){
        // console.log(args)
        //set up grep ad wc
        let grep = false;
        let wc = false;
        let path = ""
        let searchString = ""
        if(args[2] == "grep") {
            searchString = args[3];
            path = args[4];
            grep = true;
            if (args.length = 7 && args[6] == "wc")
                wc = true;
        }else{
            path = args[3];
            wc = true;
        }
        //set up text
        const fs = require('fs');
        path = "file:"+path;
        //console.log(path);
        const fileUrl = new URL(path);
        let text = fs.readFileSync(fileUrl).toString();
        // console.log(text);

        //
        let lines = text.split(/\n+/);
        //printMe(lines);
        //perform task based on grep/wc structure
        if(grep && !wc){ //basic grep ex. "grep this silly.txt"
            let ans = lines.filter(line => line.includes(searchString));
            printMe(ans);
        }
        else if(grep && wc){//complicated grep ex.  "grep this silly.txt | wc"
            let ans =
                lines.filter(line => line.includes(searchString))
                    .reduce((text, word) => text+word, " ").toLowerCase().split(/\s/g)
                    .map(word => word.replace(/[^a-zA-Z_]/g, ''))
                    .filter(word => !(word == ""))//get rid of "" that were counted as words
                    .reduce(function(stats, word) {
                        if (stats[word]) {
                            stats[word] = stats[word] + 1;
                        } else {
                            stats[word] = 1;
                        }
                        return stats;
                    }, []);
            printWC(ans);
        } else if(wc) {//just wc. ex. "wc silly.text"
            let ans =
                lines
                    .reduce((text, word) => text + word, "").toLowerCase().split(/\s/g)
                    .map(word => word.replace(/[^a-zA-Z_]/g, ''))
                    .filter(word => !(word == ""))//get rid of "" that were counted as words
                    .reduce(function (stats, word) {
                        if (stats[word]) {
                            stats[word] = stats[word] + 1;
                        } else {
                            stats[word] = 1;
                        }
                        return stats;
                    }, []);
            printWC(ans);
        }
    }

}
let processor = new Processor();
processor.process(process.argv);
