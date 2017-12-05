package com;
/**
 * Created by drunkengranite on 5/17/17.
 */

import com.Entities.Problem;
import com.Entities.Word;
import com.google.gson.annotations.Expose;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Database
{
    private static EntityManagerFactory factory;
    private BigInteger MAX_SIZE;
    private int wordsSize;
    private Random RNG;
    private List<Problem> allProblems;
    private List<String> allWords;
    public Boolean ready;
    public String statusMessage;

    public Database()
    {
        this.ready = false;
        this.statusMessage = "Initialized";
    }

    public void init()
    {
        this.RNG = new Random();
        this.statusMessage = "Connecting";
        factory = Persistence.createEntityManagerFactory("database");
        EntityManager manager = factory.createEntityManager();
    //        get the max size for the RNG generator to get problems
        Query countQuery = manager.createNativeQuery("select count(*) from problems");
        this.MAX_SIZE = (BigInteger) countQuery.getSingleResult();


       this.statusMessage = "connected, initializing problem cache";
//        create the cache of all problems
        CriteriaQuery<Problem> criteria = manager.getCriteriaBuilder().createQuery(Problem.class);
        criteria.select(criteria.from(Problem.class));
        this.allProblems = manager.createQuery(criteria).getResultList();

        System.out.println("initiializing words cache");
//         create the cache of all words
        CriteriaQuery<Word> wordCriteria = manager.getCriteriaBuilder().createQuery(Word.class);
        wordCriteria.select(wordCriteria.from(Word.class));
        List<Word> wordList = manager.createQuery(wordCriteria).getResultList();
        this.allWords = wordList.parallelStream().map(Word::getWord).distinct().collect(Collectors.toList());
        this.wordsSize = allWords.size();
        this.ready = true;
//
//        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl("jdbc:mysql://localhost:3306/simpsons");
//        config.setUsername("bart");
//        config.setPassword("51mp50n");
//        config.addDataSourceProperty("cachePrepStmts", "true");
//        config.addDataSourceProperty("prepStmtCacheSize", "250");
//        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
//
//        HikariDataSource ds = new HikariDataSource(config);
    }


    public void putLeaderBoard()
    {

    }

    public void getLeaderBoard()
    {

    }

    private int getRandom()
    {
        return this.RNG.nextInt(this.MAX_SIZE.intValue() - 1);
    }

    public ArrayList<Tuple> getProblemSet()
    {
        ArrayList<Tuple> toReturn = new ArrayList<>();
        int counter = 0;
        do
        {
            Random rng = new Random();
            Problem randomProblem = allProblems.get(getRandom());

            List<String> optionsList = rng.
                    ints(0, wordsSize)
                    .limit(45)
                    .parallel()
                    .mapToObj(i -> allWords.get(i))
                    .distinct()
                    .filter((randomWord) -> randomProblem.getWords()
                            .parallelStream()
                            .noneMatch((storedWord -> storedWord.getWord().equals(randomWord))))
                    .collect(Collectors.toList());
            toReturn.add(new Tuple(new ProblemWrapper(randomProblem), optionsList));
            counter++;
        } while (counter < 30);

        return toReturn;
    }

    class Tuple{
        @Expose
        private List<String> options;
        @Expose
        private ProblemWrapper problem;


        Tuple(ProblemWrapper problem, List<String> options){
            this.problem= problem;
            this.options = options;
        }

        public List<String> getOptions()
        {
            return options;
        }

        public void setOptions(List<String> options)
        {
            this.options = options;
        }

        public ProblemWrapper getProblem()
        {
            return problem;
        }

        public void setProblem(ProblemWrapper problem)
        {
            this.problem = problem;
        }
    }


    class ProblemWrapper{
        @Expose
        private String correctAnswer;
        @Expose
        private String word;

        public ProblemWrapper(Problem problem){
            this.word = problem.getWord();
            Random random = new Random();
            this.correctAnswer = problem.getWords().get(random.nextInt(problem.getWords().size())).getWord();
        }

        public String getCorrectAnswer()
        {
            return correctAnswer;
        }

        public void setCorrectAnswer(String correctAnswer)
        {
            this.correctAnswer = correctAnswer;
        }

        public String getWord()
        {
            return word;
        }

        public void setWord(String word)
        {
            this.word = word;
        }

    }

}
