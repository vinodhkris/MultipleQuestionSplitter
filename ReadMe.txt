What does this code do?

This project aims at splitting multiple questions. It has been written in Java, using the Stanford Parser. The stanford Parser package used has been attached with the project. It is the "englishPCFG.ser.gz" which can be downloaded and installed from the stanford NLP website. It is the english Stanford Parser. 

What are the files in this project ?

The code you need to run is Doubleq.java. The other 2 files are PreProcess.java and Dijikstraalgo.java . PreProcess.java is used to PreProcess the question entered in terms of removing the stop words (the stopwords list is also attached with this project). The Dijikstaalgo.java is used for getting the shortest path between the nodes in the graph, the nodes being words of the question. This is essential for the algorithm to identify what are the 2 component questions.


How do I run it ?

Since it is a java project you obviously need jdk and jre to run it. Eclipse was used in the building of this project, so its better if you use eclipse. If you see the code you will see that the project requires 2 files, englishPCFG.ser.gz and stopwordlist.txt, which is basically required for parsing and pre processing of the question. These are also attached with the project. 

What are the inputs needed?

There are 2 inputs needed. 

1. What is the question ?

You can either input a question or just press enter in which case the default question is taken. The default question is ""Where did the first president die?"
You can change it in the code.

2. What answer type is required?

This basically expects you to answer in terms of what you expect the first question to be. For example in the default case : Where did the first president of India die? , The first question you would expect to be given out would be : Who is the first president of India? , so 'who' would be the appropriate input in this case. This is so that the questions are properly framed in the output. I am working on the system identifying the question answer type itself, wil be out on the next release. 

The input to this question therefore, should always be one of the question words(who, where, why, etc). Otherwise the questions would not be properly framed in the output. But the subject of the questions nevertheless are always properly identified. 

What is the output like

The output gives you the two questions. As I said it also depends on the input you gave to the required answer type. It also gives the time elapsed. 

Also attached is some of the questions, it is known to give the right output to. Please try out more and let me know if it fails for any!! I was not able to find a dataset to properly test out this. 

Thats all I can think of. Go through the code to know more. Mail me at krishnan.vinodh@gmail.com if you have any doubts. 

Have Fun!!