package com.wistron.occ.parser;

import java.io.IOException;
import java.io.InputStream;

public interface Parser {

    void process(InputStream inputStream) throws IOException;

}
