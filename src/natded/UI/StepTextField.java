package natded.UI;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import lexer.Lexer;
import lexer.tokens.*;
import natded.StepNode;
import parser.Clause;
import parser.Parser;

import java.util.ArrayList;

public class StepTextField extends TextField {

    private LeafNode parent;

    public StepTextField(LeafNode parent) {
        super();
        this.parent = parent;
        setOnMouseExited( e -> {
            if (!e.getSource().equals(this)){
                Lexer.setLexString(this.getText());
                Clause.parse(0);
                if (Parser.error) {
                    this.setStyle("-fx-text-fill: darkred;");
                }
            }
        });
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
        });
    }


}
