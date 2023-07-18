package x.nexuskrop.minelint.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import x.nexuskrop.minelint.commands.CommandSyntaxParser;
import x.nexuskrop.minelint.commands.CommandSyntaxException;

class CommandLinterTest {
    @Test
    void readUnquotedString_emptyTest() {
        // Create a linter with an EMPTY STRING
        var linter = new CommandSyntaxParser("");

        Assertions.assertThrows(CommandSyntaxException.class, linter::readUnquotedString);
    }

    @Test
    void readUnquotedString_allMultiWordTest() {
        var linter = new CommandSyntaxParser("word1 word2 word3");

        var str1 = Assertions.assertDoesNotThrow(linter::readUnquotedString);
        var str2 = Assertions.assertDoesNotThrow(linter::readUnquotedString);
        var str3 = Assertions.assertDoesNotThrow(linter::readUnquotedString);

        Assertions.assertEquals("word1", str1);
        Assertions.assertEquals("word2", str2);
        Assertions.assertEquals("word3", str3);
    }

    @Test
    void readUnquotedString_singleMultiWordTest() {
        var linter = new CommandSyntaxParser("word1 word2");

        var str = Assertions.assertDoesNotThrow(linter::readUnquotedString);
        Assertions.assertEquals("word1", str);
    }

    @Test
    void resetText_parse() {
        var linter = new CommandSyntaxParser("word1");

        var str1 = Assertions.assertDoesNotThrow(linter::readUnquotedString);
        linter.resetText("word2");

        var str2 = Assertions.assertDoesNotThrow(linter::readUnquotedString);

        Assertions.assertEquals("word1", str1);
        Assertions.assertEquals("word2", str2);
    }

    @Test
    void read_valueTest() {
        var linter = new CommandSyntaxParser("10 20 30.2 40.3");

        Assertions.assertEquals(10, Assertions.assertDoesNotThrow(linter::readInt));
        Assertions.assertEquals(20L, Assertions.assertDoesNotThrow(linter::readLong));
        Assertions.assertEquals(30.2F, Assertions.assertDoesNotThrow(linter::readFloat));
        Assertions.assertEquals(40.3, Assertions.assertDoesNotThrow(linter::readDouble));
    }

    @Test
    void readBoolean_legal() {
        var linter = new CommandSyntaxParser("true false");
        
        Assertions.assertEquals(true, Assertions.assertDoesNotThrow(linter::readBoolean));
        Assertions.assertEquals(false, Assertions.assertDoesNotThrow(linter::readBoolean));
    }

    @Test
    void readBoolean_illegal() {
        var linter = new CommandSyntaxParser("TRUE");

        Assertions.assertThrows(CommandSyntaxException.class, linter::readBoolean);
    }

    @Test
    void readQuotedString_autoVary() {
        var linter = new CommandSyntaxParser("'single' \"double\"");

        var str1 = Assertions.assertDoesNotThrow(linter::readQuotedString);
        var str2 = Assertions.assertDoesNotThrow(linter::readQuotedString);

        Assertions.assertEquals("single", str1);
        Assertions.assertEquals("double", str2);
    }

    @Test
    void readGreedyString_test() {
        var linter = new CommandSyntaxParser("say Hey This Great Something!!!");

        Assertions.assertEquals("say", Assertions.assertDoesNotThrow(linter::readUnquotedString));
        Assertions.assertEquals("Hey This Great Something!!!", Assertions.assertDoesNotThrow(linter::readGreedyString));
    }

}
