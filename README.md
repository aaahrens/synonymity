# synonymity, all the public code
note: none of this code is running live anywhere, it is up to you to use it.

## the API
* uses sparkjava and hibernate as its ORM. 
* Generating problem sets right now is too CPU heavy
* connects to a postgres database
* uses gradle for dependancies
* dishes out the problem set in a get request, with the difficulty set in the headers  

## the scraper
* scraps dictionary.com, finds synonyms on the site based on url routing and the pages it returns
* pipelines to a connected database using sqlalchemy

## the android app

After starting the game, you have X seconds to guess the correct synonym, otherwise that is game over. If the correct synonym is guessed, then it starts over again. The final score is the number of correct synonyms.
