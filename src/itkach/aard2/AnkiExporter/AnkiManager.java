package itkach.aard2.AnkiExporter;

import android.content.Context;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class AnkiManager {

    public void addEntry(String url){
        String content = Downloader.getWordDef(url);
        String[] wordsplit = split(content);
        long deckId = getDeckId();
        long modelId = getModelId();
        String[] fields = new String[] {wordsplit[0], wordsplit[1]};
        if(!helper.isAlreadyIn(modelId,wordsplit[0])){
            helper.getApi().addNote(modelId, deckId, fields, null);
        }
    }
    public void removeWord(String s){

    }
    public AnkiManager() {
    }


    private void initAnki() {
        createDeckIfNeeded();
        createModelIfNeeded();
    }
    private String[] split(String content){
        final Pattern pattern = Pattern.compile("<h1>(.+?)</h1>", Pattern.DOTALL);
        final Matcher matcher = pattern.matcher(content);
        matcher.find();
        String[] ret = new String[2];
        ret[0] = matcher.group(1);
        ret[1] = content.substring(matcher.end());
        return ret;
    }

    /**
     * get the deck id
     * @return might be null if there was a problem
     */
    private void createDeckIfNeeded() {
        Long deckId = helper.findDeckIdByName(DECK_NAME);
        if (deckId == null) {
            helper.getApi().addNewDeck(DECK_NAME);
        }
    }

    private Long getDeckId() {
         return helper.findDeckIdByName(DECK_NAME);
    }
    /**
     * get model id
     * @return might be null if there was an error
     */
    private void createModelIfNeeded() {
        Long mid = helper.findModelIdByName(MODEL_NAME, NB_FIELDS);
        if (mid == null) {
            helper.getApi().addNewBasicModel(MODEL_NAME);
        }
    }

    private Long getModelId() {
        return helper.findModelIdByName(MODEL_NAME, NB_FIELDS);
    }

    public void buildAnkiHelper(Context context) {
        helper = new AnkiHelper(context);
        ctx = context;
        initAnki();
    }
    private Context ctx;
    private AnkiHelper helper;

    public static final String DECK_NAME = "Aard2";
    public static final String MODEL_NAME = "rawDumpFromAard2";
    public static final int NB_FIELDS = 2;
}
