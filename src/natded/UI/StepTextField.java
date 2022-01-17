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

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

public class StepTextField extends TextField {

    private LeafNode parent;

    public StepTextField(LeafNode parent) {
        super();
        this.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue){
                Parser.error = false;
                Lexer.setLexString(StepTextField.this.getText());
                Parser.t = Lexer.lex();
                Clause.parse(0);
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
        });
    }


}
