
class UniqueWordCounter{

    process(words){

    return words.reduce(function(stats, word) {
            if (stats[word]) {
                stats[word] = stats[word] + 1;
            } else {
                stats[word] = 1;
            }
            return stats;
        }, []);
    }

}


module.exports = UniqueWordCounter;

