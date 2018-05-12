
/**
 * Copyright 2016 University of Zurich
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import ngram.ExampleClient;

import java.io.IOException;
import java.util.ArrayList;

public class RunMe {

    /*
     * download the interaction data and unzip it into the root of this project (at
     * the level of the pom.xml). Unpack it, you should now have a folder that
     * includes a bunch of folders that have dates as names and that contain .zip
     * files.
     */
    public static String eventsDir = "Events-170301-2";

    /*
     * download the context data and follow the same instructions as before.
     */
    public static String contextsDir = "Contexts-170503";

    public static void main(String[] args) throws IOException {

        // BASIC DATA READING

        // new GettingStarted(eventsDir).run();
        //new CountEventTypeExample(eventsDir).run();
        // new GettingStartedContexts(contextsDir).run();

        // RSSE RELATED EXAMPLES
        //new BMNMining().run();
        /**
         * Example Client Usage
         */

        ExampleClient client = new ExampleClient();
        ArrayList<String> clientRequest = new ArrayList<>();

        clientRequest.add("output/System.data.Linq.txt");
        clientRequest.add("<System.Drawing.Drawing2D,new>,<System.Drawing.Drawing2D,StartFigure>");

        System.out.println(client.query(clientRequest));
    }
};