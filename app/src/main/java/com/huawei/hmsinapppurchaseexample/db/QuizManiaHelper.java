package com.huawei.hmsinapppurchaseexample.db;
/**
 * Name of the project QUIZ MANIA.
 * Created by Sanghati Mukherjee.
 * Huawei Technologies Co., Ltd.
 * sanghati.mukherjee@huawei.com
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import com.huawei.hmsinapppurchaseexample.model.Questions;

public class QuizManiaHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DB_NAME = "QuizMania.db";

    private static final int DB_VERSION = 2;
    private static final String TABLE_NAME = "QUIZMASTER";
    private static final String ID = "_ID";
    private static final String TOPIC = "TOPIC";
    private static final String QUESTION = "QUESTION";
    private static final String OPTIONA = "OPTIONA";
    private static final String OPTIONB = "OPTIONB";
    private static final String OPTIONC = "OPTIONC";
    private static final String OPTIOND = "OPTIOND";
    private static final String ANSWER = "ANSWER";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + TOPIC + " VARCHAR(255), " + QUESTION + " VARCHAR(255), " + OPTIONA + " VARCHAR(255), " + OPTIONB + " VARCHAR(255), " + OPTIONC + " VARCHAR(255), " + OPTIOND + " VARCHAR(255), " + ANSWER + " VARCHAR(255));";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public QuizManiaHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void listOfAllQuestion() {
        ArrayList<Questions> arraylist = new ArrayList<>();

        // General Knowledge Questions...
        arraylist.add(new Questions("gk","India has largest deposits of ____ in the world.", "Gold", "Copper", "Mica", "None of the above", "Mica"));

        arraylist.add(new Questions("gk","Who was known as Iron man of India ?", "Govind Ballabh Pant", "Jawaharlal Nehru", "Subhash Chandra Bose", "Sardar Vallabhbhai Patel", "Sardar Vallabhbhai Patel"));

        arraylist.add(new Questions("gk", "India participated in Olympics Hockey in", "1918", "1928", "1938", "1948", "1928"));

        arraylist.add(new Questions("gk","Who is the Flying Sikh of India ?", "Mohinder Singh", "Joginder Singh", "Ajit Pal Singh", "Milkha singh", "Milkha singh"));

        arraylist.add(new Questions("gk","How many times has Brazil won the World Cup Football Championship ?", "Four times", "Twice", "Five times", "Once", "Five times"));

        // Sports Questions..
        arraylist.add(new Questions("sp","Which was the 1st non Test playing country to beat India in an international match ?", "Canada", "Sri Lanka", "Zimbabwe", "East Africa", "Sri Lanka"));

        arraylist.add(new Questions("sp","Ricky Ponting is also known as what ?", "The Rickster", "Ponts", "Ponter", "Punter", "Punter"));

        arraylist.add(new Questions("sp","India won its first Olympic hockey gold in...?", "1928", "1932", "1936", "1948", "1928"));

        arraylist.add(new Questions("sp","The Asian Games were held in Delhi for the first time in...?", "1951", "1963", "1971", "1982", "1951"));

        arraylist.add(new Questions("sp","The 'Dronacharya Award' is given to...?", "Sportsmen", "Coaches", "Umpires", "Sports Editors", "Coaches"));

        // History Questions...
        arraylist.add(new Questions("his","The Battle of Plassey was fought in", "1757", "1782", "1748", "1764", "1757"));

        arraylist.add(new Questions("his","The title of 'Viceroy' was added to the office of the Governor-General of India for the first time in", "1848 AD", "1856 AD", "1858 AD", "1862 AD", "1858 AD"));

        arraylist.add(new Questions("his","Tipu sultan was the ruler of", "Hyderabad", "Madurai", "Mysore", "Vijayanagar", "Mysore"));

        arraylist.add(new Questions("his","The Vedas contain all the truth was interpreted by", "Swami Vivekananda", "Swami Dayananda", "Raja Rammohan Roy", "None of the above", "Swami Dayananda"));

        arraylist.add(new Questions("his","The Upanishads are", "A source of Hindu philosophy", "Books of ancient Hindu laws", "Books on social behavior of man", "Prayers to God", "A source of Hindu philosophy"));

        // General Science Questions...
        arraylist.add(new Questions("gs","Which of the following is a non metal that remains liquid at room temperature ?", "Phosphorous", "Bromine", "Chlorine", "Helium", "Bromine"));

        arraylist.add(new Questions("gs","Which of the following is used in pencils?", "Graphite", "Silicon", "Charcoal", "Phosphorous", "Graphite"));

        arraylist.add(new Questions("gs","The gas usually filled in the electric bulb is", "Nitrogen", "Hydrogen", "Carbon Dioxide", "Oxygen", "Nitrogen"));

        arraylist.add(new Questions("gs","Which of the gas is not known as green house gas ?", "Methane", "Nitrous oxide", "Carbon dioxide", "Hydrogen", "Hydrogen"));

        arraylist.add(new Questions("gs","The hardest substance available on earth is", "Gold", "Iron", "Diamond", "Platinum", "Diamond"));

        this.insertAllQuestions(arraylist);

    }


    private void insertAllQuestions(ArrayList<Questions> allQuestions) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (Questions question : allQuestions) {
                values.put(TOPIC, question.getTopic());
                values.put(QUESTION, question.getQuestion());
                values.put(OPTIONA, question.getOptionA());
                values.put(OPTIONB, question.getOptionB());
                values.put(OPTIONC, question.getOptionC());
                values.put(OPTIOND, question.getOptionD());
                values.put(ANSWER, question.getAnswer());
                db.insert(TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }


    public List<Questions> getAllListOfQuestions(String topicName) {

        List<Questions> questionsList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String coloumn[] = {ID, TOPIC, QUESTION, OPTIONA, OPTIONB, OPTIONC, OPTIOND, ANSWER};
        String selection = "TOPIC =?";
        String[] selectionArgs = {topicName};

       Cursor cursor = db.query(TABLE_NAME, coloumn, selection, selectionArgs, null, null, null);
        //Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " where TOPIC = '" + topicName + "'" , null);

        while (cursor.moveToNext()) {
            Questions question = new Questions();
            question.setId(cursor.getInt(0));
            question.setTopic(cursor.getString(1));
            question.setQuestion(cursor.getString(2));
            question.setOptionA(cursor.getString(3));
            question.setOptionB(cursor.getString(4));
            question.setOptionC(cursor.getString(5));
            question.setOptionD(cursor.getString(6));
            question.setAnswer(cursor.getString(7));
            questionsList.add(question);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        db.close();
        return questionsList;
    }
}