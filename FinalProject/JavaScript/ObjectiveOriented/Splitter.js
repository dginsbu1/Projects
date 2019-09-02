


class Splitter{

    process(text){
        return text.split(/\s/g);
        //return text.split(/\n+/);
    }

}

module.exports = Splitter;
