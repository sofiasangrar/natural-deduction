package lexer;

import lexer.tokens.*;

public final class Lexer {

		// store the current character being processed
        private static int ch = ' ';

        // stores the column of the current character
        private static int characterCountOnLine = 0;

        // stores the column number for the first character of a token
        private static int characterForToken = 0;

        private static String lexString;

        public static void setLexString(String s){
            lexString = s;
            characterCountOnLine=0;
            ch = ' ';
        }

        //return the next token in the logo code
        public static Token lex() {

            //skip the white space
            while (ch == ' ' || ch == '\t' || ch == 13) {

                switch (ch) {
                    case '\t': {
                        // each tab is worth 4 characters but as the get char method increments the the column count,
						// we increment it by 3 here and then it is increased by 1, so 4 overall
                        characterCountOnLine += 3;
                        break;
                    }
                }
                getChar();
            }

            // sets the column of the first character to the column of the current character
            characterForToken = characterCountOnLine;

            //identify new character and return correct token
            switch ((char)ch) {

                case ')' : {
                    getChar();
                    return new RParenToken(characterForToken);
                }

                case '(' : {
                    getChar();
                    return new LParenToken(characterForToken);
                }

                case '%':{
                    getChar();
                    return new EmptyToken(characterForToken);
                }

                case 'V':{
                    getChar();
                    return new OrToken(characterForToken);
                }
                case '^':{
                    getChar();
                    return new AndToken(characterForToken);
                }
                case '!':
                    getChar();
                    return new NotToken(characterForToken);
                case 'T':
                    getChar();
                    return new TrueToken(characterForToken);
                case 'F':
                    getChar();
                    return new FalseToken(characterForToken);
                case ',':
                    getChar();
                    return new CommaToken(characterForToken);
                case '=':
                case '|':
                    return operatorCheck();
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
                case 'v':
                case 'w':
                case 'x':
                case 'y':
                case 'z':{
                    return checkCharacterStream();
                }
                case (char)-1:{
                    return new EOIToken();
                }
                default : {
                    // error returned get a new error
                    char firstChar = (char) ch;
                    getChar();
                    return checkIllegalInputStream(firstChar);
                }
            }
        }

        //check what operator a token is
        private static Token operatorCheck(){
            char char1 = (char)ch;
            ch = getChar();
            char char2=(char)ch;
            ch = getChar();


            // checks the second character to see if it a token involving an equals
            if (char1 == '=' && char2 == '>') {
                return new ImpliesToken(characterForToken);
            } else if (char1 == '|' && char2 == '-'){
                return new NDToken(characterForToken);
            }
            // if the token fails all checks then it will begin checking illegal input
            return checkIllegalInputStream(char1);
        }

        // create a character stream and identify whether it is a keyword or identifier
        private static Token checkCharacterStream() {
            char firstCharacter=(char)ch;
            String characterStream = firstCharacter + "";

            // gets the second character
            getChar();

            // checks if the character is alphabetic or is numeric.
            while(Character.isAlphabetic(ch) && ch!='F' && ch!='T' && ch!='V') {
                characterStream += (char)ch;
                getChar();
            }

            return new IdentToken(characterStream, characterForToken);

        }



        //receive the rest of the illegal input
    private static Token checkIllegalInputStream(char firstChar) {
            String illegalStream = firstChar + "";

        // checks if the next character is whitespace
        while (!Character.isWhitespace(ch)) {
            illegalStream += (char) ch;
            getChar();
        }

        return new IllegalStringToken(illegalStream, characterForToken);
    }



        //this reads chars from stdin. You can read in files any way you want, using FileReader etc.
        private static int getChar() {
            try {
                if (characterCountOnLine>=lexString.length()) {
                    ch=-1;
                    return -1;
                }
                ch = lexString.charAt(characterCountOnLine);

                characterCountOnLine++;
            } catch (Exception e) {
                System.out.println(e);
                System.exit(1);
            }
            return ch;
        }
    }