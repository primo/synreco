package model;

import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**  Returns text split into sentences with words other than pos and shorter tham wordMinLength filtered out.
 */
public class InputFilter {
    public static final String DEFAULT_SENTENCE_MODEL="en-sent.bin";
    public static final String DEFAULT_TAG_MODEL="en-pos-maxent.bin";
    public static final String DEFAULT_TOKEN_MODEL="en-token.bin";
    private InputStream sentenceModelData;
    private InputStream tokenModelData;
    private SentenceModel sentenceModel;
    private TokenizerModel tokenModel;
    private TokenizerME tokenizer;
    private SentenceDetectorME sdetector;
    private POSModel posModel;
    private POSTaggerME tagger;

    public InputFilter() throws IOException {
        // Start with a model, a model is learned from training data
        sentenceModelData = new FileInputStream(DEFAULT_SENTENCE_MODEL);
        sentenceModel= new SentenceModel(sentenceModelData);
        sdetector = new SentenceDetectorME(sentenceModel);
        tokenModelData = new FileInputStream(DEFAULT_TOKEN_MODEL);
        tokenModel = new TokenizerModel(tokenModelData);
        tokenizer = new TokenizerME(tokenModel);
        posModel = new POSModelLoader().load(new File(DEFAULT_TAG_MODEL));
        tagger = new POSTaggerME(posModel);
    }

    public void close() throws IOException {
        sentenceModelData.close();
        tokenModelData.close();
    }

    public List<List<String>> filter(String text, String pos, int wordMinLength) {
        List<List<String>> output = new ArrayList<List<String>>();
        String sentences[] = sdetector.sentDetect(text);
        for(String sentence: sentences) {
            List<String> currentValid = new ArrayList<String>();
            String tokens[] = tokenizer.tokenize(sentence);
            String tags[] = tagger.tag(tokens);
            for (int i = 0; i<tags.length; ++i) {
                final String token = tokens[i];
                if (tags[i].equals(pos) && token.length() >= wordMinLength) {
                    currentValid.add(token);
                }
            }
            output.add(currentValid);
        }
        return output;
    }
}
