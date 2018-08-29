package itkach.aard2;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnkiDroidExporter {
    private AnkiDroidHelper mAnkiDroid;


    public void buildAnkiHelper(Context context) {
        mAnkiDroid = new AnkiDroidHelper(context);
    }

    /**
     * get the deck id
     * @return might be null if there was a problem
     */
    private Long getDeckId() {
        Long did = mAnkiDroid.findDeckIdByName(AnkiDroidConfig.DECK_NAME);
        if (did == null) {
            did = mAnkiDroid.getApi().addNewDeck(AnkiDroidConfig.DECK_NAME);
            mAnkiDroid.storeDeckReference(AnkiDroidConfig.DECK_NAME, did);
        }
        return did;
    }

    /**
     * get model id
     * @return might be null if there was an error
     */
    private Long getModelId() {
        Long mid = mAnkiDroid.findModelIdByName(AnkiDroidConfig.MODEL_NAME, AnkiDroidConfig.FIELDS.length);
        if (mid == null) {
            mid = mAnkiDroid.getApi().addNewCustomModel(AnkiDroidConfig.MODEL_NAME, AnkiDroidConfig.FIELDS,
                    AnkiDroidConfig.CARD_NAMES, AnkiDroidConfig.QFMT, AnkiDroidConfig.AFMT, AnkiDroidConfig.CSS, getDeckId(), null);
            mAnkiDroid.storeModelReference(AnkiDroidConfig.MODEL_NAME, mid);
        }
        return mid;
    }

    /**
     * Use the instant-add API to add flashcards directly to AnkiDroid.
     * @param data List of cards to be added. Each card has a HashMap of field name / field value pairs.
     */
    private void addCardsToAnkiDroid(final List<Map<String, String>> data) {
        Long deckId = getDeckId();
        Long modelId = getModelId();
        if ((deckId == null) || (modelId == null)) {
            // we had an API error, report failure and return
            //Toast.makeText(MainActivity.this, getResources().getString(R.string.card_add_fail), Toast.LENGTH_LONG).show();
            return;
        }
        String[] fieldNames = mAnkiDroid.getApi().getFieldList(modelId);
        if (fieldNames == null) {
            // we had an API error, report failure and return
            //Toast.makeText(MainActivity.this, getResources().getString(R.string.card_add_fail), Toast.LENGTH_LONG).show();
            return;
        }
        // Build list of fields and tags
        LinkedList<String []> fields = new LinkedList<String []>();
        LinkedList<Set<String>> tags = new LinkedList<Set<String>>();
        for (Map<String, String> fieldMap: data) {
            // Build a field map accounting for the fact that the user could have changed the fields in the model
            String[] flds = new String[fieldNames.length];
            for (int i = 0; i < flds.length; i++) {
                // Fill up the fields one-by-one until either all fields are filled or we run out of fields to send
                if (i < AnkiDroidConfig.FIELDS.length) {
                    flds[i] = fieldMap.get(AnkiDroidConfig.FIELDS[i]);
                }
            }
            tags.add(AnkiDroidConfig.TAGS);
            fields.add(flds);
        }
        // Remove any duplicates from the LinkedLists and then add over the API
        mAnkiDroid.removeDuplicates(fields, tags, modelId);
        int added = mAnkiDroid.getApi().addNotes(modelId, deckId, fields, tags);
    }
}
