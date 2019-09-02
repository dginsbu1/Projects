#include <stdio.h>
#include <stdlib.h> // For exit() function
#include <stdint.h>
#include <string.h>
#include <ctype.h>



//BASICS
char* path;
char * buffer;
char** text;
char* searchWord;
char grep = 0;
char wc = 0;
int totalLines = 0;
int wordCount;
int wordSpot;
char** words;
long long charTotal = 0;
void process(int argc, char* argv[]);
char* readFile(FILE *fp);
char** filter(char* buffer);
char** convertCase(char** text);
char** splitText(char** text);//return array of the words
char** nonAlphabeticFilter(char** words);
char* abcWordFilter(char* line);
int getLineCount(char *buffer);
void printText(char **stuff, int number);
struct wordMap;
struct wordMap *pairz;
struct wordMap * frequencyCounter(char** words);


int main(int argc, char* argv[]){
    process(argc, argv);
    return 0;
}

void process(int argc, char* argv[]) {
    if(strcmp(argv[1],"grep") == 0) {//grep
        //printf("we are doing grep\n");
        searchWord = argv[2];
        path = argv[3];
        grep = 1;
       // printf("grep\n");
        if (argc == 6) {
            wc = 1;
           // printf("wc\n");
        }
    }else{
        //printf("just wc\n");
        wc = 1;
        path = argv[2];
    }

    //printf("path is %s\n", path);
    FILE *fp = fopen(path, "r");
    if(fp == NULL){
        printf("file didn't open\n");
    }

    buffer = readFile(fp);
//    printf(buffer);
//    printf("\n");
    if(grep && !wc){
        //printf("grep\n");
        filter(buffer);
        printText(text, totalLines);
    }
    else if(grep && wc){
       // printf("grep and wc\n");
        filter(buffer);
        text = convertCase(text);
        words = splitText(text);
        words = nonAlphabeticFilter(words);
        frequencyCounter(words);

    } else if (wc){
        //printf("wc\n");
        text = convertCase(text);
        words = splitText(text);
        words = nonAlphabeticFilter(words);
        frequencyCounter(words);
    }
    free(buffer);
    free(words);
}

char* readFile(FILE *fp){
    buffer = 0;
    int length;
    // FILE * f = fopen (filename, "rb");

    if (fp)
    {
        fseek (fp, 0, SEEK_END);
        length = ftell(fp);
        fseek (fp, 0, SEEK_SET);
        buffer = (char*)(calloc(1,length + 1));
        //buffer = (char*)(malloc(length + 1));
        //buffer[0] = '\0';

        //memset(&buffer[0], 0, sizeof(buffer));

        if (buffer)
        {
            fread(buffer, length, 1, fp);
        }
    }
    buffer[length] = '\0';
    fclose (fp);
    //printf("%s\n", buffer);
    return buffer;

}
//https://www.daniweb.com/programming/software-development/code/216270/parsing-a-string-by-lines
int StringbyLines(char *buffer, char ***string) {
    int		*newLine;
    int		b, j, l, z = 1;
    int		lineCount, len;
    char 		*pch, **temp = NULL;
    /*
    ** Get line count
    ** Allocate memory for new line handling
    ** Check if memory allocating failed
    */
    lineCount = getLineCount(buffer);
    newLine = (int *)malloc(lineCount + sizeof(int) * sizeof(*newLine));
    if (!newLine)
        return 0;
    newLine[0] = 0;
    // Find first occurance of a new line
    pch = strchr(buffer, '\n');
    if (!pch)
        return 0;
    // If found, find all positions
    while (pch) {
        newLine[z] = pch-buffer+1;
        pch = strchr(pch+1, '\n');
        z++;
    }
    newLine[z] = (int)strlen(buffer) + 1;
    // Allocate memory to our temporary pointer
    temp = (char **)malloc(lineCount * (sizeof *temp));
    if (!temp)
        return 0;
    // Go through all lines found
    for (l = 0; l < z; l++) {
        b = 0;
        len = ((newLine[l+1]-1) + (newLine[l]) + 1);
        // Allocate memory per index
        temp[l] = (char *)malloc(len * sizeof(**temp));
        if (!temp[l])
            return 0;
        // Put our data in
        for (j = newLine[l]; j < newLine[l+1]-1; j++) {
            temp[l][b] = buffer[j];
            b++;
        }
        temp[l][b] = '\0';
    }
    // Free memory for line position
    free(newLine);
    // Set our pointer to point to char **temp
    *string = temp;
    // Return lines found
    return z;
}
int getLineCount(char *buffer) {
    int z = 1;
    char *pch;
    // Find first match
    pch = strchr(buffer, '\n');
    // Increment line count
    while (pch) {
        pch = strchr(pch+1, '\n');
        z++;
    }
    return z;
}
char** filter(char* buffer){
//make 2d array
    totalLines = StringbyLines(buffer, &text);
    if (!totalLines) {
        printf("Parsing failed.\n");
        return 0;
    }
    char *buff;
    int newTotalLines = 0;
    for(int i = 0; i < totalLines; i++) {
        buff = *(text + i);
        char *s;
        s = strstr(buff, searchWord);      // search for string "hassasin" in buff
        if (s != NULL) {                     // if successful then s now points at "hassasin"
            //printf("found\n");
            *(text + newTotalLines) = *(text + i);
            newTotalLines++;
        }
    }// index of "hassasin" in buff can be found by pointer subtraction
        //NULL the rest
        int temp = newTotalLines;
        while(newTotalLines< totalLines){
            *(text+newTotalLines) = NULL;
            newTotalLines++;
        }
        totalLines = temp;
        return text;
}
//convert text to lower case
char** convertCase(char** text){
    if(!grep)
        totalLines = StringbyLines(buffer, &text);//do if grep didn't
    for(int line = 0; line< totalLines; line++){
        int letter = 0;
        while(text[line][letter] != '\0'){
            text[line][letter] = tolower(text[line][letter]);
            letter++;
        }
        //add \0 as last letter
        text[line][letter] = '\0';
    }
    return text;
}
size_t getCharTotal(char **pString) {
    for(int i = 0; i < totalLines; i++) {
        size_t size = strlen(*(pString + i));
        charTotal += size;
        //printf("size of this line is: %d\n", size);
    }
    return charTotal;
}
char** splitText(char** text){
    words = (char**) malloc((getCharTotal(text))*sizeof(char) + 500);
    wordCount = 0;
    for(int line = 0; line < totalLines; line++){
        char *newWord = strtok(*(text+line), " ");
        while(newWord != NULL){
            words[wordCount] = newWord;
            //printf("%s\n",newWord);
            newWord = strtok(NULL, " ");
            wordCount++;
        }
    }
    return words;
}
char** nonAlphabeticFilter(char** words){
    int wordsUsed = 0;
    int i = 0;
    while( i < wordCount){
        char *maybeWord = abcWordFilter(words[i]);
//        if(strcmp(maybeWord, "") != 0){//its not empty
//            words[wordsUsed] = maybeWord;
//            wordsUsed++;
//        }
        if(strlen(maybeWord) > 0){//its not empty
            words[wordsUsed] = maybeWord;
            wordsUsed++;
        }
        i++;
    }
    //null the rest of the words
    int counter =0;
    while(wordsUsed < i){
        words[wordsUsed] = NULL;
        wordsUsed++;
        counter++;
    }
    wordCount = wordCount-counter;
    return words;
}
//https//www.programiz.com/c-programming/examples/remove-characters-string
char* abcWordFilter(char* line){
    int i,j;
    for(i = 0; line[i] != '\0'; ++i)//for a single word
    {
        while (!( (line[i] >= 'a' && line[i] <= 'z') || (line[i] >= 'A' && line[i] <= 'Z') || line[i] == '\0') )
        {
            for(j = i; line[j] != '\0'; ++j)
            {
                line[j] = line[j+1];
            }
            line[j] = '\0';
        }
    }
//    printf("Output String: ");
//    puts(line);
    return line;

}
void printText(char **stuff, int number){
    for(int i = 0; i < number; i++){
        printf(*(stuff+i));
        printf("\n");
    }

}
typedef struct wordMap{
    char *word;
    int count;
} wordMap;

struct wordMap * frequencyCounter(char** wordsz){
//check all the words in list and then add
    char* keyWord;
    int count;
    struct wordMap pairs[wordCount];
    wordSpot = 0;
    for(int i = 0; i < wordCount; i++) {
        //if its a normal word
        if (strcmp((keyWord = *(words + i)), "") != 0) {
            //printf("the keyWord is: %s\n", keyWord);
            count = 1;
            for(int j = i+1; j < wordCount; j++) {
              //  printf("checking spot %d\n", j);
                //its a match
                if (strcmp(keyWord, *(words + j)) == 0) {
                //    printf("found: %s, at spot: %d\n", keyWord, j);
                    count++;
                    *(words + j) = "";
                }
            }
            pairs[wordSpot].word =  keyWord;
            pairs[wordSpot].count =  count;
            wordSpot++;
        }
    }
    for(int i = 0; i < wordSpot; i++){
        printf(" %s, %d",pairs[i].word, pairs[i].count);
        printf("\n");
    }
    *pairz = *pairs;
    return pairz;
}



