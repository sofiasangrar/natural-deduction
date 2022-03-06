package natded.UI;

import com.sun.javafx.scene.control.skin.Utils;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lexer.Lexer;
import lexer.tokens.*;
import parser.Clause;
import parser.Parser;

import static natded.Main.DISPLAY_HEIGHT;
import static natded.Main.DISPLAY_WIDTH;

public class StepTextField extends TextField {

    private LeafNode parent;
    private final double minWidth = DISPLAY_WIDTH/15;

    public StepTextField(LeafNode parent) {
        super();
        //Text heightText = new Text("!");
        //heightText.setFont(StepTextField.this.getFont());
        //StepTextField.this.setMinHeight(heightText.getLayoutBounds().getHeight()+ 2 * StepTextField.this.getPadding().getTop() + 2d);
        this.setMinWidth(minWidth);
        this.setFont(new Font(DISPLAY_HEIGHT/60));
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
        this.parent = parent;
        this.getContent().addListener((observable, oldValue, newValue) -> {
            for (String key : AndToken.getKeys()) {
                String s = newValue.replace(key, AndToken.getString());
                if (!s.equals(newValue)) {
                    this.setText(s);
                }
            }

            for (String key : EmptyToken.getKeys()) {
                String s = newValue.replace(key, EmptyToken.getString());
                if (!s.equals(newValue)) {
                    this.setText(s);
                }            }

            for (String key : ImpliesToken.getKeys()) {
                String s = newValue.replace(key, ImpliesToken.getString());
                if (!s.equals(newValue)) {
                    this.setText(s);
                }            }

            for (String key : NDToken.getKeys()) {
                String s = newValue.replace(key, NDToken.getString());
                if (!s.equals(newValue)) {
                    this.setText(s);
                }            }

            for (String key : NotToken.getKeys()) {
                String s = newValue.replace(key, NotToken.getString());
                if (!s.equals(newValue)) {
                    this.setText(s);
                }            }

            for (String key : OrToken.getKeys()) {
                String s = newValue.replace(key, OrToken.getString());
                if (!s.equals(newValue)) {
                    this.setText(s);
                }            }
            Text text = new Text(StepTextField.this.getText());
            text.setFont(StepTextField.this.getFont());
            StepTextField.this.setPrefWidth(text.getLayoutBounds().getWidth()+ 2 * StepTextField.this.getPadding().getLeft() + 2d);

        });
    }


}
