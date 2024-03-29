package ca.ubc.cs.cpsc210.quiz.activity;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by pcarter on 14-11-06.
 *
 * A score tracker having a list of at most COUNT of the most recent scores,
 * the minimum number of attempts taken to guess location of restaurant among those scores
 * and the highest number of points earned among those scores.
 *
 * NOTE TO CPSC 210 STUDENTS: applies the Singleton Pattern
 */
public class ScoreTracker {
    private static final String LOG_TAG = "ScoreTracker";
    private static final int COUNT = 5;
    private static ScoreTracker instance;
    private ScoreSerializer serializer;
    private List<Score> scores;
    private int minAttempts;
    private int highPointsEarned;

    private int minAttemptInCurrentList;
    private int HighetPointInCurrentList;


    /**
     * Constructor initializes list of scores by reading them from file.
     * Creates empty list of scores if data cannot be read from file.
     *
     * @param c   the application context
     *
     * NOTE TO CPSC 210 STUDENTS: do not modify this method
     */
    private ScoreTracker(Context c) {
        serializer = new ScoreSerializer(c);
        minAttempts = Integer.MAX_VALUE;
        highPointsEarned = Integer.MIN_VALUE;

        try {
            scores = serializer.readScores();
            if (scores.size() > 0) {
                minAttempts = findMinAttemptsTaken();
                highPointsEarned = findHighestPointsEarned();
            }
        } catch (Exception e) {
            scores = new ArrayList<Score>();
            Log.e(LOG_TAG, "Error loading scores");
        }
    }

    /**
     * Get the instance of ScoreTracker.
     *
     * @param c  the application context
     * @return  the score tracker
     *
     * NOTE TO CPSC 210 STUDENTS: do not modify this method
     */
    public static ScoreTracker getInstance(Context c) {
        if (instance == null)
            instance = new ScoreTracker(c);
        return instance;
    }

    /**
     * Add score to list, keeping only the most recent COUNT scores added;
     * update min attempts and max points.
     *
     * @param s  score to add to list
     */
    public void addScore(Score s) {


        if(s.numAttempts<findMinAttemptsTaken()){
            minAttempts=s.numAttempts;
        }
        if(s.pointsEarned>findHighestPointsEarned()){
            highPointsEarned=s.pointsEarned;
        }
        if(scores.size()<COUNT){
            scores.add(s);
        }
         if(scores.size()>=COUNT) {
            scores.remove(0);
            scores.add(s);
        }



    }

    /**
     * Get smallest number of attempts taken to guess location of restaurant among scores in current list
     *
     * @return  smallest number of attempts taken
     */
    public int getMinAttemptsTaken() {

        return findMinAttemptsTaken();
    }

    /**
     * Get highest number of points earned among scores in current list
     *
     * @return  highest number of points earned
     */
    public int getHighPointsEarned() {

        return findHighestPointsEarned();
    }

    /**
     * Get list of most recent COUNT scores added, ordered from most recent (at the front of the list)
     * to oldest (at the end of the list)
     *
     * @return  ordered list of scores, from most recent to oldest
     */
    public List<Score> getScores() {

        List<Score> reversescores=new ArrayList<Score>(scores);
        Collections.reverse(reversescores);
         return reversescores;// stub
    }

    /**
     * Determine the highest number of points earned among scores in current list.
     * Requires: list is not empty
     *
     * @return highest number of points earned
     */
    private int findHighestPointsEarned() {
        if (scores.size()==0){
            return Integer.MAX_VALUE;
        }
       HighetPointInCurrentList=scores.get(0).getPointsEarned();
        for (int i=1;i<scores.size();i++){
            if(HighetPointInCurrentList<scores.get(i).getPointsEarned()){
                HighetPointInCurrentList=scores.get(i).getPointsEarned();
            }
        }
        return HighetPointInCurrentList;
           // stub
    }

    /**
     * Determine the smallest number of attempts taken to guess location of restaurant among scores in current list.
     * Requires: list is not empty
     *
     * @return smallest number of attempts taken
     */
    private int findMinAttemptsTaken() {
        if (scores.size()==0){
            return Integer.MAX_VALUE;
        }

        minAttemptInCurrentList=scores.get(0).getNumAttempts();
        for (int i=1;i<scores.size();i++){
            if(minAttemptInCurrentList>scores.get(i).getNumAttempts()){
                minAttemptInCurrentList=scores.get(i).getNumAttempts();
            }
        }
        return minAttemptInCurrentList;  // stub
    }

    /**
     * Write scores to file
     *
     * @return true if successful, false otherwise
     *
     * NOTE TO CPSC 210 STUDENTS: do not modify this method
     */
    public boolean saveScores() {
        try {
            serializer.writeScores(scores);
            Log.i(LOG_TAG, "Scores written to file");
            return true;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error saving scores to file");
            return false;
        }
    }
}
