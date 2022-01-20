package lexer;

import lexer.tokens.*;

public final class Lexer {

        private static int ch = ' ';
        private static int column = 0;
        public static int tokenColumn = 0;
        private static String lexString;

        public static void setLexString(String s){
            lexString = s;
            column =0;
            ch = ' ';
        }

        public static Token lex() {

            //skip whitespace
            while (ch == ' ' || ch == '\t' || ch == 13) {
                getChar();
            }

            tokenColumn = column;

            switch ((char)ch) {

                case ')' : {
                    getChar();
                    return new RParenToken();
                }

                case '(' : {
                    getChar();
                    return new LParenToken();
                }

                case EmptyToken.code:{
                    getChar();
                    return new EmptyToken();
                }

                case OrToken.code: {
                    getChar();
                    return new OrToken();
                }
                case AndToken.code: {
                    getChar();
                    return new AndToken();
                }
                case NotToken.code:
                    getChar();
                    return new NotToken();
                case 'T':
                    getChar();
                    return new TrueToken();
                case 'F':
                    getChar();
                    return new FalseToken();
                case ',':
                    getChar();
                    return new CommaToken();
                case ImpliesToken.code:
                    getChar();
                    return new ImpliesToken();
                case NDToken.code:
                    getChar();
                    return new NDToken();
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'E':
                case 'G':
                case 'H':
                case 'I':
                case 'J':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'S':
                case 'U':
                case 'W':
                case 'X':
                case 'Y':
                case 'Z':
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                case 'g':
                case 'h':
                case 'i':
                case 'j':
                case 'k':
                case 'l':
                case 'm':
                case 'n':
                case 'o':
                case 'p':
                case 'q':
                case 'r':
                case 's':
                case 't':
                case 'u':
                case 'w':
                case 'x':
                case 'y':
                case 'z':{
                    return getCharStream();
                }
                case (char)-1:{
                    return new EOIToken();
                }
                default:{
                    return getIllegalInput();
                }
            }
        }

        private static Token getCharStream() {
            char firstCharacter=(char)ch;
            String characterStream = firstCharacter + "";

            getChar();

            while(Character.isAlphabetic(ch)) {
                characterStream += (char)ch;
                getChar();
            }

            return new IdentToken(characterStream);
        }

        private static Token getIllegalInput() {
            char firstChar = (char) ch;
            String illegalStream = firstChar + "";
            getChar();

            while (!Character.isWhitespace(ch) && ch!=-1) {
                illegalStream += (char) ch;
                getChar();
            }

            return new IllegalStringToken(illegalStream);
        }

        private static void getChar() {
            try {
                if (column >= lexString.length()) {
                    ch=-1;
                    return;
                }
                ch = lexString.charAt(column);

                column++;

            } catch (Exception e) {
                System.out.println(e);
                System.exit(1);
            }
        }
    }