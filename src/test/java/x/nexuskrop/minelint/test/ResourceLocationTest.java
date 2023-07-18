package x.nexuskrop.minelint.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import x.nexuskrop.minelint.parsing.data.ResourceLocation;

public class ResourceLocationTest {
    @Test
    public void toStringTest() {
        var rl = new ResourceLocation("test", "tested/test_file");

        Assertions.assertEquals("test:tested/test_file", rl.toString());
    }
}
