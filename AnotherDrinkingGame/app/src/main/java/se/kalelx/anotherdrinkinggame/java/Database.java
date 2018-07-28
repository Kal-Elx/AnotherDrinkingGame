package se.kalelx.anotherdrinkinggame.java;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static se.kalelx.anotherdrinkinggame.java.QuestionType.*;

public class Database {

    private static Database sDatabase;
    private final Context mContext;

    private List<Player> mPlayers;
    private final List<Player> mFemales;
    private final List<Player> mMales;
    private List<Couple> mCouples;
    private List<Question> mNeverHaveIEverQuestestions = new ArrayList<>();
    private List<Question> mPointToQuestions = new ArrayList<>();
    private List<Question> mBackToBackFemaleQuestions = new ArrayList<>();
    private List<Question> mBackToBackMaleQuestions = new ArrayList<>();
    private List<Question> mBackToBackCoupleQuestions = new ArrayList<>();
    private List<Question> mBackToBackGeneralQuestions = new ArrayList<>();
    private List<Question> mRymes = new ArrayList<>();
    private List<Question> mCategories = new ArrayList<>();
    private List<Question> mMissionFemale = new ArrayList<>();
    private List<Question> mMissionMale = new ArrayList<>();
    private List<Question> mMissionCouple = new ArrayList<>();
    private List<Question> mMissionGeneral = new ArrayList<>();
    private List<Question> mPreviousMissions = new ArrayList<>();
    private int mNumberOfFemales;
    private int mNumberOfMales;
    private final Random mRandomGenerator = new Random();

    private boolean userWantsNHIE = true;
    private boolean userWantsPG = true;
    private boolean userWantsB2B = true;
    private boolean userWantsMISSION = true;
    private boolean userWantsCATEGORY = true;
    private boolean userWantsRYME = true;

    public static Database get(Context context) {
        if (sDatabase == null) {
            sDatabase = new Database(context);
        }

        return sDatabase;
    }

    private Database(Context context) {
        mContext = context.getApplicationContext();
        mPlayers = new ArrayList<>();
        mCouples = new ArrayList<>();
        mFemales= new ArrayList<>();
        mMales = new ArrayList<>();
    }

    public void reset() {
        sDatabase = null;
    }

    public void createQuestions() {
        getInformationAboutPlayers();

        if (userWantsNHIE) {
            createNeverHaveIEverQuestions();
        }
        if (userWantsPG) {
            createPointToQuestions();
        }
        if (userWantsB2B) {
            createBackToBackQuestions();
        }
        if (userWantsRYME) {
            createRymes();
        }
        if (userWantsCATEGORY) {
            createCategories();
        }
        addMissions();
        countQuestions();
    }

    private void addMissions() {
        if (userWantsMISSION) {
            createMissions();
            if (mNumberOfFemales >= 1) {
                createMissionsWithFemalesForAll();
            }
            if (mNumberOfMales >= 1) {
                createMissionsWithMalesForAll();
            }
            if (mNumberOfFemales >= 1 && mNumberOfMales >= 1) {
                createMissionsWithFemalesForMales();
                createMissionsWithMalesForFemales();
            }
            if (mNumberOfFemales >= 2) {
                createMissionsWithFemalesForFemales();
            }
            if (mNumberOfMales >= 2) {
                createMissionsWithMalesForMales();
            }
        }
    }

    private void countQuestions() {
        System.out.println("Jag har aldrig: " + mNeverHaveIEverQuestestions.size());
        System.out.println("Pekleken: " + mPointToQuestions.size());
        System.out.println("Rygg mot rygg: " + (mBackToBackCoupleQuestions.size() + mBackToBackGeneralQuestions.size() + mBackToBackFemaleQuestions.size() + mBackToBackMaleQuestions.size()));
        System.out.println("Uppdrag: " + (mMissionGeneral.size() + mMissionCouple.size() + mMissionFemale.size() + mMissionMale.size()));
        System.out.println("Rim: " + mRymes.size());
        System.out.println("Kategori: " + mCategories.size());
    }

    private void getInformationAboutPlayers() {
        mFemales.clear();
        mMales.clear();
        for (Player player : mPlayers) {
            if (player.getGender() == Gender.FEMALE) {
                mFemales.add(player);
            } else if (player.getGender() == Gender.MALE) {
                mMales.add(player);
            }
        }
        mNumberOfFemales = mFemales.size();
        mNumberOfMales = mMales.size();
    }

    public Question getQuestionFor(Player player) {
        List<Question> questions = new ArrayList<>();
        questions.addAll(mNeverHaveIEverQuestestions);
        questions.addAll(mPointToQuestions);
        questions.addAll(mBackToBackGeneralQuestions);
        questions.addAll(mRymes);
        questions.addAll(mCategories);
        questions.addAll(mMissionGeneral);

        if (player.getGender() == Gender.FEMALE) {
            questions.addAll(mMissionFemale);
            if (mNumberOfFemales >= 2) {
                questions.addAll(mBackToBackFemaleQuestions);
            }
        } else if (player.getGender() == Gender.MALE) {
            questions.addAll(mMissionMale);
            if (mNumberOfMales >= 2) {
                questions.addAll(mBackToBackMaleQuestions);
            }
        }
        if (!player.isSingle()) {
            questions.addAll(mBackToBackCoupleQuestions);
            questions.addAll(mMissionCouple);
        }

        if (questions.size() > 0) {
            int index = mRandomGenerator.nextInt(questions.size());
            Question question = questions.get(index);
            remove(question);
            if (isMission(question)) {
                mPreviousMissions.add(question);
            }
            return questions.get(index);
        } else {
            // Reset all questions
            mPreviousMissions.clear();
            createQuestions();
            return getQuestionFor(player);
        }
    }

    private boolean isMission(Question question) {
        QuestionType qt = question.getType();
        return qt == MISSION_GENERAL || qt == MISSION_MALE || qt == MISSION_FEMALE || qt == MISSION_COUPLE;
    }

    private void remove(Question q) {
        switch (q.getType()) {
            case NHIE:
                mNeverHaveIEverQuestestions.remove(q);
                break;
            case PG:
                mPointToQuestions.remove(q);
                break;
            case B2B_GENERAL:
                mBackToBackGeneralQuestions.remove(q);
                break;
            case B2B_COUPLE:
                mBackToBackCoupleQuestions.remove(q);
                break;
            case B2B_FEMALE:
                mBackToBackFemaleQuestions.remove(q);
                break;
            case B2B_MALE:
                mBackToBackMaleQuestions.remove(q);
                break;
            case RYME:
                mRymes.remove(q);
                break;
            case CATEGORY:
                mCategories.remove(q);
                break;
            case MISSION_COUPLE:
                mMissionCouple.remove(q);
                break;
            case MISSION_GENERAL:
                mMissionGeneral.remove(q);
                break;
            case MISSION_FEMALE:
                mMissionFemale.remove(q);
                break;
            case MISSION_MALE:
                mMissionMale.remove(q);
                break;
        }
    }

    public String getPlayerToQuestion(String question, String playerName) {
        switch (question.charAt(question.indexOf("*") + 1)) {
            case 'A':
                return question.replace("*A*", getRandomPlayerFromList(playerName, mPlayers));
            case 'P':
                return question.replace("*P*", getPartnerFor(getPlayer(playerName)).getName());
            case 'M':
                return question.replace("*M*", getRandomPlayerFromList(playerName, mMales));
            case 'F':
                return question.replace("*F*", getRandomPlayerFromList(playerName, mFemales));
            default:
                return question;
        }
    }

    public Player getPartnerFor(Player player) {
        List<Player> possibleTeammates = new ArrayList<>();
        for (Couple couple : mCouples) {
            if (couple.playerInCouple(player)) {
                possibleTeammates.add(couple.getPartnerFor(player));
            }
        }
        if (possibleTeammates.size() > 1) {
            return possibleTeammates.get(mRandomGenerator.nextInt(possibleTeammates.size()));
        }
        return possibleTeammates.get(0);
    }

    /*
    Returns the name of a random player that isn't the given name.
     */
    private String getRandomPlayerFromList(String name, List<Player> list) {
        List<Player> duplicatedList = new ArrayList<>(list);
        duplicatedList.remove(getPlayer(name));
        int index = mRandomGenerator.nextInt(duplicatedList.size());
        return duplicatedList.get(index).getName();
    }

    /*
    Removes couples with players that have been removed.
    Used when players are edited during game.
     */
    public void cleanCouples() {
        List<Couple> notValidCouples = new ArrayList<>();
        for (Couple c : mCouples) {
            if (!mPlayers.contains(c.getPlayer1()) || !mPlayers.contains(c.getPlayer2())) {
                notValidCouples.add(c);
            }
        }
        mCouples.removeAll(notValidCouples);
    }

    public void clearQuestions() {
        mNeverHaveIEverQuestestions.clear();
        mPointToQuestions.clear();
        mBackToBackFemaleQuestions.clear();
        mBackToBackMaleQuestions.clear();
        mBackToBackCoupleQuestions.clear();
        mBackToBackGeneralQuestions.clear();
        mRymes.clear();
        mCategories.clear();
        mMissionFemale.clear();
        mMissionMale.clear();
        mMissionCouple.clear();
        mMissionGeneral.clear();
    }

    public List<Player> getPlayers() {
        return mPlayers;
    }

    public void allPlayersAdded() {
        for (Player player : mPlayers) {
            if (player.getGender() == Gender.FEMALE) {
                mFemales.add(player);
            } else if (player.getGender() == Gender.MALE) {
                mMales.add(player);
            }
        }
    }

    public void playersEdited() {
        getInformationAboutPlayers();
        addMissions();
        for (Question q : mPreviousMissions) {
            switch (q.getType()) {
                case MISSION_GENERAL:
                    mMissionGeneral.remove(q);
                    break;
                case MISSION_COUPLE:
                    mMissionMale.remove(q);
                    break;
                case MISSION_FEMALE:
                    mMissionFemale.remove(q);
                    break;
                case MISSION_MALE:
                    mMissionMale.remove(q);
                    break;
            }
        }
    }

    private Player getPlayer(String name) {
        for (Player player : mPlayers) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    public List<Player> getFemales() {
        return mFemales;
    }

    public List<Player> getMales() {
        return mMales;
    }

    public List<Couple> getCouples() {
        return mCouples;
    }

    public void setCouples(List<Couple> couples) {
        mCouples = couples;
    }

    public boolean getSwitchStatusForNHIE() {
        return userWantsNHIE;
    }

    public void setSwitchStatusForNHIE(boolean userWantsNHIE) {
        this.userWantsNHIE = userWantsNHIE;
    }

    public boolean getSwitchStatusForPG() {
        return userWantsPG;
    }

    public void setSwitchStatusForPG(boolean userWantsPG) {
        this.userWantsPG = userWantsPG;
    }

    public boolean getSwitchStatusForB2B() {
        return userWantsB2B;
    }

    public void setSwitchStatusForB2B(boolean userWantsB2B) {
        this.userWantsB2B = userWantsB2B;
    }

    public boolean getSwitchStatusForMISSION() {
        return userWantsMISSION;
    }

    public void setSwitchStatusForMISSION(boolean userWantsMISSION) {
        this.userWantsMISSION = userWantsMISSION;
    }

    public boolean getSwitchStatusForCATEGORY() {
        return userWantsCATEGORY;
    }

    public void setSwitchStatusForCATEGORY(boolean userWantsCATEGORY) {
        this.userWantsCATEGORY = userWantsCATEGORY;
    }

    public boolean getSwitchStatusForRYME() {
        return userWantsRYME;
    }

    public void setSwitchStatusForRYME(boolean userWantsRYME) {
        this.userWantsRYME = userWantsRYME;
    }

    private void add(String question, QuestionType type) {
        Question q = new Question(question, type);
        switch (type) {
            case NHIE:
                mNeverHaveIEverQuestestions.add(q);
                break;
            case PG:
                mPointToQuestions.add(q);
                break;
            case B2B_GENERAL:
                mBackToBackGeneralQuestions.add(q);
                break;
            case B2B_COUPLE:
                mBackToBackCoupleQuestions.add(q);
                break;
            case B2B_FEMALE:
                mBackToBackFemaleQuestions.add(q);
                break;
            case B2B_MALE:
                mBackToBackMaleQuestions.add(q);
                break;
            case RYME:
                mRymes.add(q);
                break;
            case CATEGORY:
                mCategories.add(q);
                break;
            case MISSION_COUPLE:
                mMissionCouple.add(q);
                break;
            case MISSION_GENERAL:
                mMissionGeneral.add(q);
                break;
            case MISSION_FEMALE:
                mMissionFemale.add(q);
                break;
            case MISSION_MALE:
                mMissionMale.add(q);
                break;
        }
    }

    /*
    Grammar: Don't start sentence with capital letter, finnish sentence with dot.
     */
    private void createNeverHaveIEverQuestions() {
        mNeverHaveIEverQuestestions = new ArrayList<>();
        QuestionType t = NHIE;
        add("låtsas tycka om vin för att framstå som sofistikerad.", t);
        add("snott toapapper för det tagit slut hemma.", t);
        add("blivit utslängd från en nattklubb.", t);
        add("sjukanmält mig för jag varit bakfull.", t);
        add("åkt polisbil.", t);
        add("haft sex i min kompis säng.", t);
        add("flirtat med min kompis syskon.", t);
        add("hamnat i fyllecell.", t);
        add("haft sex utomlands.", t);
        add("spelat Habbo Hotel.", t);
        add("flirtat mig till en drink.", t);
        add("tagit en springnota.", t);
        add("sagt fel namn till min partner.", t);
        add("varit med på tv.", t);
        add("jobbat på en snabbmatskedja.", t);
        add("kallat mig själv en belieber.", t);
        add("varit på en tinderdejt.", t);
        add("haft på mig handbojor.", t);
        add("fastnat med tungan på en kall lyktstolpe.", t);
        add("varit statist i en film.", t);
        add("sjungit karaoke.", t);
        add("blivit nöjd med ett skolfoto.", t);
        add("sjungit på en scen.", t);
        add("fejkat en lapp från mina föräldrar för att slippa idrotten i skolan.", t);
        add("ljugit för min partner om var jag är.", t);
        add("skadat mina föräldrars bil.", t);
        add("gått in på fel toalett.", t);
        add("haft sönder en möbel när jag hade sex på den.", t);
        add("skadat mig under sex.", t);
        add("använt någon annans tandborste.", t);
        add("vänt ut och in på mina underkläder för att jag inte hade några rena.", t);
        add("köpt ett Happy Meal.", t);
        add("deltagit i en matätartävling.", t);
        add("använt någon annans legitimation.", t);
        add("varit i ett slagsmål.", t);
        add("ringt 112.", t);
        add("letat efter sexleksaker i mina föräldrars sovrum.", t);
        add("kommit med i 10 000-metersklubben.", t);
        add("misslyckats med att komma in på klubben för jag varit för full.", t);
        add("pratat skit om någon i det här rummet.", t);
        add("snapat med en främling.", t);
        add("haft en KK.", t);
        add("sett någon i det här rummet naken.", t);
        add("haft sex utomhus.", t);
        add("flirtat med någon jag inte varit intresserad av för att få personen att göra något för mig.", t);
        add("gått till en fest jag inte varit bjuden till.", t);
        add("laddat ner något olagligt.", t);
        add("testat droger.", t);
        add("smugit in på krogen när jag varit minderårig.", t);
        add("legat med någon som min kompis legat med innan.", t);
        add("snott alkohol av mina föräldrar.", t);
        add("haft sex i en bil.", t);
        add("blivit påkommen när jag haft sex.", t);
        add("gått på strippklubb.", t);
        add("hånglat med flera personer samma kväll.", t);
        add("fått sparken.", t);
        add("varit kär.", t);
        add("spräckt skärmen på min mobiltelefon.", t);
        add("spelat in en podcast.", t);
        add("druckit alkohol innan 12:00.", t);
        add("druckit alkohol för att slippa köra en kompis.", t);
        add("glömt bort mitt lösenord.", t);
        add("haft en trekant.", t);
        add("onanerat utomhus.", t);
        add("dejtat en kollega.", t);
        add("fått blommor av min partner.", t);
        add("använt en sexleksak.", t);
        add("dejtat flera personer samtidigt.", t);
        add("loggat in på ett ex konto på något socialt medie.", t);
        add("ljugit för min chef.", t);
        add("gett eller fått ett sugmärke.", t);
        add("blivit full ensam.", t);
        add("sökt jobb på Systembolaget.", t);
        add("försökt fånga ett djur på fyllan.", t);
        add("varit på vinprovning.", t);
        add("onanerat till en bild av någon i det här rummet.", t);
        add("haft en crush på en lärare.", t);
        add("använt någon annans tandborste.", t);
        add("haft en pregnancy scare.", t);
        add("fejkat en orgasm.", t);
        add("spelat strippoker.", t);
        add("druckit fulvin.", t);
        //add(".", t);
    }

    /*
    Grammar: Start sentence with capital letter, finnish sentence with question mark.
     */
    private void createPointToQuestions() {
        mPointToQuestions = new ArrayList<>();
        QuestionType t = PG;
        add("Vem kommer bli rikast?", t);
        add("Vem kommer få barn först?", t);
        add("Vem är fullast?", t);
        add("Vem dansar bäst?", t);
        add("Vem skulle ersätta alla sina vänner för 10 miljoner?", t);
        add("Vem kommer troligast hooka med personen till höger om den ikväll?", t);
        add("Vem borde inte ha fått körkort?", t);
        add("Vilka kunde inte ladda ner den här appen för de hade en iPhone?\n(Alla som det stämmer in på dricker)", t);
        add("Vem kommer troligast gifta sig med en kändis?", t);
        add("Vem skulle dö först på en öde ö?", t);
        add("Vem skulle klara sig bäst som ståuppkomiker?", t);
        add("Vem har gått längst utan att duscha?", t);
        add("Vem skulle troligast söka till en dokusåpa?", t);
        add("Vem har spelat bort mest pengar?", t);
        add("Vem har mest hybris?", t);
        add("Vem är sämst på engelska?", t);
        add("Vem är mest lättlurad?", t);
        add("Vem vill du se naken?", t);
        add("Vem är nördigast?", t);
        add("Vem kommer troligast aldrig mer läsa en bok?", t);
        add("Vem skulle vara bäst som influencer?", t);
        add("Vem är pinsammast på sociala medier?", t);
        add("Vem är klumpigast?", t);
        add("Vem är största huliganen?", t);
        add("Vem är högljuddast?", t);
        add("Vem är aktivast på sociala medier?", t);
        add("Vem hanterar alkohol sämst?", t);
        add("Vem dricker mest?", t);
        add("Vem har tråkigast jobb/utbildning?", t);
        add("Vem spenderar mest tid vid datorn?", t);
        add("Vem har hållt på med sin mobil mest under kvällen?", t);
        add("Vem kommer få till det ikväll?", t);
        add("Vem har bäst sångröst?", t);
        add("Vem har minst sex?", t);
        add("Vem ljuger mest?", t);
        add("Vem skulle du inte låna ut din bil till?", t);
        add("Vem skvallrar mest?", t);
        add("Vem är största toffeln?", t);
        add("Vem har dyrast dricka ikväll?", t);
        add("Vem är mest skrockfull?", t);
        add("Vem har skickat flest nakenbilder i sitt liv?", t);
        add("Vem äger flest sexleksaker?", t);
        add("Vem bryr sig mest om streaken på Snapchat?", t);
        add("Vem försöker oftast briljera med sina kunskaper om alkohol?", t);
        add("Vem spyr först ikväll?", t);
        add("Vem är lättast att få i säng?", t);
        add("Vem har skevast politiska åtsikter?", t);
        add("Vem löper störst risk att bli hemlös?", t);
        add("Vem särskriver mest?", t);
        add("Vem är mest besserwisser?", t);
        add("Vem skulle med störst sannolikhet ligga med sin lärare/chef?", t);
        add("Vem fjäskar mest?", t);
        add("Vem lyssnar på konstigast musik?", t);
        add("Vem behöver klippa sig?", t);
        add("Vem åker troligast på campingsemester varje sommar?", t);
        add("Vem skryter mest om tidigare ligg?", t);
        add("Vem har sämst lokalsinne?", t);
        add("Vem är mest glömsk?", t);
        add("Vem gör minst på sitt jobb?", t);
        add("Vem är största playern?", t);
        add("Vem skulle kunna göra en plastikoperation?", t);
        add("Vem har snyggast tatuering?", t);
        add("Vem har haft den värsta karatefyllan?", t);
        add("Vem skulle lyckas bäst som politiker?", t);
        add("Vem är sämst på geografi?", t);
        //add("Vem ?", t);
    }

    /*
    Grammar: Start sentence with capital letter, finnish sentence with question mark.
     */
    private void createBackToBackQuestions() {
        mBackToBackFemaleQuestions = new ArrayList<>();
        mBackToBackMaleQuestions = new ArrayList<>();
        mBackToBackCoupleQuestions = new ArrayList<>();
        mBackToBackGeneralQuestions = new ArrayList<>();
        add("Vem är bäst på FIFA?", B2B_MALE);
        add("Vem bjuder mest på sig själv?", B2B_GENERAL);
        add("Vem har bäst klädstil?", B2B_GENERAL);
        add("Vem snyltar mest på sin partner?", B2B_COUPLE);
        add("Vem lägger mest pengar på kläder?", B2B_GENERAL);
        add("Vem är snyggast ikväll?", B2B_GENERAL);
        add("Vem dansar bäst?", B2B_GENERAL);
        add("Vem raggar bäst?", B2B_GENERAL);
        add("Vem är mest desperat?", B2B_GENERAL);
        add("Vem blir lättast avundsjuk?", B2B_COUPLE);
        add("Vem är mest romantisk?", B2B_COUPLE);
        add("Vem är sämst på att laga mat?", B2B_COUPLE);
        add("Vem är bäst i sängen?", B2B_COUPLE);
        add("Vem har snyggast mamma?", B2B_GENERAL);
        add("Vem tar längst tid på sig för att fixa sig?", B2B_FEMALE);
        add("Vem är mest vältränad?", B2B_GENERAL);
        add("Vem tittar på mest porr?", B2B_GENERAL);
        add("Vem är största douchen?", B2B_MALE);
        add("Vem är största svärmorsdrömmen?", B2B_MALE);
        add("Vem tofflar mest?", B2B_COUPLE);
        add("Vem har legat med flest?", B2B_GENERAL);
        add("Vem tycker mest om melodifestivalen?", B2B_GENERAL);
        add("Vem är mest attraherad av den andra?", B2B_GENERAL);
        add("Vem är mest kinky?", B2B_GENERAL);
        add("Vem är bäst i skolan?", B2B_GENERAL);
        add("Vem är snålast?", B2B_GENERAL);
        add("Vem är mest vuxen?", B2B_GENERAL);
        add("Vem är mest fåfäng?", B2B_GENERAL);
        add("Vem har bäst musiksmak?", B2B_GENERAL);
        add("Vem har bäst flöde på instagram?", B2B_FEMALE);
        add("Vem har bäst skäggväxt?", B2B_MALE);
        add("Vem kommer bäst klara att föda ett barn?", B2B_FEMALE);
        add("Vem är mest beroende av sin mobil?", B2B_GENERAL);
        add("Vem skulle tjäna mest som prostituerad?", B2B_GENERAL);
        add("Vem har brutit mest lagar i sitt liv?", B2B_GENERAL);
        add("Vem har bäst garderob?", B2B_FEMALE);
        add("Vem bestämmer mest över vad den andra har på sig?", B2B_COUPLE);
        add("Vem har mest sex?", B2B_GENERAL);
        add("Vem onanerar mest?", B2B_GENERAL);
        add("Vem är bäst på dirty talk?", B2B_COUPLE);
        add("Vem har äckligast fötter?", B2B_COUPLE);
        add("Vem är mest kräsen?", B2B_GENERAL);
        add("Vem tindrar mest?", B2B_GENERAL);
        add("Vem har äckligast morgonandedräkt?", B2B_COUPLE);
        add("Vem har bäst inredningssmak?", B2B_COUPLE);
        add("Vem har störst \"fötter\"?", B2B_MALE);
        //add("", B2B_);
    }

    /*
    Grammar: Start with capital letter, no dot.
     */
    private void createRymes() {
        mRymes = new ArrayList<>();
        QuestionType t = RYME;
        add("Arm", t);
        add("Hus", t);
        add("Lax", t);
        add("Hår", t);
        add("Öl", t);
        add("Vin", t);
        add("Skumpa", t);
        add("Hoppa", t);
        add("Fest", t);
        add("Hus", t);
        add("Sprit", t);
        add("Dans", t);
        add("Spel", t);
        add("Flaska", t);
        add("Full", t);
        add("Ljuga", t);
        add("Drake", t);
        add("Mat", t);
        add("Ung", t);
        add("Bar", t);
        add("Sång", t);
        add("Boll", t);
        //add("", t);
    }

    /*
    Grammar: Start with capital letter, no dot.
     */
    private void createCategories() {
        mCategories = new ArrayList<>();
        QuestionType t = CATEGORY;
        add("Fotbollslag", t);
        add("Bilmärken", t);
        add("Filmer med Leonardo DiCaprio", t);
        add("Ölmärken", t);
        add("Paradise Hotel-deltagare", t);
        add("Klädmärken", t);
        add("Influencers", t);
        add("Könssjukdomar", t);
        add("Synonymer till att ha sex", t);
        add("Norska kändisar", t);
        add("Carola låtar", t);
        add("Skräckfilmer", t);
        add("Amerikanska delstater", t);
        add("Primtal", t);
        add("Färger", t);
        add("Disneyfilmer", t);
        add("Drickspel", t);
        add("Rappare", t);
        add("Artister som sjunger på svenska", t);
        add("Pojkband", t);
        add("Vinmärken", t);
        add("Podcasts", t);
        add("Skådespelare", t);
        add("Smink- och hårproduktsmärken", t);
        add("Pizzasorter", t);
        add("Youtubers", t);
        add("Streamingtjänster", t);
        add("Spritmärken", t);
        add("Ed Sheeran låtar", t);
        add("Eminem låtar", t);
        add("Karaktärer i Game Of Thrones", t);
        add("Filmer med Jennifer Aniston", t);
        add("Brädspel", t);
        add("Pokémon", t);
        add("Spelare i svenska fotbollslandslaget", t);
        add("Kungligheter", t);
        add("Deltagare i Melodifestivalen", t);
        add("Kända familjer", t);
        add("Amerikanska komediserier", t);
        add("Idol-vinnare", t);
        add("Svenska Hollywoodfruar", t);
        add("Sexställningar", t);
        add("Drinkar", t);
        add("Mobiltillverkare", t);
        add("TV-/Datorspel", t);
        //add("", t);
    }

    /*
    *A* --> A player in the game.
    *P* --> The partner to the player.
    {S} --> Secret mission

    Grammar: Start sentence with capital letter, finnish sentence with dot.
     */
    private void createMissions() {
        mMissionCouple = new ArrayList<>();
        mMissionMale = new ArrayList<>();
        mMissionFemale = new ArrayList<>();
        mMissionGeneral = new ArrayList<>();
        add("Ge *P* frukost på sängen imorgon.", MISSION_COUPLE);
        add("Spela upp en låt och låt de andra gissa vilken det är. Alla utom den som gissade rätt dricker.\nOm ingen gissade rätt dricker du.", MISSION_GENERAL);
        add("Gå in på toan och vänd ut och in på dina kalsonger.", MISSION_MALE);
        add("Drick bara med vänsterhanden under resten av spelet.", MISSION_GENERAL);
        add("Hitta på ett uppdrag till *A* som involverar gymnastik.", MISSION_GENERAL);
        add("Prata skånska tills *A* har fått nog.", MISSION_GENERAL);
        add("Imitera en säl.", MISSION_GENERAL);
        add("Peka på den person som du tror har en kondom med sig. Om du har rätt dricker personen du pekade på annars dricker du.", MISSION_GENERAL);
        add("Dela ut en klunk till den sötaste personen ikväll.", MISSION_GENERAL);
        add("Låt *A* rita en mustasch på dig.", MISSION_GENERAL);
        add("Visa gruppen hur bra du är på att rappa.", MISSION_GENERAL);
        add("Gilla Sean Banans facebooksida.", MISSION_GENERAL);
        add("Rita en gubbe runt din navel och prata med den tills du får ett annat uppdrag.", MISSION_GENERAL);
        add("Ge bort en klunk av din dricka till någon som behöver den mer.", MISSION_GENERAL);
        add("Tejpa fast din nuvarande dricka i handen och ta inte loss den förens du har druckit upp den.", MISSION_GENERAL);
        add("Dela ut en klunk till alla som är nyktrare än *A*.", MISSION_GENERAL);
        add("Dela ut en klunk till alla som är längre än *A*.", MISSION_GENERAL);
        add("Immitera Zlatan.", MISSION_GENERAL);
        add("{S}Säg halleluja varje gång du druckit upp ditt glas resten av kvällen.", MISSION_GENERAL);
        add("Ta en body shot från *A*.", MISSION_GENERAL);
        add("Låt *A* blanda en drink till dig.", MISSION_GENERAL);
        add("Ge valfritt uppdrag till *A*.", MISSION_GENERAL);
        add("Byt ut din dricka mot en blandning av alla spelares drickor.", MISSION_GENERAL);
        add("{S}Bjud *A* på något att dricka samtidigt som du hämtar din nästa.", MISSION_GENERAL);
        add("Utmana *A* på en dricktävling.", MISSION_GENERAL);
        add("{S}Lämna toadörren olåst nästa gång du går på toa.", MISSION_GENERAL);
        add("{S}Lägg fem svenska schlagerlåtar i kö på kvällens musikspelare utan att bli påkommen.", MISSION_GENERAL);
        add("Byt ett klädesplagg med *A* och ha på dig det under resten av spelet.", MISSION_GENERAL);
        add("{S}Testa dina ficktjuvsfärdigheter genom att någon gång under kvällen försöka ta mobiltelefonen ur fickan på *A*.", MISSION_GENERAL);
        add("Spela upp en låt som beskriver dina känslor för *A*.", MISSION_GENERAL);
        add("Drick ur sugrör resten av kvällen.", MISSION_GENERAL);
        add("Varje gång *A* dricker måste du också dricka. Detta gäller tills du får ett nytt uppdrag.", MISSION_GENERAL);
        add("Prata som Arnold Schwarzenegger tills du får ett nytt uppdrag.", MISSION_GENERAL);
        add("{S}Ge ut uppdraget till valfri person att stava Arnold Schwarzeneggers efternamn. Personen får ta en klunk för varje fel den gör.", MISSION_GENERAL);
    }

    /*
     *M* --> A male player in the game.
     *F* --> A female player in the game.
     */
    private void createMissionsWithFemalesForMales() {
        QuestionType t = MISSION_MALE;
        add("{S}Kalla *F* för min drottning under resten av kvällen.", t);
        add("Ditt namn är nu Jeeves. Agera butler åt *F*. Uppdraget är över när hon kallar dig för ditt riktiga namn", t);
    }

    private void createMissionsWithMalesForFemales() {
        QuestionType t = MISSION_FEMALE;
        add("Hjälp *M* att sminka sig lite.", t);
        add("Vaxa valfri kroppsdel på *M* som ni tillsammans kommer överens om.", t);
        add("Framför en duett tillsammans med *M*. Gruppen väljer låt.", t);
    }

    private void createMissionsWithMalesForMales() {
        QuestionType t = MISSION_MALE;
        add("{S}Kalla *M* brother from another mother under resten av kvällen.", t);
    }

    private void createMissionsWithFemalesForFemales() {
        QuestionType t = MISSION_FEMALE;
        add("{S}Ta bilder med *F* varje chans du får under kvällen.", t);
    }

    private void createMissionsWithMalesForAll() {
        QuestionType t = MISSION_GENERAL;
        add("{S}Upprepa allt *M* säger tills han upptäckt vad du gör.", t);
    }

    private void createMissionsWithFemalesForAll() {
        QuestionType t = MISSION_GENERAL;
        add("{S}Ge *F* en komplimang varje gång du dricker tills hon säger att du ska sluta.", t);
    }
}
