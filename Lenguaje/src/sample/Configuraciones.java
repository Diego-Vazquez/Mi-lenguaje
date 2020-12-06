package sample;

import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Configuraciones {
    private CodeArea codeArea;
    private ExecutorService executor;
    private VBox vBox;
    private TextArea Consola;
    private int weight;
    private int hight;

    public Configuraciones(VBox vBox, int weight, int hight) {
        this.vBox = vBox;
        this.weight = weight;
        this.hight = hight;
        principal();
    }

    public Configuraciones(VBox vBox, TextArea consola, int weight, int hight) {
        this.vBox = vBox;
        Consola = consola;
        this.weight = weight;
        this.hight = hight;
        principal();
    }

    public Configuraciones() {
      principal();
    }


    //mis metodos
    public void remplazar_texto(String texto){
        codeArea.clear();
        codeArea.replaceText(0, 0, texto);
    }

    public String obtener_texto(){
        return codeArea.getText();
    }

    public void clear(){
        codeArea.clear();
    }

    //3333333333333333333333333333333333333333333333333333333333

    public void principal(){
        executor = Executors.newSingleThreadExecutor();
        codeArea = new CodeArea();
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        Subscription cleanupWhenDone = codeArea.multiPlainChanges()
                .successionEnds(Duration.ofMillis(100))
                .supplyTask(this::computeHighlightingAsync)
                .awaitLatest(codeArea.multiPlainChanges())
                .filterMap(t -> {
                    if(t.isSuccess()) {
                        return Optional.of(t.get());
                    } else {
                        t.getFailure().printStackTrace();
                        return Optional.empty();
                    }
                })
                .subscribe(this::applyHighlighting);

        // call when no longer need it: `cleanupWhenFinished.unsubscribe();`

        codeArea.replaceText(0, 0, sampleCode);
        codeArea.setStyle("-fx-font-size: 16; -fx-font-family: HATTEN;");
        StackPane stackPane=new StackPane(new VirtualizedScrollPane<>(codeArea));
        stackPane.setPrefSize(this.weight,this.hight);
        vBox.getChildren().add(stackPane);
    }





    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        String text = codeArea.getText();
        Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
            @Override
            protected StyleSpans<Collection<String>> call() throws Exception {
                return computeHighlighting(text);
            }
        };
        executor.execute(task);
        return task;
    }

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        codeArea.setStyleSpans(0, highlighting);
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :
                                                                    matcher.group("COMMENT") != null ? "comment" :
                                                                            null; /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }


    private static final String[] KEYWORDS = new String[]{
            //aqui van las palabras reservadas de mi lenguaje
            "dobl","str","int","if","else","for","do","switch",
            "bol","case","print","while","and","or","not","true","false","void"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    //private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";
    private static final String COMMENT_PATTERN = "##[^\n]*" + "|" + "#(.|\\R)*?#";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN +")"
                    + "|(?<PAREN>" + PAREN_PATTERN +")"
                    + "|(?<BRACE>" + BRACE_PATTERN +")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN +")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN +")"
                    + "|(?<STRING>" + STRING_PATTERN +")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN +")"
    );

    private static final String sampleCode = String.join("\n",new String[]{
            //lo que aparecera al inicio
            "proyecto {",
            "",
            "int main(void){",
            "print(\"hello world\");",
            "",
            "",
            "}",
            "}"
    });

    public void detener_hilo(){
        executor.shutdown();
        try{
            if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)){
                executor.shutdownNow();
            }
        }catch (InterruptedException e){
            executor.shutdownNow();
        }
    }
}
