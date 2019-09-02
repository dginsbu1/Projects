


class NonEnglishFilter{

    process(lines){
        return lines.map(word => word.replace(/[^a-zA-Z]/g, ''))
            .filter(word => !(word == ""));//get rid of "" that were counted
    }

}

module.exports = NonEnglishFilter;
