import nltk
import random

#nltk.download('words')

word_list = nltk.corpus.words.words()

nroDoc=10

def generate_random_words(N):
    random_words = random.choices(word_list, k=N)
    return random_words

def save_to_file(filename):
    N = 1000
    for i in range(nroDoc):
        rangeI=random.randrange(nroDoc);
        links=[]
        with open("../invertIndex/InvertedIndex_input/file"+str(i), 'w') as file:
            #words = generate_random_words(N)
            for j in range(2000):
                file.write(str(i)+' _ ')
                words = generate_random_words(N)
                if(j==2000/2):
                    for k in range(rangeI):
                       randomDoc=random.randrange(nroDoc)
                       if randomDoc not in links:
                        links.append(randomDoc)
                        file.write(str(randomDoc) + ' ') 
                for word in words:
                    file.write(word + ' ')
                file.write('\n')

if __name__ == "__main__":
    save_to_file('../invertIndex/Invertedindex_input/random_words.txt1')
    
