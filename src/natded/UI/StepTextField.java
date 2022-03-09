package natded.UI;

import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lexer.Lexer;
import lexer.tokens.*;
import parser.Clause;
import parser.Parser;

import java.lang.reflect.InvocationTargetException;

import static natded.Main.DISPLAY_HEIGHT;
import static natded.Main.DISPLAY_WIDTH;
import static natded.NatDedUtilities.logicSymbols;
import static natded.UI.NDScene.WINDOW_BACKGROUND_COLOR;

public class StepTextField extends TextField {

    private final double MIN_WIDTH = DISPLAY_WIDTH/15;

    StepTextField() {
        super();
        this.setMinWidth(MIN_WIDTH);
        this.setFont(new Font(DISPLAY_HEIGHT/60));
        this.setEditableField(true);
        this.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && StepTextField.this.getContent().length()>0){
                Parser.error = false;
                Lexer.setLexString(StepTextField.this.getText());
                Parser.t = Lexer.lex();
                Clause.parse();
                if (Parser.error) {
                    StepTextField.this.setStyle("-fx-text-fill: darkred;");
                } else {
                    StepTextField.this.setStyle("-fx-text-fill: black;");
                }
            } else {
                StepTextField.this.setStyle("-fx-text-fill: black;");
            }
    });

        //set listeners to replace keyboard keys as aliases for each logical symbol
        this.getContent().addListener((observable, oldValue, newValue) -> {
            for (Class<? extends Token> symbol : logicSymbols){
                String[] keys;
                String code;
                try {
                    keys = (String[])symbol.getMethod("getKeys").invoke(null);
                    code = (String)symbol.getMethod("getString").invoke(null);
                    for (String key : keys) {
                        String s = newValue.replace(key, code);
                        if (!s.equals(newValue)) {
                            this.setText(s);
                        }            }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // set field width to be based on current text in box
            Text text = new Text(StepTextField.this.getText());
            text.setFont(StepTextField.this.getFont());
            StepTextField.this.setPrefWidth(text.getLayoutBounds().getWidth()+ 2 * StepTextField.this.getPadding().getLeft() + 2d);

        });
    }

    /**
     * toggle whether field is editable and adjust style accordingly
     * @param editable whether field should be editable or not
     */
    public void setEditableField(boolean editable){
        setEditable(editable);
        if (editable) {
            setCursor(Cursor.TEXT);
            setStyle("-fx-background-color: white; -fx-border-color: lightgray; -fx-border-width: 1");
        } else {
            setStyle("-fx-background-color: "+ WINDOW_BACKGROUND_COLOR + "; -fx-border-width: 0");
            setCursor(Cursor.DEFAULT);
        }
    }


}
