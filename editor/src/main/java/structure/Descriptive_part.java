package structure;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Created by Alex on 27.11.2016.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "descriptive_part")
public class Descriptive_part {

    public Memory_block getMemory_block() {
        return memory_block;
    }

    public Alphabet getAlphabet() {
        return alphabet;
    }

    @JacksonXmlProperty
    Memory_block memory_block;

    @JacksonXmlProperty
    Alphabet alphabet;

    public Descriptive_part() {
    }

    public Descriptive_part(Memory_block memory_block) {
        this.memory_block = memory_block;
    }

    public Descriptive_part(Memory_block memory_block, Alphabet alphabet) {
        this.memory_block = memory_block;
        this.alphabet = alphabet;
    }
}
